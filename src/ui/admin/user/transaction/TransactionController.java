package ui.admin.user.transaction;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import data.Transaction;
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
    @FXML
    private TableColumn<Transaction, String> transactionIDColumn, userIDColumn, paymentMethodColumn, dateColumn, subscriptionIdcolumn;
    @FXML
    private TableColumn<Transaction, Integer> amountColumn; 
    @FXML
    private Button logoutbutton, moviebtn, tvbtn, usersbtn;
    @FXML
    private TableView<Transaction> transactionTable;

    private final ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeColumns();
        displayTransactions();
    }

    private void initializeColumns() {
        transactionIDColumn.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        subscriptionIdcolumn.setCellValueFactory(new PropertyValueFactory<>("planType"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
    }

    private void displayTransactions() {
        transactionList.clear();
        ResultSet rs = DatabaseHandler.getTransactions();
        
        if (rs == null) {
            System.err.println("Error: No transactions retrieved.");
            return;
        }
    
        try {
            while (rs.next()) {
                transactionList.add(new Transaction(
                    rs.getString("TransactionID"),
                    rs.getString("UserID") != null ? rs.getString("UserID") : "N/A",
                    rs.getString("Subscription"),  // FIX: Use correct alias
                    rs.getString("PaymentMethod"),       
                    rs.getInt("Price"), 
                    rs.getString("TransactionDate")
                ));
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        transactionTable.setItems(transactionList);
        
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
