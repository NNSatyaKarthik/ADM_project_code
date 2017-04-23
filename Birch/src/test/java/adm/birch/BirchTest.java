package adm.birch;

import adm.birch.Vector;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.Test;
import utilities.FileReaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testScikitDataset() throws IOException {
        FileReaders frs = new FileReaders("/Users/nagasaty/0classes/adm/adm_project/values.csv", ",");
        List<Vector> vectors = frs.getVectors(-1);
        Birch birch = new Birch(0, 3, 6);
        for(Vector point: vectors){
            birch.insert(point);
        }
        File f= new File("/Users/nagasaty/0classes/adm/adm_project/levelOrderTraversal.output");
        String s = birch.levelOrderTraversal();
        
        FileUtils.writeStringToFile(f, s);
    }
}