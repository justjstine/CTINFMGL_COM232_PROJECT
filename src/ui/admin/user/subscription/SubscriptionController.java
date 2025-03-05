package ui.admin.user.subscription;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import data.Subscription;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SubscriptionController implements Initializable{
    ObservableList<Subscription> subscriptionList = FXCollections.observableArrayList();

    @FXML
    private JFXButton addpaymentbutton, backbutton, deletebutton, updatebutton;
    @FXML
    private Button logoutbutton, moviebtn, tvbtn, usersbtn;
    @FXML
    private TextField plantypetextfield, pricetextfield;
    @FXML
    private TableColumn<Subscription, String> plantypecolumn, pricecolumn, subscriptionidcolumn;
    @FXML
    private TableView<Subscription> subscriptiontable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeCol();
        displaySubscription();
    }

    private void initializeCol() {
        subscriptionidcolumn.setCellValueFactory(new PropertyValueFactory<>("subscriptionID"));
        plantypecolumn.setCellValueFactory(new PropertyValueFactory<>("subscriptionType"));
        pricecolumn.setCellValueFactory(new PropertyValueFactory<>("subscriptionPrice"));
    }

    private void displaySubscription(){
        subscriptionList.clear();
        ResultSet result = DatabaseHandler.getSubscription();
    
        if (result == null) {
            System.err.println("Error: ResultSet is null. Check database connection.");
            return;
        }
    
        try {
            while (result.next()) {
                String subscriptionID = result.getString("subscriptionID");
                String subscriptionType = result.getString("PlanType"); // Updated column name
                String subscriptionPrice = result.getString("Price");
    
                subscriptionList.add(new Subscription(subscriptionID, subscriptionType, subscriptionPrice));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        subscriptiontable.setItems(subscriptionList);
    }

    @FXML
    public void addSubscription() {
        String subscriptionType = plantypetextfield.getText().trim();
        String subscriptionPrice = pricetextfield.getText().trim();

        if (subscriptionType.isEmpty() || subscriptionPrice.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter all fields");
            alert.showAndWait();
            return;
        }

        Subscription subscription = new Subscription("", subscriptionType, subscriptionPrice);

        if (DatabaseHandler.addSubscription(subscription)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Subscription Added");
            alert.showAndWait();
            displaySubscription();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Operation Failed");
            alert.showAndWait();
        }
        displaySubscription();
    }

    @FXML 
    public void deleteSubscription() {
        Subscription subscription = subscriptiontable.getSelectionModel().getSelectedItem();

        if (subscription == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a subscription to delete.");
            alert.showAndWait();
            return;
        }

        if (DatabaseHandler.deleteSubscription(subscription)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Subscription Deleted");
            alert.showAndWait();
            displaySubscription();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Operation Failed");
            alert.showAndWait();
        }
    }

    @FXML
public void updateSubscription() {
    Subscription subscription = subscriptiontable.getSelectionModel().getSelectedItem();

    if (subscription == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Please select a subscription to update.");
        alert.showAndWait();
        return;
    }

    String newSubscriptionType = plantypetextfield.getText().trim();
    String newPrice = pricetextfield.getText().trim();

    if (newSubscriptionType.isEmpty() || newPrice.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Please enter all fields");
        alert.showAndWait();
        return;
    }

    if (DatabaseHandler.updateSubscription(subscription, newSubscriptionType, newPrice)) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Subscription Updated");
        alert.showAndWait();
        displaySubscription();
    } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Operation Failed");
        alert.showAndWait();
    }
}
@FXML
    public void moviebuttonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/movieandshow/movie/Movie.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Pinoy Flix");
        stage.setScene(scene);
        stage.show();
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
   private void backbuttonHandler(ActionEvent event) throws IOException {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/user/AUser.fxml"));
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

}
