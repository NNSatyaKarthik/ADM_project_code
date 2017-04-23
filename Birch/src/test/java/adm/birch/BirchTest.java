package adm.birch;

import adm.birch.Vector;
import org.junit.*;

import java.util.ArrayList;

/**
 * Created by nagasaty on 4/20/17.
 */
public class BirchTest {
    private ArrayList<Vector> points;

    @Before
    public void init() {
        points = new ArrayList<Vector>();
        Vector temp;
        for (int i = 1; i < 12; i++) {
            temp = new Vector(1);
            temp.x[0] = i;
            /*if(i %2 == 0){
                xCluster.add(temp);
            }else {
                yCluster.add(temp);
            */
            points.add(temp);
        }
//            System.out.println(temp.square());
    }

    
    @org.junit.Test
    public void test(){
        Birch birch = new Birch(0, 3, 6);
        for(Vector point: points){
            birch.insert(point);
//            System.out.println(point.x[0]);
        }
        System.out.println(birch.levelOrderTraversal());
    }

}