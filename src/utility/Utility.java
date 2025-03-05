package utility;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Utility {
    public static final String ICON_IMAGE_LOC = "/resources/Logo.png";

     // Add icon
     public static void setStageIcon(Stage stage) {
        try {
            stage.getIcons().add(new Image(Utility.class.getResourceAsStream(ICON_IMAGE_LOC)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     // Load new scenes/windows
     public static Object loadWindow(URL loc, String title, Stage parentStage) {
        Object controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(loc);
            Parent parent = loader.load();
            controller = loader.getController();
            Stage stage = null;
            if (parentStage != null) {
                stage = parentStage;
            } else {
                stage = new Stage(StageStyle.DECORATED);
            }
            stage.setTitle(title);

            // Adds an icon for each window opened
            setStageIcon(stage);

            stage.setScene(new Scene(parent));
            stage.show();
            //setStageIcon(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return controller;
    }
}