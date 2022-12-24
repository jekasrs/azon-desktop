package seller.group.azon.backend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seller.group.azon.controller.Controller;

import java.io.IOException;

public class ControlStage {

    public static Controller changeStage(Class<?> parentClass, Parent root, String src) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(parentClass.getResource(src));
        Parent predecessor = fxmlLoader.load();
        Stage stage = (Stage) root.getScene().getWindow();

        // take instance of controller
        Controller controller = fxmlLoader.getController();

        // set new scene
        Scene scene = new Scene(predecessor);
        stage.setScene(scene);
        stage.show();
        return controller;
    }

    public static Controller changeStageByStage(Class<?> parentClass, Stage stage, String src) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(parentClass.getResource(src));
        Parent predecessor = fxmlLoader.load();

        // take instance of controller
        Controller controller = fxmlLoader.getController();

        // set new scene
        Scene scene = new Scene(predecessor);
        stage.setScene(scene);
        stage.show();
        return controller;
    }

}
