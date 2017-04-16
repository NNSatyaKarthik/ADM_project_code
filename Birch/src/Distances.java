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
        return Math.sqrt((points.getSS().val() + (points.getN()*c.square().val())- (2*c.mul(points.getLS())))/points.getN());
    }

    public static double getRadius(List<Vector> points){
        Vector c = Distances.getCentroid(points);
        double numerator = 0;
        for (Vector point : points){
            numerator += point.sub(c).square().val();
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
                numerator += points.get(i).sub(points.get(j)).square().val();
            }
        }
        return Math.sqrt((numerator)/(points.size() * (points.size()-1)));
    }
}
