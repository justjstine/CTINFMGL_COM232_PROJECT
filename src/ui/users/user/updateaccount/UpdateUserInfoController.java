package ui.users.user.updateaccount;

import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;
import data.User;
import database.DatabaseHandler;


public class UpdateUserInfoController {


    @FXML
    private JFXButton backButton;


    @FXML
    private TextField emailtextfield;


    @FXML
    private TextField usernametextfield;


    @FXML
    private TextField firstnametextfield;


    @FXML
    private TextField lastnametextfield;


    @FXML
    private PasswordField passwordtextfield;


    @FXML
    private ComboBox<String> paymentmethodcombo;


    @FXML
    private ComboBox<String> plantypecombo;


    @FXML
    private JFXButton updateuseraccountbtn;


    @FXML
    private void initialize() {
    loadPaymentMethods();
    loadSubscriptionTypes();
}  

    @FXML
    private void backButtonHandler(ActionEvent event) throws IOException {
       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(getClass().getResource("/ui/users/user/movies/MovieUUser.fxml"));
       Parent parent = loader.load();
       Scene scene = new Scene(parent);
       Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       stage.setTitle("Pinoy Flix");
       stage.setScene(scene);
       stage.show();
    }


    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void updateUUser(ActionEvent event) {
    String username = usernametextfield.getText() != null ? usernametextfield.getText().trim() : "";
    String password = passwordtextfield.getText() != null ? passwordtextfield.getText().trim() : "";  
    String firstname = firstnametextfield.getText() != null ? firstnametextfield.getText().trim() : "";
    String lastname = lastnametextfield.getText() != null ? lastnametextfield.getText().trim() : "";  
    String email = emailtextfield.getText() != null ? emailtextfield.getText().trim() : "";
    String planType = plantypecombo.getValue() != null ? plantypecombo.getValue() : "";
    String paymentMethod = paymentmethodcombo.getValue() != null ? paymentmethodcombo.getValue() : "";
    
    if (!email.matches("^[^@]+@[^@]+\\.[^@]{2,}$")) {
        showAlert(AlertType.ERROR, "Invalid email format.");
        return;
    }

    if (firstname.matches(".*\\d.*") || lastname.matches(".*\\d.*")) {
        showAlert(AlertType.ERROR, "First name or last name contains invalid characters.");
        return;
    }
   
    // Array of input values and their corresponding error messages
    String[][] fields = {
        {username, "Empty username"},
        {password, "Empty password"},
        {firstname, "Empty first name"},
        {lastname, "Empty last name"},
        {email, "Empty email"},
        {planType, "Empty plan type"},
        {paymentMethod, "Empty payment method"}
    };
   
    for (String[] field : fields) {
        if (field[0].isEmpty()) {
            showAlert(AlertType.ERROR, field[1]);
            return;
        }
    }
        User user = new User(0,username, password, firstname, lastname, email, "" , planType, paymentMethod, "");
       
        if (DatabaseHandler.updateUUser(user)) {
            showAlert(AlertType.INFORMATION, "Account Updated");
        } else {
            showAlert(AlertType.ERROR, "Can't update account. Please ensure username isn't altered");
            return;
        }
    }

    private void loadPaymentMethods() {
        ObservableList<String> paymentMethods = FXCollections.observableArrayList();
        List<String> methods = DatabaseHandler.getPaymentMethods();
        paymentMethods.addAll(methods);
        paymentmethodcombo.setItems(paymentMethods);
    }
   
        private void loadSubscriptionTypes() {
        ObservableList<String> subscriptionTypes = FXCollections.observableArrayList();
        List<String> types = DatabaseHandler.getSubscriptionTypes();
        subscriptionTypes.addAll(types);
        plantypecombo.setItems(subscriptionTypes);
    }
}




