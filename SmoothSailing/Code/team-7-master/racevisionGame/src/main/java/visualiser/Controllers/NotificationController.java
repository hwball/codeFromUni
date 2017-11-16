package visualiser.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller for a popup notification regarding user activity.
 */
public class NotificationController extends Controller{
    private @FXML Label lblDescription;
    private @FXML Text txtMessage;

    /**
     * Closes the popup window once clicked.
     */
    public void ok(){
        ((Stage)lblDescription.getScene().getWindow()).close();
    }

    /**
     * Displays the appropriate popup notification.
     * @param message message for the user
     * @param warning if true warning text shown, if false success text shown
     */
    public void setMessage(String message, Boolean warning){
        lblDescription.setText(message);
        if (!warning){
            txtMessage.setText("Success!");
            txtMessage.setFill(Color.GREEN);
        }
    }
}
