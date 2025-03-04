package ui.admin.user.payment;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import data.Payment;
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

public class PaymentController implements Initializable {

    ObservableList<Payment> paymentList = FXCollections.observableArrayList();

    @FXML
    private JFXButton addpaymentbutton, backbutton, deletebutton, updatebutton;
    @FXML
    private TableView<Payment> paymenttable;
    @FXML
    private Button logoutbutton, moviebtn, tvbtn, usersbtn;
    @FXML
    private TableColumn<Payment, String> paymentidcolumn, paymentmethodcolumn;
    @FXML
    private TextField paymenttextfield;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeCol();
        displayPayment();
    }
    private void initializeCol(){
        paymentidcolumn.setCellValueFactory(new PropertyValueFactory<>("paymentID"));
        paymentmethodcolumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
    }

    private void displayPayment(){
        paymentList.clear();
        ResultSet result = DatabaseHandler.getPayment();

        if (result == null) {
            System.err.println("Error: ResultSet is null. Check database connection.");
            return;
        }
        try {
            while(result.next()){
                String paymentID = result.getString("paymentID");
                String paymentMethod = result.getString("paymentMethod");
                paymentList.add(new Payment(paymentID, paymentMethod));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        paymenttable.setItems(paymentList);
    }

@FXML
public void addPayment(ActionEvent event) {
    String paymentMethod = paymenttextfield.getText().trim();
    if (paymentMethod.isEmpty()) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("Please enter a payment method.");
        alert.showAndWait();
        return;
    }
    Payment payment = new Payment("", paymentMethod); // Assuming paymentID is auto-generated
    
    if (DatabaseHandler.addPayment(payment)) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Payment method added successfully.");
        alert.showAndWait();
    } else {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("Failed to add payment method.");
        alert.showAndWait();
    }
    displayPayment();

}
@FXML
public void deletePayment(ActionEvent event) {
    Payment payment = paymenttable.getSelectionModel().getSelectedItem();

    if (payment== null) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("Please select a payment method to delete.");
        alert.showAndWait();
        return;
    }

    if (DatabaseHandler.deletePayment(payment)) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Payment method deleted successfully.");
        alert.showAndWait();
        displayPayment();
    } else {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Failed to delete payment method.");
        alert.showAndWait();
    }
}
@FXML
public void updatePayment(ActionEvent event) {
    Payment selectedPayment = paymenttable.getSelectionModel().getSelectedItem();
    String newPayment = paymenttextfield.getText().trim();
    
    if (selectedPayment == null || newPayment.isEmpty()) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("Please select a payment method to update.");
        alert.showAndWait();
        return;
    }
    
    if (DatabaseHandler.updatePayment(selectedPayment, newPayment)) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Payment method updated successfully.");
        alert.showAndWait();
        displayPayment();
    } else {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("Failed to update payment method.");
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




