package visualiser.layout;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import visualiser.model.SeagullSound;
import visualiser.model.SoundAssets;

import java.util.Random;

/**
 * Created by Gondr on 28/09/2017.
 */
public class SeagullMesh extends MeshView{
    private int index = 0;
    private int isFlapping = 0;
    private int flapPeriod;
    private int flapStrength;
    private int periodElapsed = 0;
    private SeagullSound sound;
    private SeagullSound konamiSound;
    private int cryCooldown = 600;
    private int cryState = 0;

    public SeagullMesh() {
        setMesh(Assets3D.seagull[0].getMesh());
        PhongMaterial white = new PhongMaterial(Color.WHITE);
        setMaterial(white);
        Random rand = new Random();
        flapPeriod = rand.nextInt(9);
        flapPeriod = 60 * (11 + flapPeriod);
        flapStrength = rand.nextInt(3) + 2;
        scheduledFlap.start();
        cryCooldown = rand.nextInt(10) * 60 + 600;//chirpyness factor
        sound = SoundAssets.defaultSound;
        konamiSound = SoundAssets.seagullSounds.get(rand.nextInt(SoundAssets.seagullSounds.size()));
    }

    private AnimationTimer flap = new AnimationTimer() {
        @Override
        public void handle(long now) {
            index = (index + 1) % Assets3D.seagull.length;
            setMesh(Assets3D.seagull[index].getMesh());
            if (index == 0){
                isFlapping ++;
                if (isFlapping >= flapStrength){
                    stop();
                    setMesh(Assets3D.seagull[0].getMesh());
                    isFlapping = 0;
                }
            }
        }
    };

    private AnimationTimer scheduledFlap = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (periodElapsed == 0 && isFlapping == 0){
                startFlapping();
            }
            periodElapsed = (periodElapsed + 1) % flapPeriod;
            if (cryState > 0) {
                cryState -= 1;
            }
        }
    };

    public void startFlapping(){
        flap.start();
    }

    public void konamiTriggered(){
        sound = konamiSound;
    }

    public void playCry(){
        if (cryState == 0) {
            sound.play();
            cryState = cryCooldown;
        }
    }
}
