package visualiser.gameController.Keys;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating Keys, these could be predefined in the future.
 */
public class KeyFactory {
    /**
     * Retrieve command given key
     */
    private Map<String, ControlKey> keyState;

    /**
     * Constructor for key state, set up initial state of each action.
     */
    public KeyFactory() {
        this.keyState = new HashMap<>();
        keyState.put("Z", new ZoomInKey());
        keyState.put("X", new ZoomOutKey());
        keyState.put("SPACE", new VMGKey());
        keyState.put("SHIFT", new SailsToggleKey());
        keyState.put("ENTER", new TackGybeKey());
        keyState.put("UP", new UpWindKey());
        keyState.put("DOWN", new DownWindKey());
    }

    /**
     * Get the Control Key in charge of a key press
     * @param key key pressed (String value of KeyCode)
     * @return the Control Key behaviour of the key pressed.
     */
    public ControlKey getKey(String key){
        return keyState.get(key);
    }

    public Map<String, ControlKey> getKeyState() {
        return keyState;
    }

    public void setKeyState(Map<String, ControlKey> keyState) {
        this.keyState = keyState;
    }

    /**
     * Update the key bound to a particular command in the keystate.
     * @param newKey the new key value for the command
     * @param command the command to be updated
     */
    public void updateKey(String newKey, String command){
        ControlKey controlKey = null;
        String oldKey = null;
        for (Map.Entry<String, ControlKey> entry : keyState.entrySet()) {
            // if this is the correct command
            if (entry.getValue().toString()==command){
                controlKey = entry.getValue();
                oldKey = entry.getKey();
            }
        }
        keyState.remove(oldKey, controlKey);
        keyState.put(newKey, controlKey);
    }

    /**
     * Persistently saves the keybindings the user has set.
     */
    public void save(){
        try {
            // open the filestream and write to it
            FileOutputStream fos = new FileOutputStream(
                    System.getProperty("user.dir")+
                            "/settings/keyBindings.xml");
            XMLEncoder xmlEncoder = new XMLEncoder(fos);
            xmlEncoder.writeObject(this.keyState);
            xmlEncoder.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the persistently saved keybindings the user has set.
     */
    public void load(){
        try {
            // access settings folder, create if it doesn't exist
            File settingsFolder = new File(
                    System.getProperty("user.dir")+"/settings");
            if (!settingsFolder.exists()){
                settingsFolder.mkdir();
            }

            // access keybindings xml file, create if it doesn't exist
            File savedFile = new File(
                    settingsFolder+"/keyBindings.xml");
            if (!savedFile.exists()){
                savedFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(savedFile);
                XMLEncoder xmlEncoder = new XMLEncoder(fos);
                xmlEncoder.writeObject(this.keyState);
                xmlEncoder.close();
            }

            // load the saved settings into the game
            InputStream is = new FileInputStream(savedFile);
            XMLDecoder xmlDecoder = new XMLDecoder(is);
            Map<String, ControlKey> savedKeyState
                    = (Map<String, ControlKey>)xmlDecoder.readObject();
            xmlDecoder.close();
            this.keyState = savedKeyState;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
