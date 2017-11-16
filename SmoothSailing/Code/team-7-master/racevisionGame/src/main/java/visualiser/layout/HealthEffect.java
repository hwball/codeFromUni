package visualiser.layout;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

/**
 * Created by zwu18 on 24/09/17.
 */
public class HealthEffect extends Subject3D {

    private int sourceID;
    private long currentTime;
    private AudioClip warningSound = new AudioClip(this.getClass().getResource("/visualiser/sounds/warning.mp3").toExternalForm());
    private AudioClip deadSound = new AudioClip(this.getClass().getResource("/visualiser/sounds/dead1.wav").toExternalForm());

    public HealthEffect(int sourceID, long currentTime){
        super(createEffect(), 0);
        this.sourceID = sourceID;
        this.currentTime = currentTime;
    }

    /**
     * Initialise the mesh view with image
     * @return Mesh view
     */
    private static Shape3D createEffect(){

        Image image = new Image(HealthEffect.class.getClassLoader().getResourceAsStream("images/warning.png"));

        Plane3D plane = new Plane3D(20, 20, 10, 10);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.web("#FFFFFF"));
        material.setSpecularColor(Color.web("#000000"));
        material.setDiffuseMap(image);

        MeshView imageSurface = new MeshView(plane);


        imageSurface.setMaterial(material);
        imageSurface.setMouseTransparent(true);
        //imageSurface.toFront();        this.flashInterval = flashInterval;

        return imageSurface;
    }

    public void rotateView(Double angle, Double pivotX, Double pivotY, Double pivotZ, Point3D axis){
        Rotate rotate = new Rotate(angle, axis);
        rotate.setPivotX(pivotX);
        rotate.setPivotY(pivotY);
        rotate.setPivotZ(pivotZ);
        this.getMesh().getTransforms().add(rotate);
    }

    public void setVisible(boolean bool){
        this.getMesh().setVisible(bool);
    }


    public int getSourceID(){
        return sourceID;
    }

    public void setSourceID(int id){
        this.sourceID = id;
    }

    /**
     * Display visual indication when boat dies
     * @param player boolean if player is current user or not
     */
    public void displayDeath(boolean player){
        Image image = new Image(HealthEffect.class.getClassLoader().getResourceAsStream("images/warning2.png"));
        PhongMaterial material = (PhongMaterial) this.getMesh().getMaterial();
        material.setDiffuseColor(Color.web("#FFFFFF"));
        material.setSpecularColor(Color.web("#000000"));
        material.setDiffuseMap(image);
        this.getMesh().setMaterial(material);
        if(player) {
            deadSound.play();
        }
    }


    /**
     * Flash the mesh view at a certain interval
     * @param checkTime The current time of flash
     * @param flashInterval Desired flash interval
     * @param playerBoat Whether or not this effect is for the player's boat.
     */
    public void flash(long checkTime, long flashInterval, boolean playerBoat){
        if(checkTime >= (currentTime+flashInterval)){
            this.currentTime = checkTime;
            if(this.getMesh().isVisible()){
                this.setVisible(false);
            } else {
                if(playerBoat) {
                    warningSound.setVolume(0.1);
                    warningSound.play();
                }
                this.setVisible(true);
            }
        }
    }

}
