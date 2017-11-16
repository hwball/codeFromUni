package visualiser.layout;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.transform.Translate;
import visualiser.model.GraphCoordinate;
import visualiser.utils.GPSConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Gondr on 28/09/2017.
 */
public class SeagullFlock{
    private ObservableList<Subject3D> seagulls = FXCollections.observableArrayList();
    private double xPos = 0;
    private double yPos = 0;
    private double zPos = 20;
    private double heading = 0;
    private double[] xOffset = {0, -5, -5};
    private double[] yOffset = {0, -5, 5};
    private double xBound = Integer.MAX_VALUE;//-/+
    private double yBound = Integer.MAX_VALUE;
    private List<Double> goToX = new ArrayList<>();
    private List<Double> goToY = new ArrayList<>();

    private double speed = 0.01;

    private AnimationTimer goTo = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (goToX.size() > 0) {
                //System.out.println(xPosition + " " + yPosition);
                heading = GPSConverter.getAngle(new GraphCoordinate(xPos, yPos),
                        new GraphCoordinate(goToX.get(0), goToY.get(0)));
                double delx = goToX.get(0) - xPos;
                double dely = goToY.get(0) - yPos;
                double scale = speed / Math.sqrt(delx * delx + dely * dely);
                if (scale < 1) {
                    xPos += delx * scale;
                    yPos += dely * scale;
                } else {
                    goToX.remove(0);
                    goToY.remove(0);
                }
            }else {
                stop();
                randNewAction();
            }
        }
    };

    private AnimationTimer update = new AnimationTimer() {
        @Override
        public void handle(long now) {
            for (int i = 0; i < seagulls.size(); i++){
                Subject3D seagull = seagulls.get(i);
                seagull.getMesh().setTranslateY(zPos);

                seagull.setHeading(heading);
                seagull.setX(xPos + xOffset[i]);
                seagull.setZ(yPos + yOffset[i]);
            }
        }
    };

    public SeagullFlock() {
        this(0d, 0d, 0d);
    }

    public SeagullFlock(double x, double y, double z){
        xPos = x;
        yPos = y;
        zPos = z;
        addToFlock();
        update.start();
        randNewAction();
    }

    public void addToFlock(){
        if (seagulls.size() < 3) {
            Annotation3D newSeagull = new Annotation3D(new SeagullMesh());
            newSeagull.setXRot(0);
            seagulls.add(newSeagull);
//            seagulls.add(new Annotation3D(new Sphere(5)));
        }
    }

    public void setxBound(double xBound) {
        this.xBound = xBound;
    }

    public void setyBound(double yBound) {
        this.yBound = yBound;
    }

    public void randNewAction(){
        Random rand = new Random();
        int nextAction = rand.nextInt(1);
        switch(nextAction){
            case 0:
                //do a straight line
                double nextX = rand.nextInt((int)this.xBound);
                /*if (nextX > this.xBound/2){
                    nextX = - nextX/2;
                }*/
                double nextY = rand.nextInt((int)this.yBound);
                /*if (nextY > this.yBound/2){
                    nextY = - nextY/2;
                }*/
                goToX.add(nextX);
                goToY.add(nextY);
                break;
            case 1:
                //do a octogan circle
                goToX.add(xPos - 3);
                goToX.add(xPos - 3);
                goToX.add(xPos);
                goToX.add(xPos + 3);
                goToX.add(xPos + 6);
                goToX.add(xPos + 6);
                goToX.add(xPos + 3);
                goToX.add(xPos);
                //y
                goToX.add(yPos - 3);
                goToX.add(yPos - 6);
                goToX.add(yPos - 9);
                goToX.add(yPos - 9);
                goToX.add(yPos - 6);
                goToX.add(yPos - 3);
                goToX.add(yPos);
                goToX.add(yPos);
                break;
        }
        goTo.start();
    }

    public List<Subject3D> getSeagulls() {
        return seagulls;
    }
}
