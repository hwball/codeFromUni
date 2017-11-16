package visualiser.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import visualiser.app.App;
import java.io.IOException;

/**
 * Abstract controller class to give each subclass the functionality to load
 * a new scene into the existing stage, or create a new popup window.
 */
public abstract class Controller {
    private Stage stage = App.getStage();

    /**
     * Loads the title screen again when app is already running.
     * @throws IOException if a problem with the title.fxml
     */
    protected void loadTitleScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/visualiser/scenes/title.fxml"));
        Parent root = loader.load();
        stage.setResizable(false);
        Scene scene = new Scene(root);
        addCssStyle(scene);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Used to load a new scene in the currently open stage.
     * @param fxmlUrl the URL of the FXML file to be loaded
     * @return the controller of the new scene
     * @throws IOException if there is an issue with the fxmlUrl
     */
    protected Controller loadScene(String fxmlUrl) throws IOException {
        // load the correct fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource
                ("/visualiser/scenes/"+fxmlUrl));
        Parent root = loader.load();

        // reuse previous stage and it's window size
        Stage stage = App.getStage();
        Double stageHeight = stage.getHeight();
        Double stageWidth = stage.getWidth();

        // set new scene into existing window
        Scene scene = new Scene(root, stageWidth, stageHeight);
        addCssStyle(scene);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
        stage.setHeight(stageHeight);
        stage.setWidth(stageWidth);
        stage.sizeToScene();

        // return controller for the loaded fxml scene
        return loader.getController();
    }

    /**
     * Used to load a scene in a new separate popup stage.
     * @param fxmlUrl the URL of the FXML file to be loaded
     * @param title title for the new window
     * @param modality modality settings for popup window
     * @return the controller of the new scene
     * @throws IOException if there is an issue with the fxmlUrl
     */
    protected Controller loadPopupScene(String fxmlUrl, String title, Modality
            modality) throws IOException {
        // load the correct fxml scene
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(
                "/visualiser/scenes/" + fxmlUrl));
        Parent root = loader.load();

        // create a new 'pop-up' window
        Stage stage = new Stage();
        stage.initModality(modality);
        stage.setTitle(title);
        stage.centerOnScreen();
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/SailIcon.png")));
        Scene scene = new Scene(root);
        addCssStyle(scene);
        stage.setScene(scene);
        stage.show();

        // return controller for the loaded fxml scene
        return loader.getController();
    }

    /**
     * Adds the relevant CSS styling to the scene being loaded.
     * @param scene new scene to be loaded and displayed
     */
    private void addCssStyle(Scene scene){
        if (App.dayMode) {
            scene.getStylesheets().add("/css/dayMode.css");
        } else {
            scene.getStylesheets().add("/css/nightMode.css");
        }
    }

}
