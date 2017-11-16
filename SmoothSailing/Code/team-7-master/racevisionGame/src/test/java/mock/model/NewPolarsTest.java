package mock.model;

import mock.dataInput.PolarParser;
import org.junit.Before;
import org.junit.Test;
import shared.model.Bearing;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by fwy13 on 5/09/17.
 */
public class NewPolarsTest {

    @Before
    public void setUp(){
        PolarParser.parseNewPolars("mock/polars/acc_polars.csv");
        NewPolars.linearInterpolatePolars();


//        Uncomment if you want to read the linear interpolation in text
//        Method getPolars = null;
//        try {
//            getPolars = NewPolars.class.getDeclaredMethod("printOutLinearInterpolated");
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        getPolars.setAccessible(true);
//        try {
//            getPolars.invoke(NewPolars.newPolars);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void testQuads() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //reflection for private class
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = Double.TYPE;
        Method getQuads = NewPolars.class.getDeclaredMethod("getQuadrant", parameterTypes);
        getQuads.setAccessible(true);

        //start invoking
        Object[] paras1 = new Object[1];
        paras1[0] = (new Double(0)).doubleValue();
        int q1 = (int) getQuads.invoke(NewPolars.newPolars, paras1);
        assertEquals(q1, 1);

        //start invoking
        Object[] paras2 = new Object[1];
        paras2[0] = (new Double(90)).doubleValue();
        int q2 = (int) getQuads.invoke(NewPolars.newPolars, paras2);
        assertEquals(q2, 2);

        //start invoking
        Object[] paras3 = new Object[1];
        paras3[0] = (new Double(180)).doubleValue();
        int q3 = (int) getQuads.invoke(NewPolars.newPolars, paras3);
        assertEquals(q3, 3);

        //start invoking
        Object[] paras4 = new Object[1];
        paras4[0] = (new Double(270)).doubleValue();
        int q4 = (int) getQuads.invoke(NewPolars.newPolars, paras4);
        assertEquals(q4, 4);

        //start invoking
        Object[] paras5 = new Object[1];
        paras5[0] = (new Double(360)).doubleValue();
        int q5 = (int) getQuads.invoke(NewPolars.newPolars, paras5);
        assertEquals(q5, 1);

    }

    @Test
    public void testEdgeSpeeds(){
        //just make sure that speeds at certain angles do not throw a null exception and are not negative
        double maxTWS = 30;

        for (double tws = 0; tws < maxTWS; tws += 1){
            for (double j = 0; j < 360; j++){
                Bearing twa = Bearing.fromDegrees(j);
                for (double i = 0; i < 360; i++){
                    Bearing boatBearing = Bearing.fromDegrees(i);
                    double speed = NewPolars.calculateSpeed(twa, tws, boatBearing);
                    assertTrue(speed >= 0);
                }
            }
        }

    }

    @Test
    public void testClosestSpeeds() throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        //reflection for private class
        Method getClosest = NewPolars.class.getDeclaredMethod("getClosest", double.class, Set.class);
        getClosest.setAccessible(true);

        Method getPolars = NewPolars.class.getDeclaredMethod("getPolars");
        getPolars.setAccessible(true);

        double maxTWS = 30;

        //only catches for nulls
        for (double tws = 0; tws < maxTWS; tws += 1){
            Map<Double, TreeMap<Double, Double>> polars = (Map<Double, TreeMap<Double, Double>>) getPolars.invoke(NewPolars.newPolars);
            double speed = (double) getClosest.invoke(NewPolars.newPolars, tws, polars.keySet());
            assertTrue(speed >= 0);
        }
    }

    @Test
    public void testAutoVSCalculated(){
        //test that the auto chosen speed is the same speed that is calculated
        double maxTWS = 30;
        for (double tws = 0; tws < maxTWS; tws ++){
            for (double twa = 0; twa < 360; twa ++){
                Bearing TW = Bearing.fromDegrees(twa);
                for (double ba = 0; ba < 360; ba ++){
                    Bearing boatBearing = Bearing.fromDegrees(ba);
                    VMG autoVMG = NewPolars.setBestVMG(TW, tws, boatBearing);
                    double speed = NewPolars.calculateSpeed(TW, tws, autoVMG.getBearing());
                    assertTrue(autoVMG.getSpeed() == speed);
                }
            }
        }
    }

}
