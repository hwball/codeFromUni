package visualiser.layout;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

/**
 * Created by Gondr on 27/09/2017.
 */
public class NewSeaSurface extends MeshView{
    private int seaIndex = 0;

    private AnimationTimer seaRipple = new AnimationTimer() {
        @Override
        public void handle(long now) {
            seaIndex = (seaIndex + 1) % Assets3D.sea.length;
            setMesh(Assets3D.sea[seaIndex].getMesh());
        }
    };

    public NewSeaSurface(){
        super(Assets3D.sea[0].getMesh());
        Color seaBlue = new Color(0.284, 0.573, 1.00, 1);
        Material seaMat = new PhongMaterial(seaBlue);
        setMaterial(seaMat);
        setMouseTransparent(true);
        seaRipple.start();
    }
}
