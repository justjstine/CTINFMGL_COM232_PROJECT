package ui.signup;

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

public class SignupController {

    @FXML
    private JFXButton createaccountbutton, backButton;
    @FXML
    private TextField emailtextfield, firstnametextfield, lastnametextfield, usernametextfield;
    @FXML
    private PasswordField passwordtextfield;
    @FXML
    private ComboBox<String> plantypecombo, paymentmethodcombo;


    @FXML
    private void initialize() {
    loadPaymentMethods();
    loadSubscriptionTypes();
}
  

    @FXML
    private void addAccount(ActionEvent event) throws IOException {
    String username = usernametextfield.getText().trim();
    String password = passwordtextfield.getText().trim();
    String firstname = firstnametextfield.getText().trim();
    String lastname = lastnametextfield.getText().trim();
    String email = emailtextfield.getText().trim();
    String planType = plantypecombo.getValue();
    String paymentMethod = paymentmethodcombo.getValue();

    if (username.isEmpty() || password.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || planType.isEmpty() || paymentMethod.isEmpty()) {
        showAlert(AlertType.ERROR, "All fields are required.");
        return;
    }

    // Validate email format using regex
    if (!email.matches("^[^@]+@[^@]+\\.[^@]{2,}$")) {
        showAlert(AlertType.ERROR, "Invalid email format.");
        return;
    }

    // Validate first name and last name (no numbers allowed)
    if (firstname.matches(".*\\d.*") || lastname.matches(".*\\d.*")) {
        showAlert(AlertType.ERROR, "First name or last name contains invalid characters.");
        return;
    }

    User user = new User(0, username, password, firstname, lastname, email, "", planType, paymentMethod, "");

    if (DatabaseHandler.addUser(user)) {
        showAlert(AlertType.INFORMATION, "Account Created");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } else {
        showAlert(AlertType.ERROR, "Account creation failed");
    }
}
    private void showAlert(AlertType alertType, String message) {
    Alert alert = new Alert(alertType);
    alert.setContentText(message);
    alert.showAndWait();
}
  
    @FXML
    private void backButtonHandler(ActionEvent event) throws IOException {
       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(getClass().getResource("/ui/login/Login.fxml"));
       Parent parent = loader.load();
       Scene scene = new Scene(parent);
       Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       stage.setTitle("Pinoy Flix");
       stage.setScene(scene);
       stage.show();
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