package visualiser.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gondr on 9/10/2017.
 */
public class SoundAssets {
    public static SeagullSound defaultSound;
    public static List<SeagullSound> seagullSounds = new ArrayList<>();

    public static void loadAssets(){
        loadSeagullSounds();
    }

    public static void loadSeagullSounds(){
        defaultSound = new SeagullSound(SoundAssets.class.getClassLoader().getResource("assets/Seagull Sounds/default.wav").toExternalForm());
        defaultSound.setVolume(0.02);
        seagullSounds.add(new SeagullSound(SoundAssets.class.getClassLoader().getResource("assets/Seagull Sounds/seagle.mp3").toExternalForm()));
        seagullSounds.get(0).setVolume(0.05);
        seagullSounds.add(new SeagullSound(SoundAssets.class.getClassLoader().getResource("assets/Seagull Sounds/Seagull  1.wav").toExternalForm()));
        seagullSounds.get(1).setVolume(0.03);
        seagullSounds.add(new SeagullSound(SoundAssets.class.getClassLoader().getResource("assets/Seagull Sounds/Seagull 2.wav").toExternalForm()));
        seagullSounds.get(2).setVolume(0.03);
        seagullSounds.add(new SeagullSound(SoundAssets.class.getClassLoader().getResource("assets/Seagull Sounds/Seagull 3.wav").toExternalForm()));
        seagullSounds.get(3).setVolume(0.03);
        seagullSounds.add(new SeagullSound(SoundAssets.class.getClassLoader().getResource("assets/Seagull Sounds/Seagull 4.wav").toExternalForm()));
        seagullSounds.get(4).setVolume(0.03);
        seagullSounds.add(new SeagullSound(SoundAssets.class.getClassLoader().getResource("assets/Seagull Sounds/seagulls.wav").toExternalForm()));
        seagullSounds.get(5).setVolume(0.06);
        seagullSounds.add(new SeagullSound(SoundAssets.class.getClassLoader().getResource("assets/Seagull Sounds/Voice 001.wav").toExternalForm()));
        seagullSounds.get(6).setVolume(0.015);
        seagullSounds.add(new SeagullSound(SoundAssets.class.getClassLoader().getResource("assets/Seagull Sounds/Why.mp3").toExternalForm()));
        seagullSounds.get(7).setVolume(0.06);
    }

}
