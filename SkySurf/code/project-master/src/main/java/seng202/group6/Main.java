package seng202.group6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seng202.group6.controller.MainWindowController;
import seng202.group6.persistence.PersistenceManager;

import java.io.IOException;

/**
 * The main class, start everything up and runs it
 */
public class Main extends Application {

    /**
     * Loads and starts the GUI main window
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        DataContainer dataContainer = new DataContainer();
        PersistenceManager persistenceManager = new PersistenceManager(dataContainer);
        Ranker ranker = new Ranker(dataContainer.getActiveAirportList(),
                dataContainer.getActiveRouteList());
        dataContainer.addObserver(ranker);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
        Parent root = loader.load(); // throws IOException

        MainWindowController mainWindowController = loader.getController();
        mainWindowController.setUp(dataContainer, persistenceManager);

        primaryStage.setOnCloseRequest(event -> {
            mainWindowController.closeProgram();
            event.consume();
        });
        primaryStage.setTitle("SkySurf");
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
        primaryStage.setScene(new Scene(root, 1280, 750));
        primaryStage.setMinHeight(750);
        primaryStage.setMinWidth(1280);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}