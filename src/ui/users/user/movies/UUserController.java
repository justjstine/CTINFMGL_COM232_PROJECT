package ui.users.user.movies;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import data.Movie;
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

public class UUserController implements Initializable{
    @FXML
    private Button logoutbutton, moviebtn, tvbtn, favoritetabbtn, moviesearchbtn;
    @FXML
    private JFXButton dislikebtn, likebtn, lovebtn, moviefavebtn;
    @FXML
    private TextField moviesearchtextfield;
    @FXML
    private TableColumn<Movie, String> classificationcolumn, releasedatecolumn, titlecolumn, genrecolumn;
    @FXML
    private TableColumn<Movie, Integer> popularitycolumn;
    @FXML
    private TableColumn<Movie, Boolean> IsFavoritecolumn;
    @FXML
    private TableView<Movie> usermovietable;
    @FXML
    private Label welcomelabel;

    private final ObservableList<Movie> movieList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeColumns();
        displayMovies();
    }

    public void initializeColumns(){
        titlecolumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        releasedatecolumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        classificationcolumn.setCellValueFactory(new PropertyValueFactory<>("contentRating"));
        genrecolumn.setCellValueFactory(new PropertyValueFactory<>("genres"));
        popularitycolumn.setCellValueFactory(new PropertyValueFactory<>("popularityScore"));
    }

    private void displayMovies() {
        movieList.clear();
        ResultSet rs = DatabaseHandler.getMovies();
        try {
            while (rs.next()) {
                List<String> genres = List.of(rs.getString("Genres").split(", "));
                movieList.add(new Movie(
                        rs.getString("MovieID"),
                        rs.getString("Title"),
                        rs.getString("ReleaseDate"),
                        rs.getString("Classification"),
                        genres,
                        rs.getInt("PopularityScore")
                ));
            }
            usermovietable.setItems(movieList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    public void displayName(String username){
        welcomelabel.setText("Welcome back, " + username);
    }

    @FXML
    private void searchMoviesHandler(ActionEvent event) {
        String searchTerm = moviesearchtextfield.getText().trim();
    
    // Call the updated searchMovies method
    ObservableList<Movie> searchResults = DatabaseHandler.searchMovies(searchTerm);
    // Update the table with search results
    usermovietable.setItems(searchResults);
    }



    @FXML
    private void likebtnHandler(ActionEvent event) {
        Movie selectedMovie = usermovietable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null && !selectedMovie.isVoted()) {
            int newPopularityScore = selectedMovie.getPopularityScore() + 1;
            selectedMovie.setPopularityScore(newPopularityScore);
            selectedMovie.setVoted(true);

            DatabaseHandler.updateMoviePopularity(selectedMovie.getMovieID(), newPopularityScore);

            usermovietable.refresh();
        }
    }

    @FXML
    private void dislikebtnHandler(ActionEvent event) {
        Movie selectedMovie = usermovietable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null && !selectedMovie.isVoted()) {
            int newPopularityScore = selectedMovie.getPopularityScore() - 1;
            selectedMovie.setPopularityScore(newPopularityScore);
            selectedMovie.setVoted(true);

            DatabaseHandler.updateMoviePopularity(selectedMovie.getMovieID(), newPopularityScore);

            usermovietable.refresh();
        }
    }

    @FXML
    private void lovebtnHandler(ActionEvent event) {
        Movie selectedMovie = usermovietable.getSelectionModel().getSelectedItem();
        if (selectedMovie != null && !selectedMovie.isVoted()) {
            int newPopularityScore = selectedMovie.getPopularityScore() + 2;
            selectedMovie.setPopularityScore(newPopularityScore);
            selectedMovie.setVoted(true);

            DatabaseHandler.updateMoviePopularity(selectedMovie.getMovieID(), newPopularityScore);

            usermovietable.refresh();
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
    private void tvbtnHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/users/user/tvshow/TVUUSER.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("TV Shows");
        stage.setScene(scene);
        stage.show();
    }
    
}
