package seller.group.azon;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seller.group.azon.backend.ControlStage;
import seller.group.azon.controller.ErrorController;
import java.io.IOException;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        // get loader
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login-view.fxml"));
            Parent root = fxmlLoader.load();

            // configure scene
            Scene scene = new Scene(root);
            stage.setTitle("Azon management");
            stage.setMinHeight(500.0);
            stage.setMinWidth(600.0);

            stage.setResizable(true);

            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e){
            ControlStage.changeStageByStage(getClass(), stage, "/fxml/error-view.fxml");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

