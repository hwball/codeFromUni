package seng202.group6.controller;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seng202.group6.model.Record;
import seng202.group6.model.RecordList;

/**
 * Controller for the add edit window. Can operate in two mode: add and edit. Add creates a new
 * record, while edit edits and existing record.
 */
public abstract class AddEditController<T extends Record> {
    @FXML
    private Button buttonCancel;


    /**
     * Used to make the text fields' appearances change to indicate invalid contents.
     */
    protected final PseudoClass errorClass = PseudoClass.getPseudoClass("error");

    /**
     * The record being edited, or null if we are creating a new record
     */
    protected T recordUnderEdit;

    /**
     * The list to add the record to.
     */
    protected RecordList<T> sourceList;


    /**
     * Closes the window.
     */
    @FXML
    private void close() {
        buttonCancel.getScene().getWindow().hide();
    }


    /**
     * Called by the confirm button. Checks that all fields are valid and if so applies the edit or
     * creates a new record as appropriate.
     */
    @FXML
    private void confirm() {
        if (!validate()) {
            displayAlertInvalid();
        } else {
            if (recordUnderEdit != null) {
                applyEdit();
            } else {
                addRecord();
            }
            close();
        }
    }


    /**
     * Sets the mode and data.
     *
     * @param sourceList the list the record to be edited may be found in or the list to append the
     *                   new record to
     * @param index      the index in the source list of the record to edit, or null to indicate add
     *                   mode
     */
    public void setUp(RecordList<T> sourceList, Integer index) {
        this.sourceList = sourceList;
        if (index != null) {
            recordUnderEdit = sourceList.get(index);
            populateFields();
        }
    }

    /**
     * Displays an alert outlining that the operation cannot proceed because of invalid data.
     */
    private void displayAlertInvalid() {
        String action = (recordUnderEdit == null) ? "Create" : "Edit";
        Alert invalidAlert = new Alert(Alert.AlertType.ERROR);
        ((Stage) invalidAlert.getDialogPane().getScene().getWindow()).getIcons()
                .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
        invalidAlert.setTitle("Unable to " + action + " Entry");
        invalidAlert.setHeaderText(null);
        invalidAlert.setContentText("Unable to " + action.toLowerCase() + " using invalid fields!");
        invalidAlert.showAndWait();
    }


    /**
     * Populates the edit fields with the currently stored data.
     */
    protected abstract void populateFields();


    /**
     * Applies the changes in the data to the record under edit.
     */
    protected abstract void applyEdit();


    /**
     * Creates a new record using the data in the editor and adds it to the source list.
     */
    protected abstract void addRecord();


    /**
     * Checks that every field in the editor is valid.
     *
     * @return true if every field is valid else false.
     */
    protected abstract boolean validate();


    /**
     * JavaFX initialization method. Adds a listener to the text property of each text fields for
     * validation, and triggers an initial validation check on each field.
     */
    @FXML
    protected abstract void initialize();
}
