package adm.birch;

import java.util.HashMap;

/**
 * Created by nagasaty on 4/25/17.
 */
public class Vector{
    HashMap<Long, Double> x;
    int capacity;

    public Vector(int capacity) {
        this.x = new HashMap<>();
        this.capacity = capacity;
    }

    public Vector(Vector point){
        this.x = new HashMap<>();
        this.capacity = point.getCapacity();
        for(Long key: point.x.keySet()){
           // System.out.println(point.x.get(key));
            this.x.put(key,point.x.get(key));
        }
    }

    public int getCapacity(){
        return this.capacity;
    }

    public void put(long col, double val) {
        this.x.put(col, val);
    }

    public Vector add(Vector y) {
        Vector res = new Vector(y.getCapacity());
        for(long key: this.x.keySet()){
            res.put(key, this.x.get(key));
        }
        
        for(long key: y.x.keySet()){
            res.put(key, res.x.getOrDefault(key, 0.0)+y.x.get(key));
        }
        return res;
    }

    public void addToThis(Vector y) {
        for(long key: y.x.keySet()){
            this.x.put(key, this.x.getOrDefault(key, 0.0)+y.x.get(key));
        }
    }

    public void divThis(double param) {
        if(param == 0)return;
        for(long key: this.x.keySet()){
            this.x.put(key, this.x.get(key)/param);
        }
    }

    public Vector div(double param) {
        if(param ==0 ) return null;
        Vector res = new Vector(this.capacity);
        for(long key: this.x.keySet()){
            res.put(key, this.x.get(key)/param);
        }
        return res;
    }

    public double val() {
        double res = 0;
        for(long key: this.x.keySet()){
            res+= this.x.get(key);
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(long key: this.x.keySet()){
            sb.append(String.format("(%d,%f)", key, this.x.get(key)));
        }
        return sb.toString();
    }

    public Vector square() {
        Vector res = new Vector(this.capacity);
        double temp;
        for(long key: this.x.keySet()){
            temp = this.x.get(key);
            res.put(key, temp * temp);
        }
        return res;
    }

    public double squaredVal() {
        double res = 0;
        double temp;
        for(long key: this.x.keySet()){
            temp = this.x.get(key);
            res+= (temp*temp);
        }
        return res;
    }

    public double absVal() {
        double res = 0;
        double temp;
        for(long key: this.x.keySet()){
            temp = this.x.get(key);
            res+= Math.abs(temp);
        }
        return res;
    }

    public Vector sub(Vector c) {
        Vector res = new Vector(this.capacity);
        double temp;
        for(long key: this.x.keySet()){
            res.put(key, this.x.get(key));
        }
        for(long key: c.x.keySet()){
            res.put(key, res.x.getOrDefault(key, 0.0)-c.x.get(key));
        }
        return res;
    }

    public void subFromThis(Vector y) {
        for(long key: y.x.keySet()){
            this.put(key, this.x.getOrDefault(key, 0.0)-y.x.get(key));
        }
    }

    public boolean isEqual(Vector y) {
        if(y == null) return false;
        if(this.x.size() != y.x.size()) return false;
        for(long key: this.x.keySet()){
            if(!y.x.containsKey(key)) return false;
            else{
                if(!y.x.get(key).equals(this.x.get(key))) return false;
            }
        }
        return true;
    }

    public boolean isEqualFields(Vector y) {
        if(y == null) return false;
        if(this.x.size() != y.x.size()) return false;
        for(long key: this.x.keySet()){
            if(!y.x.containsKey(key)) return false;
        }
        return true;
    }
    
    public double mul(Vector y) {
        double res = 0;
        for(long key: y.x.keySet()){
            res += this.x.getOrDefault(key, 0.0)*y.x.get(key);
        }
        return res;
    }
    public void setValues(Vector ss) {
        this.x.clear();
        for(long key: ss.x.keySet()){
            this.x.put(key,ss.x.get(key));
        }
    }

//    
//    public double absVal() {
//        double res =0;
//        for (long key: this.x.keySet()){
//            res += 
//        }
//    }

//    public void setValues(SVector ss) {
//        
//
//    }
}
