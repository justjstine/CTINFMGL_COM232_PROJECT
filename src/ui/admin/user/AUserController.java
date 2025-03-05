package ui.admin.user;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.jfoenix.controls.JFXButton;
import data.User;
import database.DatabaseHandler;

public class AUserController implements Initializable{

    ObservableList<User> userlist = FXCollections.observableArrayList();

    @FXML
    private TableColumn<User, String> accountcreatedcolumn, emailcolumn, firstnamecolumn, lastnamecolumn, passwordcolumn, paymentmethodcolumn, plantypecolumn, usernamecolumn;
    @FXML
    private JFXButton createbutton, deletebutton, updatebutton, paymentbutton, plantypebutton;
    @FXML
    private TextField emailtextfield, firstnametextfield, lastnametextfield, nametextfield, passwordtextfield ;
    @FXML
    private ComboBox<String> plantypecombo, paymentmethodcombo;
    @FXML
    private Button moviebtn, logoutbutton, tvButton, usersbtn;
    @FXML
    private TableView<User> mytable;
    @FXML
    private Label welcomelabel;



    public void displayName(String fname){
        welcomelabel.setText("Welcome back, " + fname);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        initializeCol();
        displayUsers();
        loadPaymentMethods();
        loadSubscriptionTypes();
    }

    private void initializeCol(){
        usernamecolumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordcolumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        firstnamecolumn.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastnamecolumn.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        emailcolumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
        accountcreatedcolumn.setCellValueFactory(new PropertyValueFactory<>("created"));
        plantypecolumn.setCellValueFactory(new PropertyValueFactory<>("planType"));
        paymentmethodcolumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
    }

    private void displayUsers(){
        userlist.clear();
        ResultSet result = DatabaseHandler.getUsers();
        
        if (result == null) {
            System.err.println("Error: ResultSet is null. Check database connection.");
            return;
        }
        try {
            while (result.next()) {
                int userID = result.getInt("UserID");
                String username = result.getString("Username");
                String password = result.getString("Password");
                String firstname = result.getString("FirstName");
                String lastname = result.getString("LastName");
                String email = result.getString("Email");
                String created = result.getString("Created");
                String planType = result.getString("PlanType");
                String paymentMethod = result.getString("PaymentMethod");
    
                userlist.add(new User(userID, username, password, firstname, lastname, email, created, planType, paymentMethod, ""));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        mytable.setItems(userlist);
    }

    @FXML
    private void createUser(ActionEvent event) {
    String username = nametextfield.getText().trim();
    String password = passwordtextfield.getText().trim();
    String firstname = firstnametextfield.getText().trim();
    String lastname = lastnametextfield.getText().trim();
    String email = emailtextfield.getText().trim();
    String planType = plantypecombo.getValue() != null ? plantypecombo.getValue().trim() : "";
    String paymentMethod = paymentmethodcombo.getValue() != null ? paymentmethodcombo.getValue().trim() : "";

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

    // Validate input fields
    for (String[] field : fields) {
        if (field[0].isEmpty()) {
            showAlert(AlertType.ERROR, field[1]);
            return;
        }
    }

    User user = new User(0,username, password, firstname, lastname, email, "", planType, paymentMethod, "");

    if (DatabaseHandler.addUser(user)) {
        showAlert(AlertType.INFORMATION, "Account Created");
        displayUsers(); // Ensure the table is updated
    } else {
        showAlert(AlertType.ERROR, "Account creation failed");
    }
}
    
    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
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

    @FXML
    private void deleteUser(ActionEvent event) {

       User user = mytable.getSelectionModel().getSelectedItem();

       if (user == null) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("Please select a user to delete.");
        alert.showAndWait();
        return;
    }

    if (DatabaseHandler.deleteUser(user)) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Account Deleted");
        alert.showAndWait();
        displayUsers();
    } else {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("Account deletion failed");
        alert.showAndWait();
    }
}

@FXML
private void updateUser(ActionEvent event) {
    User selectedUser = mytable.getSelectionModel().getSelectedItem();
    if (selectedUser == null) {
        showAlert(AlertType.ERROR, "Please select a user to update.");
        return;
    }

    String username = nametextfield.getText().trim();
    String password = passwordtextfield.getText().trim();  
    String firstname = firstnametextfield.getText().trim();
    String lastname = lastnametextfield.getText().trim();  
    String email = emailtextfield.getText().trim();
    String planType = plantypecombo.getValue();
    String paymentMethod = paymentmethodcombo.getValue();
    
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
    
    // Validate input fields
    for (String[] field : fields) {
        if (field[0].isEmpty()) {
            showAlert(AlertType.ERROR, field[1]);
            return;
        }
    }
    
    User user = new User(selectedUser.getUserID(), username, password, firstname, lastname, email, "" , planType, paymentMethod, "");
    
    if (DatabaseHandler.updateUser(user)) {
        showAlert(AlertType.INFORMATION, "Account Updated");
    } else {
        showAlert(AlertType.ERROR, "Can't update account");
        return;
    }
    
    displayUsers();
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
    public void tvbuttonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/movieandshow/tvshow/Tvshow.fxml"));
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
    private void paymentMethodButtonHandler(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/user/payment/Payment.fxml"));
    Parent parent = loader.load();
    Scene scene = new Scene(parent);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setTitle("Pinoy Flix");
    stage.setScene(scene);
    stage.show();
}

   @FXML
   private void plantypebuttonHandler(ActionEvent event) throws IOException {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/user/subscription/Subscription.fxml"));
       Parent parent = loader.load();
       Scene scene = new Scene(parent);
       Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       stage.setTitle("Pinoy Flix");
       stage.setScene(scene);
       stage.show();
   }

   @FXML
   private void transactionbuttonHandler(ActionEvent event) throws IOException {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/user/transaction/Transaction2.fxml"));
       Parent parent = loader.load();
       Scene scene = new Scene(parent);
       Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       stage.setTitle("Pinoy Flix");
       stage.setScene(scene);
       stage.show();
}
    
}

