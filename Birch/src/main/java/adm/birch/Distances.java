package adm.birch;

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
        return getCentroid(points.getN(), points.getLS());
    }

    public static Vector getCentroid(int n, Vector ls) {
        // centroid = sum(vectors)/len(vectors)
        return ls.div(n);
    }

    public static double getRadius(DataPoints points){
        return getRadius(points.getN(), points.getLS(), points.getSS());
    }

    public static double getRadius(int n, Vector ls, Vector ss){
        Vector c = getCentroid(n, ls);
        return Math.sqrt((ss.val() + (n*c.squaredVal())- (2*c.mul(ls)))/n);
    }

    public static double getRadius(List<Vector> points){
        Vector c = Distances.getCentroid(points);
        double numerator = 0;
        for (Vector point : points){
            numerator += point.sub(c).squaredVal();
        }
        return Math.sqrt((numerator)/points.size());
    }

    public static double getDiameter(int n, Vector ls, Vector ss){
        Vector c = Distances.getCentroid(n, ls);
        return Math.sqrt((2* n * ss.val() - (2*ls.mul(ls)))/(n*(n-1)));
    }

    public static double getDiameter(DataPoints points){
        return getDiameter(points.getN(), points.getLS(), points.getSS());
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
    public static double getD2(int n1, Vector ls1, Vector ss1, int n2, Vector ls2, Vector ss2){
        if(ls1!= null && ls2!= null && ss1 != null && ss2 != null){
            return Math.sqrt((n2*ss1.val() + n1*ss2.val() - 2*ls1.mul(ls2))/(n1*n2));
        }else throw new NullPointerException("x or y clusters is comming as null.. ");
    }
    
    // avg inter cluster distance measure
    public static double getD2(DataPoints x, DataPoints y){
        if(x!= null && y != null && !x.isEmpty() && !y.isEmpty()){
            return getD2(x.getN(), x.getLS(), x.getSS(), y.getN(), y.getLS(), y.getSS());
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
    public static double getD3(int n1, Vector ls1, Vector ss1, int n2, Vector ls2, Vector ss2){
        if(ls1!= null && ls2!= null && ss1 != null && ss2 != null){
            return Distances.getDiameter(n1+n2, ls1.add(ls2), ss1.add(ss2));
        }else throw new NullPointerException("x or y clusters is comming as null.. ");
    }
    
    // avg intra cluster distance measure
    public static double getD3(DataPoints x, DataPoints y){
        if(x!= null && y != null && !x.isEmpty() && !y.isEmpty()){
            return getD3(x.getN(), x.getLS(), x.getSS(), y.getN(), y.getLS(), y.getSS());
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
