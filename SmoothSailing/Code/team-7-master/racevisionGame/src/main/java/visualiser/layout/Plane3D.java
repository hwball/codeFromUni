package visualiser.layout;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGNode;
import javafx.scene.Node;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;
import java.util.List;

/**
 * 3D plane
 */
public class Plane3D extends TriangleMesh{

    /**
     * Length is up down, and width is left right. Drawn on the x-y plane with z kept at 0.
     * @param width width of the plane
     * @param length length of the plane
     * @param subdivisionsWidth number of divisions along the width of the plane
     * @param subdivisionsLength number of division along the length of the plane
     */
    public Plane3D(float width, float length, int subdivisionsWidth, int subdivisionsLength){
        //add texture points and vertex points
        float subWidth = width / (float) subdivisionsWidth;
        float subLength = length / (float) subdivisionsLength;

        ArrayList<Float> pointsList = new ArrayList<>();
        ArrayList<Float> textureCoord = new ArrayList<>();
        float startW = -width/2;
        float startL = -length/2;

        for (float l = 0; l <= length; l += subLength) {
            for (float w = 0; w <= width; w += subWidth){
                    //add points
                    pointsList.add(w + startW);
                    pointsList.add(l + startL);
                    pointsList.add(0f);
                    //addTexture coords
                    textureCoord.add(1 - w/width);
                    textureCoord.add(1 - l/length);
            }
        }

        this.getPoints().setAll(copyListToArray(pointsList));
        this.getTexCoords().setAll(copyListToArray(textureCoord));

        //connect points to make faces
        ArrayList<Integer> faces = new ArrayList<>();

        int listSize = pointsList.size()/3;
        int divsInRow = subdivisionsWidth + 1;
        for (int i = 0; i < listSize; i++){
            int row = i/divsInRow;

            if (row < 1){
                continue;
            }

            boolean notFirstCol = (i) % divsInRow != 0;
            boolean notLastCol = (i + 1) % divsInRow != 0;
            if (notFirstCol){
                faces.add(i);
                faces.add(i);
//                printPointAtIndex(i);
                faces.add(i - divsInRow);
                faces.add(i - divsInRow);
//                printPointAtIndex(i - divsInRow);
                faces.add(i - 1);
                faces.add(i - 1);
//                printPointAtIndex(i-1);
            }
            if (notLastCol) {
                faces.add(i - divsInRow + 1);
                faces.add(i - divsInRow + 1);
//                printPointAtIndex(i - divsInRow + 1);
                faces.add(i - divsInRow);
                faces.add(i - divsInRow);
//                printPointAtIndex(i - divsInRow);
                faces.add(i);
                faces.add(i);
//                printPointAtIndex(i);
            }

        }
        this.getFaces().setAll(copyListToIntArray(faces));
    }

    /**
     * Testing function to see if the points are correct
     * @param index index that the points correspond to (remember 3 is a point)
     */
    private void printPointAtIndex(int index){
        int i = index * 3;
        float x = this.getPoints().get(i);
        float y = this.getPoints().get(i + 1);
        float z = this.getPoints().get(i + 2);
        System.out.println(String.format("Point at %d is x:%f, y:%f, z:%f", index, x, y, z));
    }

    /**
     * copies the list to a float array because java List.toArray isn't working
     * @param list list to copy
     * @return array
     */
    private static float[] copyListToArray(List<Float> list){
        float[] res = new float[list.size()];
        for (int i = 0; i < list.size(); i++){
            res[i] = list.get(i);
        }
        return res;
    }

    /**
     * copies the list to an integer array because java List.toArray isn't working
     * @param list list to copy
     * @return array
     */
    private static int[] copyListToIntArray(List<Integer> list){
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++){
            res[i] = list.get(i);
        }
        return res;
    }


}
