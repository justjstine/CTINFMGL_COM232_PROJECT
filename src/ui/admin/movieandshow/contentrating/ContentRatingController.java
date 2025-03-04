package ui.admin.movieandshow.contentrating;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import data.ContentRating;
import database.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.scene.Node;

public class ContentRatingController implements Initializable {

    ObservableList<ContentRating> contentraitingList = FXCollections.observableArrayList();

    @FXML
    private JFXButton addclassificationbutton, backbuttonmovie, backbuttontvshow, deletebutton, updatebutton;
    @FXML
    private TableColumn<ContentRating, String> contentratingidcolumn, classificationcolumn;
    @FXML
    private TextField classificationtextfield;
    @FXML
    private TableView<ContentRating> contentratingtable;
    @FXML
    private Button moviebtn, tvbtn, usersbtn, logoutbtn;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        initializeCol();
        displayContentRating();
    }

    private void initializeCol(){
        contentratingidcolumn.setCellValueFactory(new  PropertyValueFactory<>("contentratingid"));
        classificationcolumn.setCellValueFactory(new  PropertyValueFactory<>("classification"));
    }

   private void displayContentRating(){
        contentraitingList.clear();
        ResultSet result = DatabaseHandler.getContentRating();
    
        if (result == null) {
            System.err.println("Error: ResultSet is null. Check database connection.");
            return;
        }
        try {
            while (result.next()) {
                String contentraitingid = result.getString("ContentRatingID");
                String classification = result.getString("Classification");

                contentraitingList.add(new ContentRating(contentraitingid, classification));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contentratingtable.setItems(contentraitingList);
    }


    @FXML
    public void addClassification(ActionEvent event) {
        String classification = classificationtextfield.getText().trim();

        if (classification.length() == 0){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Empty Classification");
            alert.showAndWait();
            return; 
        }
    
        ContentRating contentRating = new ContentRating("", classification);
    
        if(DatabaseHandler.addClassification(contentRating)){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Classification Added");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Adding of Classification Failed");
            alert.showAndWait();
        }
        displayContentRating();
    }
    

    @FXML
    public void deleteClassification(ActionEvent event) { 
        ContentRating contentRating = contentratingtable.getSelectionModel().getSelectedItem();

    if (contentRating  == null) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("Please select a classification to delete.");
        alert.showAndWait();
        return;
    }

    if (DatabaseHandler.deleteClassification(contentRating)) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Classification Deleted");
        alert.showAndWait();
        displayContentRating();
    } else {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("Cannot delete classification. It is being used by a movie.");
        alert.showAndWait();
    }
}

    @FXML
    public void updateClassification(ActionEvent event) { 
        ContentRating selectedClassification = contentratingtable.getSelectionModel().getSelectedItem();
        String newClassification = classificationtextfield.getText().trim();

    if (selectedClassification == null || newClassification.isEmpty()) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("Please select a classification and enter a new classification.");
        alert.showAndWait();
        return;
    }

    if (DatabaseHandler.updateClassification(selectedClassification, newClassification)) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Classification Updated");
        alert.showAndWait();
        displayContentRating();
    } else {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("Can't Update Classification");
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
