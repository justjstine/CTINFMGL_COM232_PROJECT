package ui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utility.Utility;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/ui/login/Login.fxml"));
        primaryStage.setTitle("Pinoy Flix");
        Utility.setStageIcon(primaryStage);
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.resizableProperty().setValue(false);
        primaryStage.show();

        Utility.setStageIcon(primaryStage);
    }

    public static void main(String[] args) {
        System.setProperty("javafx.runtime.addExports", "javafx.base/com.sun.javafx.runtime=ALL-UNNAMED");
        launch(args);
    }
}