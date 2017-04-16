import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by nagasaty on 4/15/17.
 */
public class DistancesTest {
    private ArrayList<Vector> points;

    @Before
    public void init(){
        points = new ArrayList<Vector>();
        Vector temp;
        for(int i = 1 ;  i < 11 ; i++){
            temp  = new Vector(2);
            temp.x[0] = i;
            temp.x[1] = i;
//            System.out.println(temp.square());
            points.add(temp);
        }
    }
    
    @org.junit.Test
    public void getCentroid() throws Exception {
        Vector centroid = Distances.getCentroid(points);
        System.out.println("Centroid Vector: "+centroid);
        DataPoints dataPoints = new DataPoints(points); 
        System.out.println("Centroid Vector: "+Distances.getCentroid(dataPoints));
        assertEquals(true, centroid.isEqual(Distances.getCentroid(dataPoints)));
    }

    @org.junit.Test
    public void getRadius() throws Exception {
        double radius = Distances.getRadius(points);
        System.out.println("Radius : "+radius);
        DataPoints dataPoints = new DataPoints(points);
        System.out.println("Radius : "+Distances.getRadius(dataPoints));
        assertEquals(radius, Distances.getRadius(dataPoints), 0.000005);
    }

    @Test
    public void getDiameter() throws Exception {
        double radius = Distances.getDiameter(points);
        System.out.println("Diameter : "+radius);
        DataPoints dataPoints = new DataPoints(points);
        System.out.println("Diameter : "+Distances.getDiameter(dataPoints));
        assertEquals(radius, Distances.getDiameter(dataPoints), 0.000005);
    }
}