package visualiser.gameController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Start to manually test the game controller
 */
public class GameControllerManualTest extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        GridPane root = new GridPane();
        Scene scene = new Scene(root, 1200, 800);

        InputChecker inputChecker = new InputChecker();
        inputChecker.runWithScene(scene);

        stage.setScene(scene);
        stage.setTitle("RaceVision - Team 7 - Input Tester Manual Test");
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
