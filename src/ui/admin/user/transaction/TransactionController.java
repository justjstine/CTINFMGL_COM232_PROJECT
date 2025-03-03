package ui.admin.user.transaction;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import data.Subscription;
import data.User;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TransactionController implements Initializable {

    ObservableList<User> userlist = FXCollections.observableArrayList();
    ObservableList<Subscription> subscriptionList = FXCollections.observableArrayList();
    @FXML
    private TableColumn<User, String> accountcreatedcolumn;

    @FXML
    private TableColumn<User, String> emailcolumn;

    @FXML
    private TableColumn<User, String> firstnamecolumn;

    @FXML
    private TableColumn<User, String> lastnamecolumn;

    @FXML 
    private TableColumn<User, String> transactioncolumn;

    @FXML
    private Button logoutbutton;

    @FXML
    private Button moviebtn;

    @FXML
    private TableView<User> mytable;

    @FXML
    private TableColumn<User, String> paymentmethodcolumn;

    @FXML
    private TableColumn<User, String> plantypecolumn;

    @FXML
    private Button tvbtn;

    @FXML
    private TableColumn<Subscription, String> amountcolumn;

    @FXML
    private Button usersbtn;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        initializeCol();
        displayUsers();
    }

     private void initializeCol(){
        transactioncolumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        amountcolumn.setCellValueFactory(new PropertyValueFactory<>("subscriptionAmount"));
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
                Integer userid = result.getInt("UserID");
                String subscriptionAmount = result.getString("Price");
                String firstname = result.getString("FirstName");
                String lastname = result.getString("LastName");
                String email = result.getString("Email");
                String created = result.getString("Created");
                String planType = result.getString("PlanType");
                String paymentMethod = result.getString("PaymentMethod");

                userlist.add(new User(userid, "", "", firstname, lastname, email, created, planType, paymentMethod, subscriptionAmount));
            


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        mytable.setItems(userlist);
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
   private void usersBtnHandler(ActionEvent event) throws IOException {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/admin/user/AUser.fxml"));
      Parent parent = loader.load();
      Scene scene = new Scene(parent);
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.setTitle("Pinoy Flix");
      stage.setScene(scene);
      stage.show();
  }

}
