package visualiser.gameController;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import visualiser.gameController.Keys.ControlKey;
import visualiser.gameController.Keys.KeyFactory;
import java.util.HashMap;

/**
 * Class for checking what keys are currently being used
 */
public class InputChecker {
    private KeyFactory keyFactory;
    private HashMap<String, Boolean> currentlyActiveKeys = new HashMap<>();

    /**
     * Controller loop that detects key presses that runs parallel to the main scene.
     * @param scene Scene the controller is to run in parallel with.
     */
    public void runWithScene(Scene scene){
        KeyFactory keyFactory = new KeyFactory();
        keyFactory.load();

        scene.setOnKeyPressed(event -> {
            String codeString = event.getCode().toString();
            if (!currentlyActiveKeys.containsKey(codeString)) {
                ControlKey controlKey = keyFactory.getKey(codeString);
                if (controlKey != null) {
                    controlKey.onAction();
                }
                currentlyActiveKeys.put(codeString, true);
            }
        });

        scene.setOnKeyReleased(event -> {
            String codeString = event.getCode().toString();
            ControlKey controlKey = keyFactory.getKey(codeString);
            if (controlKey != null) {
                controlKey.onRelease();
            }
            currentlyActiveKeys.remove(event.getCode().toString());
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (String key: currentlyActiveKeys.keySet()){
                    ControlKey controlKey = keyFactory.getKey(key);
                    if (controlKey != null){
                        controlKey.onHold();
                    }
                }
            }
        }.start();
    }

    /**
     * removes a key from the active dictionary
     * @param codeString string of the key press to remove
     * @return whether or not the key has been removed or not.
     */
    private boolean removeActiveKey(String codeString) {
        Boolean isActive = currentlyActiveKeys.get(codeString);

        if (isActive != null && isActive) {
            currentlyActiveKeys.put(codeString, false);
            return true;
        } else {
            return false;
        }
    }

}
