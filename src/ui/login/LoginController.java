package ui.login;

import javafx.event.ActionEvent;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import database.DatabaseHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ui.admin.user.AUserController;
import ui.users.user.movies.UUserController;
import ui.users.user.tvshow.TVUUserController;

public class LoginController {

    @FXML
    Label usernameLabel, passwordLabel;
    @FXML
    TextField usernametextfield, passwordtextfield;
    @FXML
    JFXButton loginbutton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void loginbuttonHandler(ActionEvent event) throws IOException {
        String uname = usernametextfield.getText();
        String pword = passwordtextfield.getText();
        FXMLLoader loader;

        if ("admin".equals(uname) && "admin".equals(pword)) {
            loader = new FXMLLoader(getClass().getResource("/ui/admin/user/AUser.fxml"));
            root = loader.load();
            AUserController aUserController = loader.getController();

            aUserController.displayName(uname);

        } else if (DatabaseHandler.validateLogin(uname, pword)) {
            loader = new FXMLLoader(getClass().getResource("/ui/users/user/movies/MovieUUser.fxml"));
            root = loader.load();
            UUserController uUserController = loader.getController();
            uUserController.displayName(uname);

            loader = new FXMLLoader(getClass().getResource("/ui/users/user/tvshow/TVUUser.fxml"));
            root = loader.load();
            TVUUserController TVuUserController = loader.getController();
            TVuUserController.displayName(uname);

        } else {    
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password. Please try again.");
            alert.showAndWait();
            return;
        }

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void signupbuttonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/signup/Signup.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}