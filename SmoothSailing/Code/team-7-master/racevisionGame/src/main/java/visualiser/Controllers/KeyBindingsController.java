package visualiser.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import visualiser.gameController.Keys.ControlKey;
import visualiser.gameController.Keys.KeyFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for the scene used to display and update current key bindings.
 */
public class KeyBindingsController extends Controller {
    private @FXML Button btnSave;
    private @FXML Button btnCancel;
    private @FXML Button btnReset;
    private @FXML ListView lstControl;
    private @FXML ListView lstKey;
    private @FXML ListView lstDescription;
    private @FXML AnchorPane anchor;
    private KeyFactory existingKeyFactory;
    private KeyFactory newKeyFactory;
    private Boolean changed = false;    // keyBindings have been modified
    private Button currentButton = null;    // last button clicked

    public void initialize(){
        // create new key factory to modify, keeping the existing one safe
        existingKeyFactory = new KeyFactory();
        existingKeyFactory.load();
        newKeyFactory = copyExistingFactory();
        initializeTable();
        populateTable();
        setKeyListener();
        setClosedListener();
    }

    /**
     * Sets up table before populating it.
     * Set up includes headings, CSS styling and modifying default properties.
     */
    private void initializeTable(){
        // set the headings for each column
        lstKey.getItems().add("Key");
        lstControl.getItems().add("Command");
        lstDescription.getItems().add("Description");
        lstKey.getSelectionModel().select(0);
        lstControl.getSelectionModel().select(0);
        lstDescription.getSelectionModel().select(0);

        // add CSS stylesheet once the scene has been created
        lstKey.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add("/css/keyBindings.css");
            }
        });

        // stop the columns from being selectable, so only the buttons are
        lstKey.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                        Platform.runLater(() ->
                        lstKey.getSelectionModel().select(0)));
        lstDescription.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                        Platform.runLater(() ->
                        lstDescription.getSelectionModel().select(0)));
        lstControl.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                        Platform.runLater(() ->
                        lstControl.getSelectionModel().select(0)));
    }

    /**
     * Populates the table with commands and their key binding details.
     */
    private void populateTable(){
        // add each command to the table
        for (Map.Entry<String, ControlKey> entry : newKeyFactory.getKeyState().entrySet()) {
            // create button for command
            Button button = new Button(entry.getKey());
            button.setMinWidth(120);
            button.setId(entry.getValue().toString());
            button.setOnAction(e -> currentButton = button);
            // display details for command in table
            lstControl.getItems().add(entry.getValue());
            lstKey.getItems().add(button);
            lstDescription.getItems().add(entry.getValue().getProtocolCode());
        }
    }

    /**
     * Makes a copy of the {@link KeyFactory} that does not modify the original.
     * @return new keyFactory to be modified
     */
    private KeyFactory copyExistingFactory(){
        newKeyFactory = new KeyFactory();
        Map<String, ControlKey> oldKeyState = existingKeyFactory.getKeyState();
        Map<String, ControlKey> newKeyState = new HashMap<>();

        // copy over commands and their keys
        for (Map.Entry<String, ControlKey> entry : oldKeyState.entrySet()){
            newKeyState.put(entry.getKey(), entry.getValue());
        }
        newKeyFactory.setKeyState(newKeyState);
        return newKeyFactory;
    }

    /**
     * Creates a listener for when a user tries to close the current window.
     */
    private void setClosedListener(){
        anchor.sceneProperty().addListener((obsS, oldS, newS) -> {
            if (newS != null) {
                newS.windowProperty().addListener((obsW, oldW, newW) -> {
                    if (newW != null) {
                        Stage stage = (Stage)newW;
                        // WE is processed by onExit method
                        stage.setOnCloseRequest(we -> {
                            if (we.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
                                onExit(we);
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * Creates a listener for the base anchorPane for key presses.
     * It updates the current key bindings of the {@link KeyFactory} if
     * required.
     */
    private void setKeyListener(){
        anchor.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            // if esc, cancel current button click
            if (event.getCode() == KeyCode.ESCAPE){
                btnCancel.requestFocus();
                currentButton = null;
            }
            // if a button was clicked
            else if (currentButton != null) {
                // check if a button is already mapped to this key
                for (int i = 1; i < lstKey.getItems().size(); i++) {
                    Button button = (Button)lstKey.getItems().get(i);
                    // update buttons text and remove key binding from command
                    if (button.getText().equals(event.getCode().toString())) {
                        button.setText("");
                        newKeyFactory.updateKey(button.getId(), button.getId());
                    }
                }
                // update text on the button
                currentButton.setText(event.getCode().toString());
                // update the control key
                newKeyFactory.updateKey(event.getCode().toString(),
                        currentButton.getId());
                // remove current button selection
                currentButton = null;
                changed = true;
                btnCancel.requestFocus();
            }
            event.consume();
        });
    }

    /**
     * Cancel and exits the key bindings menu. Changes are not forced to be
     * saved or fixed if invalid, and instead are defaulted back to the last
     * successful saved state.
     */
    public void cancel(){
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    /**
     * Resets all key bindings to the built-in defaults.
     */
    public void reset(){
        lstKey.getItems().clear();
        lstControl.getItems().clear();
        lstDescription.getItems().clear();
        newKeyFactory = new KeyFactory();
        initializeTable();
        populateTable();
        changed = true;
    }

    /**
     * Replace existing {@link KeyFactory} with the modified key bindings.
     */
    public void save(){
        if (isFactoryValid()) {
            existingKeyFactory = newKeyFactory;
            newKeyFactory = new KeyFactory();
            changed = false;
            existingKeyFactory.save();  // save persistently
            loadNotification("Key bindings were successfully saved.", false);
        } else {
            loadNotification("One or more key bindings are missing. " +
                    "Failed to save.", true);
        }
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    /**
     * Checks the {@link KeyFactory} being modified is valid and that no
     * commands are missing a key binding.
     * @return True if valid, false if invalid
     */
    private Boolean isFactoryValid(){
        for (Map.Entry<String, ControlKey> entry : newKeyFactory.getKeyState().entrySet
                ()) {
            if (entry.getKey().equals(entry.getValue().toString())){
                return false;
            }
        }
        return true;
    }

    /**
     * Method used to stop a user from exiting key bindings without saving
     * their changes to the {@link KeyFactory}.
     * @param we {@link WindowEvent} close request to be consumed if settings
     *                             have not been successfully saved.
     */
    private void onExit(WindowEvent we){
        // if modified KeyFactory hasn't been saved
        if (changed){
            loadNotification("Please cancel or save your changes before exiting" +
                    ".", true);
            we.consume();
        }
    }

    /**
     * Loads a popup window giving confirmation/warning of user activity.
     * @param message the message to be displayed to the user
     * @param warning true if the message to be displayed is due to user error
     */
    private void loadNotification(String message, Boolean warning){
        try {
            NotificationController nc = (NotificationController)
                    loadPopupScene("notification.fxml",
                    "", Modality.APPLICATION_MODAL);
            nc.setMessage(message, warning);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
