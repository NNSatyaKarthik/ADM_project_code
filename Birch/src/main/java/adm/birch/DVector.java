package adm.birch;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by nagasaty on 4/15/17.
 */
public class DVector{
    public double[] x;
    int len;
    
    public DVector(int capacity) {
        
        if(capacity <= 0 ) System.err.println("No of columns should be greater than 0");
        x = new double[capacity];
        this.len = capacity;
    }
    
    public DVector(double[] data){
        this.x = Arrays.copyOf(data, data.length);
        this.len = this.x.length;
    }
    
    public DVector add(DVector y){
        DVector res = new DVector(this.x);
        res.addToThis(y);
        return res;
    }
    
    public void addToThis(DVector y){
        if(this.len!= y.len) {
            System.err.println("vector lengths do not match");
            return;
        }
        
        for(int i = 0 ; i < this.len; i++){
            this.x[i] += y.x[i];
        }
    }

    public void divThis(double param) {
        if(param == 0) return;
        for(int i = 0 ; i < this.len; i++){
            this.x[i] /= param;
        }
    }
    
    public DVector div(double param){
        DVector res = new DVector(this.x);
        res.divThis(param);
        return res;
    }
    
    public double val(){
        double res = 0; 
        for(int i = 0 ; i < this.len; i++){
            res += this.x[i];
        }
        return res;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String delimiter = ", ";
//        sb.append("(");
        for (int i = 0; i < this.len; i++) {
            sb.append(this.x[i]);
            if(i!= (this.len-1)) sb.append(delimiter);
        }
//        sb.append(")");
        return sb.toString();
    }

    public DVector square() {
        DVector temp = new DVector(this.len);
        for(int i = 0 ; i < this.len; i++){
            temp.x[i] = this.x[i] * this.x[i];
        }
        return temp;
    }

    public double squaredVal() {
        double res = 0; 
        for(int i = 0 ; i < this.len; i++){
            res += this.x[i] * this.x[i];
        }
        return res;
    }

    public DVector sub(DVector c) {
        DVector res = new DVector(this.x);
        res.subFromThis(c);
        return res;
    }

    private void subFromThis(DVector y) {
        if(this.len!= y.len) {
            System.err.println("vector lengths do not match");
            return;
        }

        for(int i = 0 ; i < this.len; i++){
            this.x[i] -= y.x[i];
        }
    }

    public boolean isEqual(DVector y) {
        if(y == null) return false; 
        if(this.len!= y.len) return false;

        for(int i = 0 ; i < this.len; i++){
            if(this.x[i] != y.x[i]) {
                return false;
            }
        }
        return true;
    }

    // cross product
    public double mul(DVector y) {
        double res = 0 ;
        if(y == null) return res;
        for(int i = 0 ; i < this.len; i++){
            res += this.x[i] * y.x[i];
        }
        return res;
    }

    public double absVal() {
        double res = 0 ;
        for(int i = 0 ; i < this.len; i++){
            res += ((this.x[i] >0 )?this.x[i]:-this.x[i]);
        }
        return res;
    }

    public void setValues(DVector ss) {
        for(int i = 0 ; i < this.len; i++){
            this.x[i] = ss.x[i];
        }
    }
}
