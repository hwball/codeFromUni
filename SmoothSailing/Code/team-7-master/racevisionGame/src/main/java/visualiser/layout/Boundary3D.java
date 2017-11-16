package visualiser.layout;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import shared.model.GPSCoordinate;
import visualiser.model.GraphCoordinate;
import visualiser.utils.GPSConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that creates a 3d boundary based on gps coordinates
 */
public class Boundary3D {

    public static double thickness = 1;
    private List<Subject3D> boundaryNodes = new ArrayList<>();
    private List<Subject3D> boundaryConnectors = new ArrayList<>();
    private GPSConverter gpsConverter;

    public Boundary3D(List<GPSCoordinate> points, GPSConverter gpsConverter){
        this.gpsConverter = gpsConverter;
        setBoundaryNodes(points);
    }

    /**
     * Splits up the list so that it generates a edge of the boundary
     * @param points boundary gpscoordinate
     */
    private void setBoundaryNodes(List<GPSCoordinate> points){
        if (points.size() < 2){
            return;
        }

        for (int i = 0; i < points.size(); i++){
            if (i + 1 != points.size()){
                addBound(points.get(i), points.get(i + 1));
            } else {
                addBound(points.get(i), points.get(0));
            }
        }
    }

    /**
     * Add a two point boundary this will create a sphere at coord1 and a line to coord 2
     * this is to reduce double up (2 spheres in one point).
     * @param coord1 point to make sphere and start the line.
     * @param coord2 point to end the line.
     */
    private void addBound(GPSCoordinate coord1, GPSCoordinate coord2){
        GraphCoordinate graphCoord1 = gpsConverter.convertGPS(coord1);
        GraphCoordinate graphCoord2 = gpsConverter.convertGPS(coord2);
        GraphCoordinate avgCoord = new GraphCoordinate((graphCoord1.getX() + graphCoord2.getX()) / 2,
                (graphCoord1.getY() + graphCoord2.getY()) / 2);

        double a = (graphCoord1.getX() - graphCoord2.getX());
        double b = (graphCoord1.getY() - graphCoord2.getY());
        double c = Math.sqrt(a * a + b * b);


        Subject3D bound1 = new Annotation3D(new Sphere(thickness * 2));
        bound1.setX(graphCoord1.getX());
        bound1.setZ(graphCoord1.getY());
        boundaryNodes.add(bound1);

        Subject3D connector = new Annotation3D(new Box(c, thickness, thickness));
        connector.setX(avgCoord.getX());
        connector.setZ(avgCoord.getY());
        double angle = 90 + Math.toDegrees(GPSConverter.getAngle(graphCoord2, graphCoord1));
        connector.setHeading(angle);

        boundaryConnectors.add(connector);
    }

    /**
     * get the 3d objects to draw
     * @return 3d boundary to draw
     */
    public List<Subject3D> getBoundaryNodes(){
        //these two must be concatenated with nodes after connectors else the spheres will not overlap the lines
        ArrayList<Subject3D> result = new ArrayList<>(boundaryConnectors);
        result.addAll(boundaryNodes);
        return result;
    }
}
