package alerts;
import javafx.event.ActionEvent;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;


public class LogoutController {

    @FXML
    private void logoutUser(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Alerts/LogoutPane.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    private void proceedLogout(ActionEvent event) throws IOException {
        for (Stage stage : Stage.getWindows().stream().filter(Window::isShowing).map(Stage.class::cast).toList()) {
            stage.close();
            } 

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login/Login.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage loginStage = new Stage();
        loginStage.setTitle("Pinoy Flix");
        loginStage.setScene(scene);
        loginStage.show();

        
    }
}

