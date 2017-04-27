package adm.birch;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
//import utilities.FileReaders;
import utilities.FileReadersMtx;

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

//    @Before
//    public void init() {
//        points = new ArrayList<Vector>();
//        Vector temp;
//        for (int i = 1; i < 25; i++) {
//            temp = new Vector(1);
//            //temp.put(i,x[0])
//            /*if(i %2 == 0){
//                xCluster.add(temp);
//            }else {
//                yCluster.add(temp);
//            */
//            points.add(temp);
//        }
////            System.out.println(temp.square());
//    }

    
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
//        FileReaders frs = new FileReaders("C:\\Users\\vivek\\Documents\\ADM_project_code\\data\\2015_08_000000000029.mtx", ",");
//        List<Vector> vectors = frs.getVectors(-1);
//        insertAndLabelData(vectors,0, 2, 4, "C:\\Users\\vivek\\Documents\\ADM_project_code\\data\\2015_08_000000000029_out.csv", 3);
            String basedir = "C://Users/vivek/Documents/ADM_project_code/data/";
            FileReadersMtx frs = new FileReadersMtx(basedir + "sample.Mtx", " ",2);
            List<Vector> vectors = frs.getVectors(-1);
            insertAndLabelData(vectors,0, 5, 10, basedir+ "sample_output.csv", 4);
    }

    public void insertAndLabelData(List<Vector> vectors, double threshold, int branchinfactor, int noOfLeaves, String outputFile, int numOfClusters) throws IOException {
        System.out.println(vectors.size());
        Birch birch = new Birch(threshold, branchinfactor, noOfLeaves);
        int c = 0;
        for(Vector point: vectors){
            System.out.format("%d, %s\n" , c++, point);
            birch.insert(point);
        }
        File f= new File(outputFile);
//        String s = birch.levelOrderTraversal();
//        System.out.printf("%s",s);
        System.out.println("Insertion Done");
        StringBuilder sb = new StringBuilder();
        Map<Integer, List<Vector>> res = birch.labelData(numOfClusters);
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

    @Test
    public void readMtxFile() throws IOException {
//        String basedir = "C://Users/vivek/Documents/ADM_project_code/data/";
        String basedir = "/Users/nagasaty/0classes/adm/adm_project/";
        FileReadersMtx frs = new FileReadersMtx(basedir + "sample.Mtx", " ",2);
        List<Vector> vectors = frs.getVectors(-1);
        insertAndLabelData(vectors,0.5, 5, 10, basedir+ "sample.csv", 4);
        
    }
}