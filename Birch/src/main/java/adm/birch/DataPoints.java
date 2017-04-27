package adm.birch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nagasaty on 4/16/17.
 */
public class DataPoints {
    List<Vector> points;
    private Vector LS;
    private Vector SS;

    public DataPoints(){
        this.points = new ArrayList<>();
    }
    
    public DataPoints(ArrayList<Vector> points) {
        this.points = new ArrayList<>();
        for(Vector point : points){
            this.add(point);
//            System.out.println(point + "--" + this.LS + "--"+ this.SS);
        }
    }

    public DataPoints(List<Vector> points, Vector ls, Vector ss) {
        if(this.points == null || this.getLS() == null || this.getSS() == null){
            this.points = new ArrayList<>();
            this.points.addAll(points);
            this.LS = ls;
            this.SS = ss;
        }else{
            this.points.addAll(points);
            this.LS.addToThis(ls);
            this.SS.addToThis(ss);
        }
    }

    public void add(List<Vector> points, Vector ls, Vector ss) {    
        if(this.points == null || this.getLS() == null || this.getSS() == null){
            this.points = new ArrayList<>();
            this.points.addAll(points);
            this.LS = ls;
            this.SS = ss;
        }else{
            this.points.addAll(points);
            this.LS.addToThis(ls);
            this.SS.addToThis(ss);
        }
    }
    public void add(int index, Vector point){
        if(point != null){
            points.add(index, point);
            if(this.LS != null) this.LS.addToThis(point);
            else this.LS = new Vector(point);
            
            if(this.SS != null) this.SS.addToThis(point.square());
            else this.SS = (new Vector(point)).square();
        }
    }
    
    public void add(Vector point){
        this.add(this.points.size(), point);
    }
    
    public Vector get(int index){
        return (index < this.points.size())?this.points.get(index):null;
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

    public boolean isEmpty() {
        return this.points.isEmpty();
    }
    
    public int size(){ return this.points.size();}
}
