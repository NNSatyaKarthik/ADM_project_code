import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nagasaty on 4/15/17.
 */
public class Distances {
    //returns a centroid vector
    public static Vector getCentroid(List<Vector> points){
        // centroid = sum(vectors)/len(vectors)
        
        if(points.size() ==0  ) return null;
        Vector res = new Vector(points.get(0).x.length);
        
        for (Vector item: points) 
            res.addToThis(item);
        res.divThis(points.size());
        return res;
    }

    public static Vector getCentroid(DataPoints points){
        // centroid = sum(vectors)/len(vectors)
        Vector res = points.getLS();
        res = res.div(points.getN());
        return res;
    }
    
    public static double getRadius(DataPoints points){
        Vector c = Distances.getCentroid(points);
        return Math.sqrt((points.getSS().val() + (points.getN()*c.squaredVal())- (2*c.mul(points.getLS())))/points.getN());
    }

    public static double getRadius(List<Vector> points){
        Vector c = Distances.getCentroid(points);
        double numerator = 0;
        for (Vector point : points){
            numerator += point.sub(c).squaredVal();
        }
        return Math.sqrt((numerator)/points.size());
    }

    public static double getDiameter(DataPoints points){
        Vector c = Distances.getCentroid(points);
        int n  = points.getN();
        return Math.sqrt((2* n * points.getSS().val() - (2*points.getLS().mul(points.getLS())))/(n*(n-1)));
    }

    public static double getDiameter(List<Vector> points){
        double numerator = 0;
        
        //TODO add condition for n != 0 or n!= 1
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                numerator += points.get(i).sub(points.get(j)).squaredVal();
            }
        }
        return Math.sqrt((numerator)/(points.size() * (points.size()-1)));
    }
    
    public static double getD0(Vector x, Vector y){
        if(x!= null && y != null){
            return Math.sqrt(x.sub(y).squaredVal());
        }else throw new NullPointerException("x or y is comming as null.. ");
    }

    public static double getD1(Vector x, Vector y){
        if(x!= null && y != null){
            return x.sub(y).absVal();
        }else throw new NullPointerException("x or y is comming as null.. ");
    }

    // avg inter cluster distance measure
    public static double getD2(DataPoints x, DataPoints y){
        if(x!= null && y != null && !x.isEmpty() && !y.isEmpty()){
            int n1 = x.getN(), n2 = y.getN();
            return Math.sqrt((n2*x.getSS().val() + n1*y.getSS().val() - 2*x.getLS().mul(y.getLS()))/(n1*n2));
        }else throw new NullPointerException("x or y clusters is comming as null.. ");
    }

    // avg inter cluster distance measure
    public static double getD2(List<Vector> x, List<Vector> y){
        if(x!= null && y != null && !x.isEmpty() && !y.isEmpty()){
            double numerator = 0;
            int n1 = x.size(), n2 = y.size();
            for (int i = 0; i < n1; i++) {
                for (int j = 0; j < n2; j++) {
                    numerator += x.get(i).sub(y.get(j)).squaredVal();
                }
            }
            return Math.sqrt((numerator)/(n1*n2));
        }else throw new NullPointerException("x or y clusters points is comming as null.. ");
    }


    // avg intra cluster distance measure
    public static double getD3(DataPoints x, DataPoints y){
        if(x!= null && y != null && !x.isEmpty() && !y.isEmpty()){
            DataPoints obj = new DataPoints(x.points, x.getLS(), x.getSS());
            obj.add(y.points, y.getLS(), y.getSS());
            return Distances.getDiameter(obj);
        }else throw new NullPointerException("x or y clusters is comming as null.. ");
    }

    // avg intra cluster distance measure
    public static double getD3(List<Vector> x, List<Vector> y){
        if(x!= null && y != null && !x.isEmpty() && !y.isEmpty()){
            ArrayList<Vector> temp = new ArrayList<>();
            temp.addAll(x);
            temp.addAll(y);
            return Distances.getDiameter(temp);
        }else throw new NullPointerException("x or y clusters points is comming as null.. ");
    }


}
