package app;


import javafx.application.Application;
import javafx.stage.Stage;
import mock.app.Event;

import java.util.logging.Level;
import java.util.logging.Logger;


public class App extends Application {


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            //TODO should read a configuration file to configure server?
            Event raceEvent = new Event(false, 0, 5);


        } catch (Exception e) {
            //Catch all exceptions, print, and exit.
            Logger.getGlobal().log(Level.SEVERE, "Could not start dedicated server!", e);
            System.exit(1);
        }
    }



}
