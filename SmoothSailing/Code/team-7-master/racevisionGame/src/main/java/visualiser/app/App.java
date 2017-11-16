package visualiser.app;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import mock.app.Event;
import visualiser.layout.Assets3D;
import visualiser.model.SoundAssets;

public class App extends Application {
    private static Stage stage;
    public static Event game;
    public static Boolean dayMode = true;
    public static Integer gameType = 0;
    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private static final int SPLASH_WIDTH = 676;
    private static final int SPLASH_HEIGHT = 227;

    /**
     * Entry point for running the programme
     * @param args for starting the programme
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        ImageView splash = new ImageView(new Image(
                getClass().getClassLoader().getResourceAsStream("images/splashScreen.gif")
        ));
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
        progressText = new Label("Preparing application . . .");
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        splashLayout.setStyle(
                "-fx-padding: 5; " +
                        "-fx-background-color: cornsilk; " +
                        "-fx-border-width:5; " +
                        "-fx-border-color: " +
                        "linear-gradient(" +
                        "to bottom, " +
                        "chocolate, " +
                        "derive(chocolate, 50%)" +
                        ");"
        );
        splashLayout.setEffect(new DropShadow());
    }

    /**
     * Method that sets up and displays the splash screen
     * @param stage the initial stage
     * @throws Exception if something wrong with title screen.
     */
    public void start(Stage stage) throws Exception {
        final Task<ObservableList<String>> boatTask = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws InterruptedException {
                ObservableList<String> addedFilling =
                        FXCollections.observableArrayList();
                ObservableList<String> burgerFilling =
                        FXCollections.observableArrayList(
                                "Buns", "Patties", "Lettuce", "Onions", "Tomato",
                                "Sauces"
                        );

                updateMessage("Preparing ingredients . . .");
                Thread.sleep(100);
                for (int i = 0; i < burgerFilling.size(); i++) {
                    Thread.sleep(100);
                    updateProgress(i + 1, burgerFilling.size());
                    String nextFilling = burgerFilling.get(i);
                    addedFilling.add(nextFilling);
                    updateMessage("Adding the " + nextFilling + " . . .");
                    if (i == 0){
                        Assets3D.loadAssets();
                    } else if (i == 1){
                        SoundAssets.loadAssets();
                    }
                }
                Thread.sleep(100);
                updateMessage("Burger's done!");
                return addedFilling;
            }
        };
        showSplash(
                stage,
                boatTask,
                () -> {
                    try {
                        loadTitleScreen();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
        new Thread(boatTask).start();
    }

    /**
     * Get the main stage to be shared for all regular game play scenes.
     * @return shared stage
     */
    public static Stage getStage() {
        return App.stage;
    }

    /**
     * Loads the title screen for the first time on app start.
     * @throws Exception if there is a problem with a resource loaded
     */
    private void loadTitleScreen() throws Exception {
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource
                ("/visualiser/scenes/title.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Smooth Sailing - Burgers & Boats");
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/SailIcon.png")));
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Show the splash screen
     * @param initStage Initial stage
     * @param task Task for splash screen
     * @param initCompletionHandler initCompletionHandler interface
     */
    private void showSplash(
            final Stage initStage,
            Task<?> task,
            InitCompletionHandler initCompletionHandler
    ) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            }
        });

        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.setAlwaysOnTop(true);
        initStage.show();
    }

    /**
     * InnitCompletionHandler interface
     */
    public interface InitCompletionHandler {
        void complete();
    }
}
