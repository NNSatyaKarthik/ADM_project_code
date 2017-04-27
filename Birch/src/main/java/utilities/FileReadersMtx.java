package utilities;

//import adm.birch.SVector;
import adm.birch.Vector;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nagasaty on 4/25/17.
 */
public class FileReadersMtx {
    Logger logger = Logger.getLogger(FileReadersMtx.class);
    String  delimiter;
    String mtxFile;
    LineIterator it = null;
    long rows, cols;
    public FileReadersMtx(String mtxFile, String delimiter, int skiplines) throws IOException {
        this.it = FileUtils.lineIterator(new File(mtxFile));
        int lc = 1 ;
        while(lc <= skiplines) {
            if(it.hasNext()) it.nextLine();
            else {
                logger.error("Number of lines are less than skipLines: "+skiplines);
            }
            lc++;
        }
        String[] line = it.nextLine().split(delimiter);
        this.rows = Long.parseLong(line[0]);
        this.cols = Long.parseLong(line[1]);
        this.mtxFile = mtxFile;
        this.delimiter = delimiter;
    }

    public FileReadersMtx(String mtxFile, String delimiter) throws IOException {
        this.it = FileUtils.lineIterator(new File(mtxFile));
        int lc = 1 ;
        while(lc <= 2) {
            if(it.hasNext()) it.nextLine();
            else {
                logger.error("Number of lines are less than skipLines: "+2);
            }
            lc++;
        }
        String[] line = it.nextLine().split(delimiter);
        this.rows = Long.parseLong(line[0]);
        this.cols = Long.parseLong(line[1]);
        this.mtxFile = mtxFile;
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
        String[] aline;
        long row, col, rowNum =-1;
        double val;
        Vector v = null;
        int capacity = (int)this.cols;
        while((limit == -1 || limit > 0) && it.hasNext()){
            line = it.nextLine();
            aline = line.split(this.delimiter);
            row = Long.parseLong(aline[0]);
            col = Long.parseLong(aline[1]);
            val = Double.parseDouble(aline[2]);
            if(rowNum == -1) {
                v = new Vector(capacity);
                rowNum = row;
            }
            
            if(row == rowNum){
                //insert
                v.put(col, val);
            }else{
                // create new and insert
                res.add(v);
                v = new Vector(capacity);
                v.put(col, val);
                rowNum = row;
            }
            
        }
        if(v != null){
            res.add(v);
        }
        return res;
    }


    public static void main(String[] args) throws IOException {
        FileReadersMtx frs = new FileReadersMtx("/Users/nagasaty/0classes/adm/adm_project/sample.Mtx", " ",2);
        List<Vector> v = frs.getVectors(-1);
        System.out.println(v.size());
        for (int i = 0; i < v.size(); i++) {
            System.out.format("%d,%s", i,v.get(i));
            System.out.println();
        }

    }
}
