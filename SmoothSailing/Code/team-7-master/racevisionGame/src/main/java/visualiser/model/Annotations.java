package visualiser.model;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that processes user selected annotation visibility options to
 * display the requested information on the
 * {@link ResizableRaceCanvas}. These are displayed
 * via the {@link visualiser.Controllers.RaceViewController}. <br>
 * Annotation options for a {@link VisualiserBoat} include: its name,
 * abbreviation, speed, the time since it passed the last
 * {@link shared.model.Mark}, estimated time to the next marker,
 * and a path it has travelled made up of
 * {@link TrackPoint}s.
 */
public class Annotations {
    private ResizableRaceCanvas raceMap;

    // checkable objects in the anchor pane
    private Map<String, CheckBox> checkBoxes = new HashMap<>();
    private Map<String, Toggle> annoToggles = new HashMap<>();

    // maps of selected and saved annotations
    private Map<String, Boolean> importantAnno = new HashMap<>();
    private Map<String, Boolean> annoShownBeforeHide = new HashMap<>();

    // string values match the fx:id value of check boxes
    private static String nameCheckAnno = "showName";
    private static String abbrevCheckAnno = "showAbbrev";
    private static String speedCheckAnno = "showSpeed";
    private static String pathCheckAnno = "showBoatPath";
    private static String timeCheckAnno = "showTime";
    private static String estTimeCheckAnno = "showEstTime";
    private static String guideLineAnno = "showGuideline";

    // string values match the fx:id value of radio buttons
    private static String noBtn = "noBtn";
    private static String hideBtn = "hideAnnoRBtn";
    private static String showBtn = "showAnnoRBtn";
    private static String partialBtn = "partialAnnoRBtn";
    private static String importantBtn = "importantAnnoRBtn";

    private Boolean selectShow = false;
    private String buttonChecked;
    private String prevBtnChecked;
    @FXML Button saveAnnoBtn;

    /**
     * Constructor to set up and display initial annotations
     * @param annotationPane javaFX pane containing annotation options
     * @param raceMap the canvas to update annotation displays
     */
    public Annotations(AnchorPane annotationPane, ResizableRaceCanvas raceMap){
        this.raceMap = raceMap;

        for (Node child : annotationPane.getChildren()) {
            // collect all check boxes into a map
            if (child.getClass()==CheckBox.class){
                checkBoxes.put(child.getId(), (CheckBox)child);
            }
            // collect annotation toggle radio buttons into a map
            else if (child.getClass()== RadioButton.class){
                //annotationGroup.getToggles().add((RadioButton)child);
                annoToggles.put(child.getId(), (RadioButton)child);
            }
            else if (child.getClass() == Button.class){
                saveAnnoBtn = (Button)child;
            }
        }
        initializeAnnotations();
    }


    /**
     * Set up initial boat annotations and shows all data.
     * Defines partial annotations.
     * Creates listeners for when the user selects a different annotation
     * visibility.
     */
    public void initializeAnnotations() {
        for (Map.Entry<String, CheckBox> checkBox : checkBoxes.entrySet())
        { annoShownBeforeHide.put(checkBox.getKey(), true); }

        addCheckBoxListeners();
        addSaveAnnoListener();
        addAnnoToggleListeners();

        annoToggles.get(showBtn).setSelected(true);
    }

    /**
     * Creates listeners for each checkbox so the annotation display is
     * updated when a user selects a different level of annotation visibility.
     */
    private void addCheckBoxListeners(){
        //listener for show name in annotation
        checkBoxes.get(nameCheckAnno).selectedProperty()
                .addListener((ov, old_val, new_val) -> {
            if (old_val != new_val) {
                raceMap.toggleAnnoName();
                storeCurrentAnnotationState(nameCheckAnno, new_val);
                raceMap.draw();
            }
        });

        //listener for show abbreviation for annotation
        checkBoxes.get(abbrevCheckAnno).selectedProperty()
                .addListener((ov, old_val, new_val) -> {
                    if (old_val != new_val) {
                        raceMap.toggleAnnoAbbrev();
                        storeCurrentAnnotationState(abbrevCheckAnno, new_val);
                        raceMap.draw();
                    }
                });

        //listener for show boat path for annotation
        checkBoxes.get(pathCheckAnno).selectedProperty()
                .addListener((ov, old_val, new_val) -> {
                    if (old_val != new_val) {
                        raceMap.toggleBoatPath();
                        storeCurrentAnnotationState(pathCheckAnno, new_val);
                        raceMap.draw();
                    }
                });

        //listener to show speed for annotation
        checkBoxes.get(speedCheckAnno).selectedProperty()
                .addListener((ov, old_val, new_val) -> {
                    if (old_val != new_val) {
                        raceMap.toggleAnnoSpeed();
                        storeCurrentAnnotationState(speedCheckAnno, new_val);
                        raceMap.draw();
                    }
                });

        //listener to show time for annotation
        checkBoxes.get(timeCheckAnno).selectedProperty()
                .addListener((ov, old_val, new_val) -> {
                    if (old_val != new_val) {
                        raceMap.toggleAnnoTime();
                        storeCurrentAnnotationState(timeCheckAnno, new_val);
                        raceMap.draw();
                    }
                });

        //listener to show estimated time for annotation
        checkBoxes.get(estTimeCheckAnno).selectedProperty()
                .addListener((ov, old_val, new_val) -> {
                    if (old_val != new_val) {
                        raceMap.toggleAnnoEstTime();
                        storeCurrentAnnotationState(estTimeCheckAnno, new_val);
                        raceMap.draw();
                    }
                });

        //listener to show estimated time for annotation
        checkBoxes.get(guideLineAnno).selectedProperty()
                .addListener((ov, old_val, new_val) -> {
                    if (old_val != new_val) {
                        raceMap.toggleGuideLine();
                        storeCurrentAnnotationState(guideLineAnno, new_val);
                        raceMap.draw();
                    }
                });
    }

    /**
     * Creates a listener so the system knows when to save a users currently
     * selected annotation options as important for future use.
     */
    private void addSaveAnnoListener(){
        //listener to save currently selected annotations as important
        saveAnnoBtn.setOnAction(event -> {
            importantAnno.clear();
            for (Map.Entry<String, CheckBox> checkBox : checkBoxes.entrySet()){
                importantAnno.put(checkBox.getKey(),
                        checkBox.getValue().isSelected());
            }
        });
    }

    /**
     * Creates listeners for each visibility option so that the annotation
     * display is updated when a user selects a different level of annotation
     * visibility.
     */
    private void addAnnoToggleListeners(){
        //listener for hiding all annotations
        RadioButton hideAnnoRBtn = (RadioButton)annoToggles.get(hideBtn);
        hideAnnoRBtn.setOnAction((e)->{
            buttonChecked = hideBtn;
            selectShow = false;
            for (Map.Entry<String, CheckBox> checkBox : checkBoxes.entrySet()){
                checkBox.getValue().setSelected(false);
            }
            raceMap.draw();
            buttonChecked = noBtn;
            prevBtnChecked = hideBtn;
            selectShow = true;
        });

        //listener for showing previously visible annotations
        RadioButton showAnnoRBTN = (RadioButton)annoToggles.get(showBtn);
        showAnnoRBTN.setOnAction((e)->{
            if (selectShow) {
                buttonChecked = showBtn;
                for (Map.Entry<String, CheckBox> checkBox : checkBoxes.entrySet()){
                    checkBox.getValue().setSelected(
                            annoShownBeforeHide.get(checkBox.getKey()));
                }
                raceMap.draw();
                buttonChecked = noBtn;
                prevBtnChecked = showBtn;
            }
            selectShow = true;
        });

        //listener for showing a predetermined subset of annotations
        RadioButton partialAnnoRBTN = (RadioButton)annoToggles.get(partialBtn);
        partialAnnoRBTN.setOnAction((e)->{
            selectShow = false;
            buttonChecked = partialBtn;
            for (Map.Entry<String, CheckBox> checkBox : checkBoxes.entrySet()){
                // the checkbox defaults for partial annotations
                if (checkBox.getKey().equals(abbrevCheckAnno)
                        || checkBox.getKey().equals(speedCheckAnno)){
                    checkBox.getValue().setSelected(true);
                }
                else { checkBox.getValue().setSelected(false); }
            }
            raceMap.draw();
            buttonChecked = noBtn;
            prevBtnChecked = partialBtn;
            selectShow = true;
        });

        //listener for showing all saved important annotations
        RadioButton importantAnnoRBTN = (RadioButton)annoToggles.get(importantBtn);
        importantAnnoRBTN.setOnAction((e) ->{
            selectShow = false;
            buttonChecked = importantBtn;
            if (importantAnno.size()>0){
                for (Map.Entry<String, CheckBox> checkBox : checkBoxes.entrySet()){
                    checkBox.getValue().setSelected
                            (importantAnno.get(checkBox.getKey()));
                }
            }
            raceMap.draw();
            buttonChecked = noBtn;
            prevBtnChecked = importantBtn;
            selectShow = true;
        });
    }

    /**
     * Updates the current state of an annotation so that when a user
     * deselects the hidden visibility option, the previous annotations will
     * be displayed again.
     * @param dictionaryAnnotationKey annotation checkbox
     * @param selected boolean value representing the state of the checkbox
     */
    private void storeCurrentAnnotationState(String dictionaryAnnotationKey, boolean selected){
        if (buttonChecked != hideBtn) {
            //if we are checking the box straight out of hide instead of using the radio buttons
            annoShownBeforeHide.put(dictionaryAnnotationKey, selected);
            if (prevBtnChecked == hideBtn && buttonChecked == noBtn){
                storeCurrentAnnotationDictionary();
            }
            if (buttonChecked == noBtn) {
                selectShow = false;
                annoToggles.get(showBtn).setSelected(true);
            }
        }
    }

    /**
     * Stores all current annotation states so that when a user
     * deselects the hidden visibility option, the previous annotations will
     * be displayed again.
     */
    private void storeCurrentAnnotationDictionary(){
        for (Map.Entry<String, CheckBox> checkBox : checkBoxes.entrySet()){
            annoShownBeforeHide.put(checkBox.getKey(),
                                    checkBox.getValue().isSelected());
        }
    }
}
