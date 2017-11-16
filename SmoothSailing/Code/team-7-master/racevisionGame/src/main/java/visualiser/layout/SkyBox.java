package visualiser.layout;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import visualiser.app.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a skyBox
 */
public class SkyBox {
    private int size;
    private double x;
    private double y;
    private double z;
    private double freq;
    private double yshift;
    private double clipOverlap;
    private List<Subject3D> skyBoxPlanes = new ArrayList<>();

    public SkyBox(int edgeLength, double freq, double x, double y, double z) {
        this.size = edgeLength;
        this.x = x;
        this.y = y;
        this.z = z;
        this.freq = freq;
        this.yshift = -size/64;
        clipOverlap = 0;
        makeSkyBox();
    }

    private void makeSkyBox() {
        addTop();
        addFront();
        addBack();
        addLeft();
        addRight();
        //addSeaOverlay();
    }

    private void addTop() {
        String imagePath;
        if (App.dayMode) imagePath = "images/skybox/ThickCloudsWaterUp2048.png";
        else imagePath = "images/skybox/DarkStormyUp2048.png";
        MeshView surface = makeSurface(new Image(getClass().getClassLoader().getResourceAsStream(imagePath)), size);

        surface.setRotationAxis(new Point3D(0, 0, 1));
        surface.setRotate(180);

        surface.setTranslateX(x);
        surface.setTranslateY(y - size + 1);
        surface.setTranslateZ(z);

        Subject3D top = new SkyBoxPlane(surface,0);
        skyBoxPlanes.add(top);
    }

    private void addRight() {
        String imagePath;
        if (App.dayMode) imagePath = "images/skybox/ThickCloudsWaterRight2048.png";
        else imagePath = "images/skybox/DarkStormyRight2048.png";
        MeshView surface = makeSurface(new Image(getClass().getClassLoader().getResourceAsStream(imagePath)), size + 1);

        surface.setTranslateX(size/2);
        surface.setTranslateY(size/2);
        surface.setRotationAxis(new Point3D(1, 0, 0));
        surface.setRotate(90);
        surface.setTranslateX(-size/2);
        surface.setTranslateY(-size/2);

        surface.setTranslateX(x);
        surface.setTranslateY(y + yshift);
        surface.setTranslateZ(z + size/2 - clipOverlap);


        Subject3D right = new SkyBoxPlane(surface,0);
        skyBoxPlanes.add(right);
    }

    private void addLeft() {
        String imagePath;
        if (App.dayMode) imagePath = "images/skybox/ThickCloudsWaterLeft2048.png";
        else imagePath = "images/skybox/DarkStormyLeft2048.png";
        MeshView surface = makeSurface(new Image(getClass().getClassLoader().getResourceAsStream(imagePath)), size + 1);

        surface.setTranslateX(size/2);
        surface.setTranslateY(size/2);
        surface.setRotationAxis(new Point3D(1, 0, 0));
        surface.setRotate(-90);
        surface.setTranslateX(-size/2);
        surface.setTranslateY(-size/2);

        surface.setScaleX(-1);
        surface.setScaleZ(-1);

        surface.setTranslateX(x);
        surface.setTranslateY(y + yshift);
        surface.setTranslateZ(z - size/2 + clipOverlap);


        Subject3D left = new SkyBoxPlane(surface,0);
        skyBoxPlanes.add(left);
    }

    private void addBack() {
        String imagePath;
        if (App.dayMode) imagePath = "images/skybox/ThickCloudsWaterBack2048.png";
        else imagePath = "images/skybox/DarkStormyBack2048.png";
        MeshView surface = makeSurface(new Image(getClass().getClassLoader().getResourceAsStream(imagePath)), size);
        surface.getTransforms().add(new Rotate(90, 0, 0));

        surface.setRotationAxis(new Point3D(1, 0, 0));
        surface.setRotate(-90);

        surface.setScaleY(-1);
        surface.setScaleZ(-1);

        surface.setTranslateX(x - size/2 + clipOverlap);
        surface.setTranslateY(y + yshift);
        surface.setTranslateZ(z);

        Subject3D back = new SkyBoxPlane(surface,0);
        skyBoxPlanes.add(back);
    }

    private void addFront() {
        String imagePath;
        if (App.dayMode) imagePath = "images/skybox/ThickCloudsWaterFront2048.png";
        else imagePath = "images/skybox/DarkStormyFront2048.png";
        MeshView surface = makeSurface(new Image(getClass().getClassLoader().getResourceAsStream(imagePath)), size);
        surface.setTranslateX(size/2);
        surface.setTranslateY(size/2);
        surface.setRotationAxis(new Point3D(0, 0, 1));
        surface.setRotate(-90);
        surface.setTranslateX(-size/2);
        surface.setTranslateY(-size/2);


        surface.setTranslateX(x + size/2 - clipOverlap);
        surface.setTranslateY(y + yshift);
        surface.setTranslateZ(z);

        Subject3D front = new SkyBoxPlane(surface,0);
        skyBoxPlanes.add(front);
    }

    private MeshView makeSurface(Image diffuseMap, int size) {

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.web("#FFFFFF"));
        material.setSpecularColor(Color.web("#000000"));
        material.setDiffuseMap(diffuseMap);

        Plane3D plane = new Plane3D(size, size, 10, 10);
        MeshView surface = new MeshView(plane);
        surface.setMaterial(material);
        surface.setMouseTransparent(true);
        return surface;
    }

    private void addSeaOverlay() {
        SeaSurface seaSurface = new SeaSurface(size * 3, freq);
        seaSurface.setX(x);
        seaSurface.setY(y - size/4 + 1);
        seaSurface.setZ(z);
        skyBoxPlanes.add(seaSurface);
    }

    public List<Subject3D> getSkyBoxPlanes() {
        return skyBoxPlanes;
    }
}

