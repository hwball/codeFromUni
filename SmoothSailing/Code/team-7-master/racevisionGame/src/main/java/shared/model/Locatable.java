package shared.model;

/**
 * Created by cbt24 on 16/08/17.
 */
public interface Locatable {
    GPSCoordinate getPosition();
    void setPosition(GPSCoordinate position);
}
