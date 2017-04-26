package adm.birch;

import java.util.HashMap;

/**
 * Created by nagasaty on 4/25/17.
 */
public class SVector{
    HashMap<Long, Double> x;

    public SVector() {
        this.x = new HashMap<>();
    }

    public void put(long col, double val) {
        this.x.put(col, val);
    }

    public SVector add(SVector y) {
        SVector res = new SVector();
        for(long key: this.x.keySet()){
            res.put(key, this.x.get(key));
        }
        
        for(long key: y.x.keySet()){
            res.put(key, res.x.getOrDefault(key, 0.0)+y.x.get(key));
        }
        return res;
    }

    public void addToThis(SVector y) {
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

    public SVector div(double param) {
        SVector res = new SVector();
        for(long key: this.x.keySet()){
            res.x.put(key, this.x.get(key)/param);
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

    public SVector square() {
        SVector res = new SVector();
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
    
    public SVector sub(SVector c) {
        SVector res = new SVector();
        double temp;
        for(long key: this.x.keySet()){
            res.put(key, this.x.get(key));
        }
        for(long key: c.x.keySet()){
            res.put(key, res.x.getOrDefault(key, 0.0)-c.x.get(key));
        }
        return res;
    }

    public void subFromThis(SVector y) {
        for(long key: y.x.keySet()){
            this.put(key, this.x.getOrDefault(key, 0.0)-y.x.get(key));
        }
    }

    public boolean isEqual(SVector y) {
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

    public boolean isEqualFields(SVector y) {
        if(y == null) return false;
        if(this.x.size() != y.x.size()) return false;
        for(long key: this.x.keySet()){
            if(!y.x.containsKey(key)) return false;
        }
        return true;
    }
    
    public double mul(SVector y) {
        double res = 0;
        for(long key: y.x.keySet()){
            res += this.x.getOrDefault(key, 0.0)*y.x.get(key);
        }
        return res;
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
