package utilities;

import adm.birch.Vector;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nagasaty on 4/23/17.
 */
public class FileReaders {
    Logger logger = Logger.getLogger(FileReaders.class);
    String  delimiter = ",";
    String csvFile ;
    LineIterator it = null;
    public FileReaders(String csvFile, String delimiter) throws IOException {
        this.it = FileUtils.lineIterator(new File(csvFile));
        this.csvFile = csvFile;
        this.delimiter = delimiter;
    }
    
    // pass in the -1 to get the full data set
    public List<Vector> getVectors(int limit){
        if(this.it == null){
            logger.error("FIle reader not initialized properly..returning null");
            return null;
        }
        List<Vector> res = new ArrayList<>();
        String line = "";
        Vector vline;
        while((limit == -1 || limit > 0) && it.hasNext()){
            res.add(new Vector(Arrays.stream(it.nextLine().split(this.delimiter)).mapToDouble(Double::parseDouble).toArray()));
            if(limit != -1) limit--;
        }
        return res;
    }
    

    public static void main(String[] args) throws IOException {
        FileReaders frs = new FileReaders("/Users/nagasaty/0classes/adm/adm_project/values.csv", ",");
        List<Vector> v = frs.getVectors(-1);
        System.out.println(v.size());
        for (int i = 0; i < v.size(); i++) {
            System.out.format("%d,%s", i,v.get(i));
            System.out.println();
        }

    }
}
