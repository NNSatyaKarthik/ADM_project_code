package adm.birch;

import java.util.HashMap;

/**
 * Created by nagasaty on 4/25/17.
 */
public class SVector{
    HashMap<Long, Double> data ;

    public SVector() {
        this.data = new HashMap<>();
    }

    public void put(long col, double val) {
        this.data.put(col, val);
    }

    public SVector add(SVector y) {
        SVector res = new SVector();
        for(long key: this.data.keySet()){
            res.put(key, this.data.get(key));
        }
        
        for(long key: y.data.keySet()){
            res.put(key, res.data.getOrDefault(key, 0.0)+y.data.get(key));
        }
        return res;
    }

    public void addToThis(SVector y) {
        for(long key: y.data.keySet()){
            this.data.put(key, this.data.getOrDefault(key, 0.0)+y.data.get(key));
        }
    }

    public void divThis(double param) {
        if(param == 0)return;
        for(long key: this.data.keySet()){
            this.data.put(key, this.data.get(key)/param);
        }
    }

    public SVector div(double param) {
        SVector res = new SVector();
        for(long key: this.data.keySet()){
            res.data.put(key, this.data.get(key)/param);
        }
        return res;
    }

    public double val() {
        double res = 0;
        for(long key: this.data.keySet()){
            res+= this.data.get(key);
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(long key: this.data.keySet()){
            sb.append(String.format("(%d,%f)", key, this.data.get(key)));
        }
        return sb.toString();
    }

    public SVector square() {
        SVector res = new SVector();
        double temp;
        for(long key: this.data.keySet()){
            temp = this.data.get(key);
            res.put(key, temp * temp);
        }
        return res;
    }

    public double squaredVal() {
        double res = 0;
        double temp;
        for(long key: this.data.keySet()){
            temp = this.data.get(key);
            res+= (temp*temp);
        }
        return res;
    }
    
    public SVector sub(SVector c) {
        SVector res = new SVector();
        double temp;
        for(long key: this.data.keySet()){
            res.put(key, this.data.get(key));
        }
        for(long key: c.data.keySet()){
            res.put(key, res.data.getOrDefault(key, 0.0)-c.data.get(key));
        }
        return res;
    }

    public void subFromThis(SVector y) {
        for(long key: y.data.keySet()){
            this.put(key, this.data.getOrDefault(key, 0.0)-y.data.get(key));
        }
    }

    public boolean isEqual(SVector y) {
        if(y == null) return false;
        if(this.data.size() != y.data.size()) return false;
        for(long key: this.data.keySet()){
            if(!y.data.containsKey(key)) return false;
            else{
                if(!y.data.get(key).equals(this.data.get(key))) return false;
            }
        }
        return true;
    }

    public boolean isEqualFields(SVector y) {
        if(y == null) return false;
        if(this.data.size() != y.data.size()) return false;
        for(long key: this.data.keySet()){
            if(!y.data.containsKey(key)) return false;
        }
        return true;
    }
    
    public double mul(SVector y) {
        double res = 0;
        for(long key: y.data.keySet()){
            res += this.data.getOrDefault(key, 0.0)*y.data.get(key);
        }
        return res;
    }

//    
//    public double absVal() {
//        double res =0;
//        for (long key: this.data.keySet()){
//            res += 
//        }
//    }

//    public void setValues(SVector ss) {
//        
//
//    }
}
