package adm.birch;

import adm.birch.Vector;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.Test;
import utilities.FileReaders;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nagasaty on 4/20/17.
 */
public class BirchTest {
    private ArrayList<Vector> points;

    @Before
    public void init() {
        points = new ArrayList<Vector>();
        Vector temp;
        for (int i = 1; i < 25; i++) {
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
        Birch birch = new Birch(0, 2, 4);
        for(Vector point: points){
            birch.insert(point);
//            System.out.println(point.x[0]);
        }
        System.out.println(birch.getRoot());
        System.out.println(birch.levelOrderTraversal());
        Map<Integer, List<Vector>> res = birch.labelData(4);
        for (Integer label : res.keySet()){
            List<Vector> list = res.get(label);
            for(Vector v : list){
                System.out.printf("%s,%d\n", v, label);
            }
        }
    }

    @Test
    public void testScikitDataset() throws IOException {
        FileReaders frs = new FileReaders("/Users/nagasaty/0classes/adm/adm_project/values.csv", ",");
        List<Vector> vectors = frs.getVectors(-1);
        System.out.println(vectors.size());
        Birch birch = new Birch(0, 3, 6);
        for(Vector point: vectors){
            birch.insert(point);
        }
        File f= new File("/Users/nagasaty/0classes/adm/adm_project/values_labeled.output.csv");
        String s = birch.levelOrderTraversal();
//        System.out.printf("%s",s);
        StringBuilder sb = new StringBuilder();
        Map<Integer, List<Vector>> res = birch.labelData(1);
        for (Integer label : res.keySet()){
            List<Vector> list = res.get(label);
            for(Vector v : list){
                sb.append(String.format("%s,%d\n", v, label));
//                System.out.printf("%s,%d\n", v, label);
            }
        }
        System.out.println(sb.toString());
        if(Files.exists(Paths.get(f.getAbsolutePath()))) Files.delete(Paths.get(f.getAbsolutePath()));
        FileUtils.writeStringToFile(f, sb.toString());
    }
}