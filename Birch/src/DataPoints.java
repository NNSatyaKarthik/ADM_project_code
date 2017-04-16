import java.util.ArrayList;
import java.util.List;

/**
 * Created by nagasaty on 4/16/17.
 */
public class DataPoints {
    List<Vector> points;
    private Vector LS;
    private Vector SS;

    public DataPoints(ArrayList<Vector> points) {
        this.points = new ArrayList<>();
        for(Vector point : points){
            this.add(point);
//            System.out.println(point + "--" + this.LS + "--"+ this.SS);
        }
    }

    public void add(Vector point){
        if(point != null){
            points.add(point);
            if(this.LS != null) this.LS.addToThis(point);
            else this.LS = new Vector(point.x);
            
            if(this.SS != null) this.SS.addToThis(point.square());
            else this.SS = (new Vector(point.x)).square();
        }
    }
    public int getN() {
        return points.size();
    }

    public Vector getLS() {
        return this.LS;
    }


    public Vector getSS() {
        return this.SS;
    }

    @Override
    public String toString() {
        return String.format("(%d, %s, %s)", this.getN(), this.getLS(), this.getSS());
    }
}
