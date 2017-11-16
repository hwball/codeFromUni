package visualiser.layout;

import javafx.animation.AnimationTimer;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;

/**
 * Created by Gondr on 21/09/2017.
 */
public class Sails3D extends MeshView {
    private int index = 0;
    private boolean isLuffing = false;

    public Sails3D() {
        setMesh(Assets3D.sails[0].getMesh());
    }

    private AnimationTimer luff = new AnimationTimer() {
        @Override
        public void handle(long now) {
            setMesh(Assets3D.sails[index].getMesh());
            index = (index + 1) % 40;
        }
    };

    public void startLuffing(){
        luff.start();
        isLuffing = true;
    }

    public void stopLuffing(){
        luff.stop();
        isLuffing = false;
        setMesh(Assets3D.sails[0].getMesh());
    }

    public boolean isLuffing() {
        return isLuffing;
    }
}
