package ui.users.user.tvshow;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import data.Show;
import database.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TVUUserController implements Initializable{
    @FXML
    private Button logoutbutton, moviebtn, tvbtn, favoritetabbtn, showsearchbtn;
    @FXML
    private JFXButton dislikebtn, likebtn, lovebtn, showfavebtn;
    @FXML
    private TextField showsearchtextfield;
    @FXML
    private TableColumn<Show, String> classificationcolumn, releasedatecolumn, titlecolumn, genrecolumn;
    @FXML
    private TableColumn<Show, Integer> popularitycolumn;
    @FXML
    private TableView<Show> usershowtable;
    
    @FXML
    private Label welcomelabel;

    private final ObservableList<Show> showList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeColumns();
        displayShows();
    }

    public void initializeColumns(){
        titlecolumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        releasedatecolumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        classificationcolumn.setCellValueFactory(new PropertyValueFactory<>("contentRating"));
        genrecolumn.setCellValueFactory(new PropertyValueFactory<>("genres"));
        popularitycolumn.setCellValueFactory(new PropertyValueFactory<>("popularityScore"));
    }

    private void displayShows() {
        showList.clear();
        ResultSet rs = DatabaseHandler.getShows();
        try {
            while (rs.next()) {
                List<String> genres = List.of(rs.getString("Genre").split(", "));
                showList.add(new Show(
                        rs.getString("ShowID"),
                        rs.getString("Title"),
                        rs.getString("ReleaseDate"),
                        rs.getString("Classification"),
                        genres,
                        rs.getInt("PopularityScore")
                ));
            }
            usershowtable.setItems(showList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    public void displayName(String username){
        welcomelabel.setText("Welcome back, " + username);
    }

    @FXML
    private void searchShowsHandler(ActionEvent event) {
        String searchTerm = showsearchtextfield.getText().trim();
        
        // Call the updated searchShows method
        ObservableList<Show> searchResults = DatabaseHandler.searchShows(searchTerm);
        // Update the table with search results
        usershowtable.setItems(searchResults);
    }

    @FXML
    private void likebtnHandler(ActionEvent event) {
        Show selectedShow = usershowtable.getSelectionModel().getSelectedItem();
        if (selectedShow != null && !selectedShow.isVoted()) {
            int newPopularityScore = selectedShow.getPopularityScore() + 1;
            selectedShow.setPopularityScore(newPopularityScore);
            selectedShow.setVoted(true);

            DatabaseHandler.updateShowPopularity(selectedShow.getShowID(), newPopularityScore);

            usershowtable.refresh();
        }
    }

    @FXML
    private void dislikebtnHandler(ActionEvent event) {
        Show selectedShow = usershowtable.getSelectionModel().getSelectedItem();
        if (selectedShow != null && !selectedShow.isVoted()) {
            int newPopularityScore = selectedShow.getPopularityScore() - 1;
            selectedShow.setPopularityScore(newPopularityScore);
            selectedShow.setVoted(true);

            DatabaseHandler.updateShowPopularity(selectedShow.getShowID(), newPopularityScore);

            usershowtable.refresh();
        }
    }

    @FXML
    private void lovebtnHandler(ActionEvent event) {
        Show selectedShow = usershowtable.getSelectionModel().getSelectedItem();
        if (selectedShow != null && !selectedShow.isVoted()) {
            int newPopularityScore = selectedShow.getPopularityScore() + 2;
            selectedShow.setPopularityScore(newPopularityScore);
            selectedShow.setVoted(true);

            DatabaseHandler.updateShowPopularity(selectedShow.getShowID(), newPopularityScore);

            usershowtable.refresh();
        }
    }

    @FXML
    private void logoutUser(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/alerts/LogoutPane.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.showAndWait();
    }

   @FXML
   private void moviebtnHandler(ActionEvent event) throws IOException {
       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(getClass().getResource("/ui/users/user/movies/MovieUUSER.fxml"));
       Parent parent = loader.load();
       Scene scene = new Scene(parent);
       Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       stage.setTitle("TV Shows");
       stage.setScene(scene);
       stage.show();
   }

   @FXML
    private void updUserInfoHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/users/user/updateaccount/UpdateUserInfo.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("TV Shows");
        stage.setScene(scene);
        stage.show();
    }
    
}