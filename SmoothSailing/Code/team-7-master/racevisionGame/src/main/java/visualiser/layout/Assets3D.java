package visualiser.layout;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import com.interactivemesh.jfx.importer.x3d.X3dModelImporter;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import visualiser.Controllers.HostGameController;

import java.net.URL;

/**
 * Created by Gondr on 21/09/2017.
 */
public class Assets3D {

    public static MeshView[] sails;
    public static MeshView[] sea;
    public static MeshView[] seagull;
    public static Subject3D windArrow;
    public static Subject3D compass;
    public static Subject3D cwNextArrow;
    public static Subject3D ccwNextArrow;
    public static SkyBox skyBox;
    public static Subject3D boatHighlight;
    public static Subject3D sharks;

    public static void loadAssets(){
        loadSails();
        loadWindArrow();
        loadNextArrow();
        loadSeaSurface();
        //loadSkybox();
        loadSeagull();
        loadBoatHightlight();
    }

    private static void loadNextArrow(){
        StlMeshImporter objModelImporter = new StlMeshImporter();
        String cwPath = "assets/Next Mark Arrow CW V1.0.stl";
        URL asset = Assets3D.class.getClassLoader().getResource(cwPath);
        objModelImporter.read(asset);
        Material markColor = new PhongMaterial(new Color(0.15,0.9,0.2,1));

        MeshView cwMesh = new MeshView(objModelImporter.getImport());
        cwMesh.setMaterial(markColor);
        cwMesh.setMouseTransparent(true);
        cwMesh.toBack();
        cwMesh.setVisible(false);
        cwNextArrow = new Annotation3D(cwMesh);

        String ccwPath = "assets/Next Mark Arrow CCW V1.0.stl";
        URL ccwAsset = Assets3D.class.getClassLoader().getResource(ccwPath);
        objModelImporter.read(ccwAsset);

        MeshView ccwMesh = new MeshView(objModelImporter.getImport());
        ccwMesh.setMaterial(markColor);
        ccwMesh.setMouseTransparent(true);
        ccwMesh.toBack();
        cwMesh.setVisible(false);
        ccwNextArrow = new Annotation3D(ccwMesh);

    }

    private static void loadBoatHightlight(){
        Material markColor = new PhongMaterial(new Color(0,1,0,0.5));
        StlMeshImporter objModelImporter = new StlMeshImporter();
        String path = "assets/V1.0 Boat Highlight.stl";
        URL highlight = Assets3D.class.getClassLoader().getResource(path);
        objModelImporter.read(highlight);

        MeshView hMesh = new MeshView(objModelImporter.getImport());
        hMesh.setMaterial(markColor);
        hMesh.setMouseTransparent(true);
        hMesh.toBack();
        boatHighlight = new Subject3D(hMesh, 0);

    }

    private static void loadSails(){
        sails = new MeshView[40];
        ObjModelImporter objModelImporter = new ObjModelImporter();

        for (int i = 0; i < sails.length; i++){
            String path = String.format("assets/Sails/V1.5 Sail_%06d.obj", i + 1);
            URL asset = Assets3D.class.getClassLoader().getResource(path);
            objModelImporter.read(asset);
            if (objModelImporter.getImport().length > 0) {
                sails[i] = objModelImporter.getImport()[0];
                /*sails[i].setRotationAxis(new Point3D(1, 0, 0));
                sails[i].setRotate(-90);*/
            }
        }
    }

    private static void loadSeaSurface(){
        sea = new MeshView[100];
        ObjModelImporter objModelImporter = new ObjModelImporter();

        for (int i = 0; i < sea.length; i++){
//            String path = String.format("assets/Ocean V1.0 Small Animation/Ocean V1.0 Large Animation_%06d.obj", i + 1);
//            String path = String.format("assets/Ocean V1.0 Animation/Ocean V1.0_%06d.obj", i + 1);
            String path = String.format("assets/Ocean Animation/Ocean Animation_%06d.obj", i + 1);
            URL asset = Assets3D.class.getClassLoader().getResource(path);
            objModelImporter.read(asset);
            if (objModelImporter.getImport().length > 0) {
                sea[i] = objModelImporter.getImport()[0];
            }
        }
    }

    private static void loadSeagull(){
        seagull = new MeshView[30];
        ObjModelImporter objModelImporter = new ObjModelImporter();

        for (int i = 0; i < seagull.length; i++){
            String path = String.format("assets/V1.1 Animated/Seagull V1.1_%06d.obj", i + 1);
            URL asset = Assets3D.class.getClassLoader().getResource(path);
            objModelImporter.read(asset);
            if (objModelImporter.getImport().length > 0) {
                seagull[i] = objModelImporter.getImport()[0];
            }
        }
    }

    public static Shape3D getBoat(){
        String path = "assets/V1.4 Boat.x3d";
        return loadX3d(path);
    }

    public static Shape3D getMark(){
        String path = "assets/Burger Bouy V1.1.x3d";
        return loadX3d(path);
    }

    private static void loadWindArrow(){
        String compassPath = "assets/wind_compass.x3d";
        compass = new Annotation3D(loadX3d(compassPath));
        String arrowPath = "assets/wind_arrow.x3d";
        windArrow = new Annotation3D(loadX3d(arrowPath));
    }

    public static Shape3D getSharks(){
        String path = "assets/V1.0 Sharks.x3d";
        Shape3D shark = loadX3d(path);
        shark.setMouseTransparent(true);
        return shark;
    }

    public static Shape3D loadX3d(String path){
        X3dModelImporter x3dModelImporter = new X3dModelImporter();
        URL asset = Assets3D.class.getClassLoader().getResource(path);
        x3dModelImporter.read(asset);
        if (x3dModelImporter.getImport().length > 0) {
            //if (x3dModelImporter.getImport()[0] instanceof MeshView) {
            Group g = (Group)((Group)((Group) x3dModelImporter.getImport()[0]).getChildren().get(0)).getChildren().get(0);
            Shape3D shape = (Shape3D)g.getChildren().get(0);
            return shape;
            /*} else {
                System.err.println("Boat is not a Mesh View 0.0");
            }*/
        }
        return null;
    }

    private static void loadSkybox(){
        skyBox = new SkyBox(750, 200, 250, 0, 210);
    }

}
