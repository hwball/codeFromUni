package visualiser.layout;

import javafx.animation.AnimationTimer;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.model.Wind;

/**
 * Created by Gondr on 25/09/2017.
 */
public class WindCompass extends View3D {
    Subject3D compass;
    Subject3D windArrow;
    View3D view3D;
    Property<Wind> wind;

    AnimationTimer followView3D = new AnimationTimer() {
        @Override
        public void handle(long now) {
            setYaw(180+ view3D.getYaw());
            setPitch(30 - view3D.getPitch());
            windArrow.setHeading(wind.getValue().getWindDirection().degrees());
        }
    };

    public WindCompass(View3D view3D, Property<Wind> wind){
        super(false);
        this.wind = wind;
        this.view3D = view3D;
        this.followView3D.start();
        ObservableList<Subject3D> subjects = FXCollections.observableArrayList();
        this.setItems(subjects);
        compass = Assets3D.compass;
        windArrow = Assets3D.windArrow;
        subjects.addAll(compass, windArrow);
        this.setDistance(30);
        this.setPitch(90);
        this.setYaw(180);
    }

    public void setHeading(double heading){
        windArrow.setHeading(heading);
    }

    public void setCompassPitch(double pitch){
        setPitch(90d + pitch);
    }

}
