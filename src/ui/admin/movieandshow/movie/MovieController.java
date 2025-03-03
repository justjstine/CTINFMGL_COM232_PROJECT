package ui.admin.movieandshow.movie;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;

import com.jfoenix.controls.JFXButton;
import data.Movie;
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

public class MovieController implements Initializable {

    @FXML
    private TableColumn<Movie, String> movieidcolumn, titlecolumn, releasedatecolumn, classificationcolumn, genrecolumn;
    @FXML 
    private TableColumn<Movie, Integer> popularitycolumn;
    @FXML
    private TableView<Movie> movietable;
    @FXML
    private TextField titletextfield;
    @FXML
    private DatePicker releasedate;
    @FXML
    private ComboBox<String> contentratingbox;
    @FXML 
    private CheckComboBox<String> genrecombo;
    @FXML
    private Button logoutbutton, usersbutton;
    @FXML
    private TextField popularityscoretextfield;
    @FXML
    private JFXButton createbutton, deletebutton, updatebutton, showcrbutton;
 
    private final ObservableList<Movie> movieList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeColumns();
        displayMovies();
        loadContentRatings();
        loadGenres();
    }
    
    public void initializeColumns(){
        movieidcolumn.setCellValueFactory(new PropertyValueFactory<>("movieID"));
        titlecolumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        releasedatecolumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        classificationcolumn.setCellValueFactory(new PropertyValueFactory<>("contentRating"));
        genrecolumn.setCellValueFactory(new PropertyValueFactory<>("genres"));
        popularitycolumn.setCellValueFactory(new PropertyValueFactory<>("popularityScore"));

    }

    @FXML
    public void createMovie(ActionEvent event) {
        String title = titletextfield.getText().trim();
        String releaseDate = releasedate.getValue() != null ? releasedate.getValue().toString() : "";
        String contentRating = contentratingbox.getValue();
        ObservableList<String> selectedGenres = genrecombo.getCheckModel().getCheckedItems();
    
        if (title.isEmpty() || releaseDate.isEmpty() || contentRating == null || selectedGenres.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
            return;
        }
    
        Movie movie = new Movie("0", title, releaseDate, contentRating, selectedGenres,0);
        if (DatabaseHandler.addMovie(movie)) {
            showAlert(Alert.AlertType.INFORMATION, "Movie added successfully!");
            displayMovies();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to add movie.");
        }
    }

    @FXML
    public void updateMovie(ActionEvent event) {
        Movie selectedMovie = movietable.getSelectionModel().getSelectedItem();
        if (selectedMovie == null) {
            showAlert(Alert.AlertType.ERROR, "Please select a movie to update.");
            return;
        }

        String newTitle = titletextfield.getText().trim();
        String newReleaseDate = releasedate.getValue() != null ? releasedate.getValue().toString() : "";
        String newContentRating = contentratingbox.getValue();
        ObservableList<String> selectedGenres = genrecombo.getCheckModel().getCheckedItems();

        if (newTitle.isEmpty() || newReleaseDate.isEmpty() || newContentRating == null || selectedGenres.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all fields.");
            return;
        }

        Movie updatedMovie = new Movie(selectedMovie.getMovieID(), newTitle, newReleaseDate, newContentRating, selectedGenres, 0);

        if (DatabaseHandler.updateMovie(updatedMovie)) {
            showAlert(Alert.AlertType.INFORMATION, "Movie updated successfully!");
            displayMovies();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to update movie.");
        }
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
                    rs.getString("Classification"), // Use the correct column name here
                    genres,
                    rs.getInt("PopularityScore")
                ));
            }
            movietable.setItems(movieList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadContentRatings() {
        ObservableList<String> contentRatings = FXCollections.observableArrayList();
        ResultSet rs = DatabaseHandler.getContentRating();
    
        try {
            while (rs.next()) {
                contentRatings.add(rs.getString("Classification")); // Adjust column name if necessary
            }
            contentratingbox.setItems(contentRatings);
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
    public void deleteMovie(ActionEvent event) {
        Movie selectedMovie = movietable.getSelectionModel().getSelectedItem();
        if (selectedMovie == null) {
            showAlert(Alert.AlertType.ERROR, "Please select a movie to delete.");
            return;
        }

        if (DatabaseHandler.deleteMovie(Integer.parseInt(selectedMovie.getMovieID()))) {
            showAlert(Alert.AlertType.INFORMATION, "Movie deleted successfully!");
            displayMovies();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to delete movie.");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void showMovieContentRating(ActionEvent event) throws IOException{
       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(getClass().getResource("/ui/admin/movieandshow/contentrating/ContentRating.fxml"));
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
   private void tvbtnHandler(ActionEvent event) throws IOException {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/movieandshow/tvshow/Tvshow.fxml"));
      Parent parent = loader.load();
      Scene scene = new Scene(parent);
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.setTitle("Pinoy Flix");
      stage.setScene(scene);
      stage.show();
   }

   @FXML
   private void showgenrebutton(ActionEvent event) throws IOException {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/movieandshow/genres/Genre.fxml"));
      Parent parent = loader.load();
      Scene scene = new Scene(parent);
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.setTitle("Pinoy Flix");
      stage.setScene(scene);
      stage.show();
   }
}