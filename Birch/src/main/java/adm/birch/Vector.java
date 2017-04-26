package adm.birch;

import java.util.Arrays;

/**
 * Created by nagasaty on 4/15/17.
 */
public class Vector{
    public double[] x;
    int len;
    
    public Vector(int capacity) {
        
        if(capacity <= 0 ) System.err.println("No of columns should be greater than 0");
        x = new double[capacity];
        this.len = capacity;
    }
    
    public Vector(double[] data){
        this.x = Arrays.copyOf(data, data.length);
        this.len = this.x.length;
    }

    public Vector add(Vector y){
        Vector res = new Vector(this.x);
        res.addToThis(y);
        return res;
    }
    
    public void addToThis(Vector y){
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
    
    public Vector div(double param){
        Vector res = new Vector(this.x);
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

    public Vector square() {
        Vector temp = new Vector(this.len);
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

    public Vector sub(Vector c) {
        Vector res = new Vector(this.x);
        res.subFromThis(c);
        return res;
    }

    private void subFromThis(Vector y) {
        if(this.len!= y.len) {
            System.err.println("vector lengths do not match");
            return;
        }

        for(int i = 0 ; i < this.len; i++){
            this.x[i] -= y.x[i];
        }
    }

    public boolean isEqual(Vector y) {
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
    public double mul(Vector y) {
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

    public void setValues(Vector ss) {
        for(int i = 0 ; i < this.len; i++){
            this.x[i] = ss.x[i];
        }
    }

    public void put(long col, double val) {
        this.x[(int)col-1] = val;
    }
}
