package adm.birch;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nagasaty on 4/20/17.
 */
public class LeafNode extends Node<Vector>{
    final static Logger logger = Logger.getLogger(LeafNode.class);
    List<Vector> points = null;
    private Vector LS;
    private Vector SS;
    private IntegerObj n;
    private double threshold;
    public LeafNode(int capacity, Node parentPtr, boolean isLeaf, double threshold) {
        super(capacity, parentPtr, isLeaf);
        this.points = new ArrayList<Vector>(capacity+1);
        this.threshold = threshold;
    }
    
    public LeafNode(int capacity, Node parentPtr, boolean isLeaf, ArrayList<Vector> points) {
        super(capacity, parentPtr, isLeaf);
        this.points = new ArrayList<>(capacity+1);
        for(Vector point : points){
            this.add(point);
        }
    }

    public LeafNode(int capacity, Node parentPtr, boolean isLeaf, List<Vector> points, Vector ls, Vector ss) {
        super(capacity, parentPtr, isLeaf);
        if(this.points == null || this.getLS() == null || this.getSS() == null){
            this.points = new ArrayList<>(capacity +1);
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
            if(this.points == null) this.points = new ArrayList<>(this.getCapacity()+1);
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
            if(this.n != null) this.n.addToThis(1);
            else this.n = new IntegerObj(1);
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

    public IntegerObj getN() {
        return this.n;
    }

    public Vector getLS() {
        return this.LS;
    }
    
    public Vector getSS() {
        return this.SS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("L(");
        for (int i = 0; i < points.size(); i++) {
            Vector node = points.get(i);
            sb.append(node.toString());
            if((i+1) != points.size()) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean isEmpty() {
        return this.points.isEmpty();
    }

    public int size(){ return this.points.size();}
    
    @Override
    // NOTE: no need of delta pointers for leaf node .. dont complicate the code
    public boolean insert(Vector dataPoint) {
        if(this.points.size() < getCapacity()){
//            if(this.points.size() == 0){
                add(dataPoint);
                return true;
//            }
//            Vector centroid = Distances.getCentroid(this.getN().value, this.getLS());
//            if(Distances.getD0(centroid, dataPoint) < this.threshold){
//                add(dataPoint); // add the point to the x set and return
//                return true;
//            }
//            return false;
        }
        return false;
    }
    
    // splits this leaf and the incoming x point.. and returns the new leafnode in addition to modifying this node.
    // this nodes parent x should be update at the top layers
    public CFNode split(Vector dataPoint){
        // chose 2 leaves which are farthest from each other
        double maxSofar = -1, dist;
        int maxSofarI = -1, maxSofarJ = -1;
        
        this.add(dataPoint);
        
        for (int i = 0; i < this.n.value; i++) {
            for(int j = i+1; j < this.n.value ;j++){
                dist = Distances.getD0(this.points.get(i), this.points.get(j));
                if(dist > maxSofar){
                    maxSofar = dist;
                    maxSofarI = i; 
                    maxSofarJ = j;
                }
            }
        }
        
        if(maxSofarI == -1 || maxSofarJ == -1) logger.error("Max sofar is not updated...");
        
        Vector src = this.points.get(maxSofarI);
        Vector dest = this.points.get(maxSofarJ);
        
         // iterate again and see which one is nearer.
        LeafNode x = new LeafNode(getCapacity(), null, true, this.threshold);
        LeafNode y = new LeafNode(getCapacity(), null, true, this.threshold);
        x.add(src); // added src to x cluster
        y.add(dest); // added dest to y cluster
        double dx = 0 , dy = 0 ;
        for (int i = 0; i < points.size(); i++) {
            if(i == maxSofarI || i == maxSofarJ) continue;
            Vector point = points.get(i);
            if (point != null) { // as we are making src and dest null.. (already added above)
                dx = Distances.getD0(Distances.getCentroid(x.getN().value, x.getLS()), point); // distance from centroid to curr vector
                dy = Distances.getD0(Distances.getCentroid(y.getN().value, y.getLS()), point); // distance from centroid to curr vector
                //TODO min criteria condition (that x cluster should contain L leves minimum inorder to maintain the height of the tree) is not satisfied here.
                // TODO threshold constraint (that all the vectors in this cluster should be < T.. not taken care of here..For now considers relative lower distance 
                // TODO this has to be implemented in a cleaner way)is not yet implementeed
                if (dx < dy) {
                    x.add(point);
                } else {
                    y.add(point);
                }
            }
        }
        // till here we got 2 leafnodes (new clusters x and y) populated with the x 
        
        // no need to set this.. as the parent pointer is not changed .. here
//        x.setParentPtr(this.getParentPtr());// update x parent pointer to currentnodes parent pointer
        this.setDelta(new CFEntry(x.getN().sub(this.getN()), new Vector((x.getLS().sub(this.getLS())).getCapacity()), new Vector((x.getSS().sub(this.getSS())).getCapacity())));
        logger.debug(this);
        this.points = x.points;// update x values to the current node
        this.LS.setValues(x.getLS());
        this.SS.setValues(x.getSS());
        this.n.setValues(x.getN());
        
        logger.debug(this);
        CFNode cfNode = new CFNode(y);
        y.setParentPtr(cfNode); // set y's parent to yi 
        
        // return yi
        return cfNode;
    }
}
