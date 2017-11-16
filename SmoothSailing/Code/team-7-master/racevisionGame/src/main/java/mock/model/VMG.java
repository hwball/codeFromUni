package mock.model;

import shared.model.Bearing;

/**
 * This class encapsulates VMG - that is, velocity made good. It has a speed component and a bearing component.
 */
public class VMG {

    /**
     * Speed component of the VMG, in knots.
     */
    private double speed;

    /**
     * Bearing component of the VMG.
     */
    private Bearing bearing;


    /**
     * Ctor. Creates a VMG object with a given speed and bearing (that is, a velocity).
     * @param speed Speed component of the VMG.
     * @param bearing Bearing component of the VMG.
     */
    public VMG(double speed, Bearing bearing) {
        this.speed = speed;
        this.bearing = bearing;
    }


    /**
     * Returns the speed component of this VMG object, measured in knots.
     * @return Speed component of this VMG object.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Returns the bearing component of this VMG object.
     * @return Bearing component of this VMG object.
     */
    public Bearing getBearing() {
        return bearing;
    }

    public String toString(){
        return String.format("VMG Object: Speed %f, Bearing %f.", speed, bearing.degrees());
    }

}
