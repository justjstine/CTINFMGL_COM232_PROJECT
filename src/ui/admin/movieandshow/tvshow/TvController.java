package ui.admin.movieandshow.tvshow;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;

import com.jfoenix.controls.JFXButton;

import data.Show;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import database.DatabaseHandler;

public class TvController implements Initializable {

    @FXML
    private TableColumn<Show, String> tvshowidcolumn, tvtitlecolumn, tvreleasedatecolumn, tvclassificationcolumn, genrecolumn;
    @FXML
    private TableView<Show> tvtable;
    @FXML 
    private TableColumn<Show, Integer> popularitycolumn;
    @FXML
    private TextField tvtitletextfield;
    @FXML
    private DatePicker tvreleasedate;
    @FXML
    private ComboBox<String> tvcontentratingbox;
    @FXML 
    private CheckComboBox<String> genrecombo;
    @FXML
    private Button logoutbutton, usersbutton;
    @FXML
    private JFXButton createbutton, deletebutton, updatebutton, showcrbutton, showgenrebutton;

    private final ObservableList<Show> showlist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeColumns();
        displayShows();
        loadTvContentRatings();
        loadGenres();
    }
    
    public void initializeColumns(){
        tvshowidcolumn.setCellValueFactory(new PropertyValueFactory<>("showID"));
        tvtitlecolumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        tvreleasedatecolumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        tvclassificationcolumn.setCellValueFactory(new PropertyValueFactory<>("contentRating"));
        genrecolumn.setCellValueFactory(new PropertyValueFactory<>("genres"));
        popularitycolumn.setCellValueFactory(new PropertyValueFactory<>("popularityScore"));
    }

    @FXML
    public void createShow(ActionEvent event) {
        String title = tvtitletextfield.getText().trim();
        String releaseDate = tvreleasedate.getValue() != null ? tvreleasedate.getValue().toString() : "";
        String contentRating = tvcontentratingbox.getValue();
        ObservableList<String> selectedGenres = genrecombo.getCheckModel().getCheckedItems();
    
        if (title.isEmpty() || releaseDate.isEmpty() || contentRating == null || selectedGenres.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
            return;
        }
    
        Show show = new Show("0", title, releaseDate, contentRating, selectedGenres, 0);
        if (DatabaseHandler.addShow(show)) {
            showAlert(Alert.AlertType.INFORMATION, "Show added successfully!");
            displayShows();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to add show.");
        }
    }

    @FXML
    public void updateShow(ActionEvent event) {
        Show selectedShow = tvtable.getSelectionModel().getSelectedItem();
        if (selectedShow == null) {
            showAlert(Alert.AlertType.ERROR, "Please select a show to update.");
            return;
        }

        String newTitle = tvtitletextfield.getText().trim();
        String newReleaseDate = tvreleasedate.getValue() != null ? tvreleasedate.getValue().toString() : "";
        String newContentRating = tvcontentratingbox.getValue();
        ObservableList<String> selectedGenres = genrecombo.getCheckModel().getCheckedItems();

        if (newTitle.isEmpty() || newReleaseDate.isEmpty() || newContentRating == null || selectedGenres.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
            return;
        }

        Show updatedShow = new Show(selectedShow.getShowID(), newTitle, newReleaseDate, newContentRating, selectedGenres, 0);

        if (DatabaseHandler.updateShow(updatedShow)) {
            showAlert(Alert.AlertType.INFORMATION, "Show updated successfully!");
            displayShows();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to update show.");
        }
    }

    private void displayShows() {
        showlist.clear();
        ResultSet rs = DatabaseHandler.getShows();
        try {
            while (rs.next()) {
                List<String> genres = List.of(rs.getString("Genre").split(", "));
                showlist.add(new Show(
                    rs.getString("ShowID"),
                    rs.getString("Title"),
                    rs.getString("ReleaseDate"),
                    rs.getString("Classification"), // Use the correct column name here
                    genres,
                    rs.getInt("PopularityScore")
                ));
            }
            tvtable.setItems(showlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTvContentRatings() {
        ObservableList<String> contentRatings = FXCollections.observableArrayList();
        ResultSet rs = DatabaseHandler.getContentRating();
    
        try {
            while (rs.next()) {
                contentRatings.add(rs.getString("Classification")); // Adjust column name if necessary
            }
            tvcontentratingbox.setItems(contentRatings);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadGenres() {
        ObservableList<String> genres = FXCollections.observableArrayList();
        ResultSet rs = DatabaseHandler.getGenres(); // Assuming you have a method to get genres
    
        try {
            while (rs.next()) {
                genres.add(rs.getString("GenreName")); // Adjust column name if necessary
            }
            genrecombo.getItems().addAll(genres);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteShow(ActionEvent event) {
        Show selectedShow = tvtable.getSelectionModel().getSelectedItem();
        if (selectedShow == null) {
            showAlert(Alert.AlertType.ERROR, "Please select a show to delete.");
            return;
        }

        if (DatabaseHandler.deleteShow(Integer.parseInt(selectedShow.getShowID()))) {
            showAlert(Alert.AlertType.INFORMATION, "Show deleted successfully!");
            displayShows();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to delete show.");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void showTvContentRating(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/admin/movieandshow/contentrating/ContentRating2.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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
    private void usersBtnHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/user/AUser.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Pinoy Flix");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void movieBtnHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/movieandshow/movie/Movie.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Pinoy Flix");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void showGenreButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/movieandshow/genres/Genre2.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Pinoy Flix");
        stage.setScene(scene);
        stage.show();
    }
}