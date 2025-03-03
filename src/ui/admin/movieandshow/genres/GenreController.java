package ui.admin.movieandshow.genres;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import data.Genre;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GenreController implements Initializable {
    ObservableList<Genre> genreList = FXCollections.observableArrayList();

    @FXML
    private JFXButton addgenrebutton, deletebutton, updatebutton, backbutton;
    @FXML
    private TableColumn<Genre, String> genreidcolumn;
    @FXML
    private TableColumn<Genre, String> genrenamescolumn;
    @FXML
    private TextField genrenametextfield;
    @FXML
    private TableView<Genre> genretable;
    @FXML
    private Button logoutbutton, moviebtn, tvbtn, usersbtn;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        initializeCol();
        displayGenre();
    }

    private void initializeCol(){
        genreidcolumn.setCellValueFactory(new PropertyValueFactory<>("genreid"));
        genrenamescolumn.setCellValueFactory(new PropertyValueFactory<>("genrenames"));
    }

    private void displayGenre(){
        genreList.clear();
        ResultSet result = DatabaseHandler.getGenres();

        if (result == null) {
            System.err.println("Error: ResultSet is null. Check database connection.");
            return;
        }
        try {
            while (result.next()) {
                String genreid = result.getString("GenreID");
                String genrename = result.getString("GenreName");

                genreList.add(new Genre(genreid, genrename));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        genretable.setItems(genreList);
    }

    @FXML
    public void addGenre(ActionEvent event) {
        String genrename = genrenametextfield.getText().trim();

        if (genrename.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Please enter a genre name.");
            alert.showAndWait();
            return;
        }

        Genre genre = new Genre("", genrename);

        if(DatabaseHandler.addGenre(genre)){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Genre added");
            alert.showAndWait();
            displayGenre();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Adding of Genre Failed");
            alert.showAndWait();
        }
        displayGenre();
    }

    @FXML
    public void updateGenre(ActionEvent event) {
        Genre selectedGenre = genretable.getSelectionModel().getSelectedItem();
        String newGenreName = genrenametextfield.getText().trim();

        if (selectedGenre == null || newGenreName.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Please select a genre and enter a new genre name.");
            alert.showAndWait();
            return;
        }

        if (DatabaseHandler.updateGenre(selectedGenre, newGenreName)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Genre updated");
            alert.showAndWait();
            displayGenre();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Updating of Genre Failed");
            alert.showAndWait();
        }
    }

    @FXML
    public void deleteGenre(ActionEvent event) {
        Genre genre = genretable.getSelectionModel().getSelectedItem();

        if (genre == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Please select a genre to delete.");
            alert.showAndWait();
            return;
        }

        if (DatabaseHandler.deleteGenre(genre)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Genre deleted");
            alert.showAndWait();
            displayGenre();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Cannot delete genre. It is being used by a movie.");
            alert.showAndWait();
        }
    }
    @FXML 
    public void backbuttonmovie(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/admin/movieandshow/movie/Movie.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Pinoy Flix");
        stage.setScene(scene);
        stage.show();
    }

    @FXML 
    public void backbuttontvshow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/admin/movieandshow/tvshow/Tvshow.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Pinoy Flix");
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
    private void moviebuttonHandler(ActionEvent event) throws IOException {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/movieandshow/movie/Movie.fxml"));
       Parent parent = loader.load();
       Scene scene = new Scene(parent);
       Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       stage.setTitle("Pinoy Flix");
       stage.setScene(scene);
       stage.show();
   }

   @FXML
    private void tvshowbuttonHandler(ActionEvent event) throws IOException {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/movieandshow/tvshow/Tvshow.fxml"));
       Parent parent = loader.load();
       Scene scene = new Scene(parent);
       Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       stage.setTitle("Pinoy Flix");
       stage.setScene(scene);
       stage.show();
   }




}


