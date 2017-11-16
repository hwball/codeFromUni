package visualiser.model;


import javafx.scene.media.AudioClip;

/**
 * Created by Gondr on 9/10/2017.
 */
public class SeagullSound {

    private AudioClip sound;

    public SeagullSound(String file){
        this.sound = new AudioClip(file);
    }

    public void setVolume(double volume){
        sound.setVolume(volume);
        sound.volumeProperty().setValue(volume);
    }

    public void play(){
        this.sound.play();
    }
}
