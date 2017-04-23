package adm.birch;
import adm.birch.CFEntry;
import adm.birch.CFNode;
import adm.birch.Distances;
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
    
    public LeafNode(int capacity, Node parentPtr, boolean isLeaf) {
        super(capacity, parentPtr, isLeaf);
        this.points = new ArrayList<Vector>(capacity+1);
    }
    
    public LeafNode(int capacity, Node parentPtr, boolean isLeaf, ArrayList<Vector> points) {
        super(capacity, parentPtr, isLeaf);
        this.points = new ArrayList<>(capacity+1);
        for(Vector point : points){
            this.add(point);
//            logger.info(point + "--" + this.LS + "--"+ this.SS);
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
            if(this.LS != null) this.LS.addToThis(point);
            else this.LS = new Vector(point.x);

            if(this.SS != null) this.SS.addToThis(point.square());
            else this.SS = (new Vector(point.x)).square();
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
    public boolean insert(Vector dataPoint) {
        if(this.points.size() < getCapacity()){
            int n = this.getN();
            Vector ls = this.getLS();
            Vector ss = this.getSS();
            add(dataPoint);
            if(ls!= null && ss!=null) setDelta(new CFEntry(this.getN()-n, this.getLS().sub(ls), this.getSS().sub(ss)));
            return true;
        }
        return false;
    }
    
    // splits this leaf and the incoming data point.. and returns the new leafnode in addition to modifying this node.
    // this nodes parent data should be update at the top layers
    public CFNode split(Vector dataPoint){
        // chose 2 leaves which are farthest from each other
        double maxSofar = -1, dist;
        int maxSofarI = -1, maxSofarJ = -1;
        
        this.add(dataPoint);
        int n = this.points.size();
        
        for (int i = 0; i < n; i++) {
            for(int j = i+1; j < n ;j++){
                dist = Distances.getD0(this.points.get(i), this.points.get(j));
                if(dist > maxSofar){
                    maxSofar = dist;
                    maxSofarI = i; 
                    maxSofarJ = j;
                }
            }
        }
        
        if(maxSofarI == -1 || maxSofarJ == -1){
            logger.error("Max sofar is not updated...");
//            System.exit(255);
        }
        
        Vector src = this.points.get(maxSofarI);
        Vector dest = this.points.get(maxSofarJ);
        
        this.points.set(maxSofarI, null); this.points.set(maxSofarJ, null);
        
         // iterate again and see which one is nearer.
        LeafNode x = new LeafNode(getCapacity(), null, true);
        LeafNode y = new LeafNode(getCapacity(), null, true);
        x.add(src); // added src to x cluster
        y.add(dest); // added dest to y cluster
        double dx = 0 , dy = 0 ; 
        for(Vector point : points){
            if(point != null){ // as we are making src and dest null.. (already added above)
                dx = Distances.getD0(Distances.getCentroid(x.getN(), x.getLS()),point); // distance from centroid to curr vector
                dy = Distances.getD0(Distances.getCentroid(y.getN(), y.getLS()),point); // distance from centroid to curr vector
                //TODO min criteria condition (that x cluster should contain L leves minimum inorder to maintain the height of the tree) is not satisfied here.
                // TODO threshold constraint (that all the vectors in this cluster should be < T.. not taken care of here..For now considers relative lower distance 
                // TODO this has to be implemented in a cleaner way)is not yet implementeed
                if(dx < dy){
                    x.add(point);
                }else{
                    y.add(point);
                }
            }
        }
        // till here we got 2 leafnodes (new clusters x and y) populated with the data 
        
        // no need to set this.. as the parent pointer is not changed .. here
//        x.setParentPtr(this.getParentPtr());// update x parent pointer to currentnodes parent pointer
        this.setDelta(new CFEntry(x.getN()-this.getN(), x.getLS().sub(this.getLS()), x.getSS().sub(this.getSS())));
        this.points = x.points;// update x values to the current node
        this.LS = x.LS;
        this.SS = x.SS;
        
        CFNode cfNode = new CFNode(y, new CFEntry(y.getN(), y.getLS(), y.getSS()));
        y.setParentPtr(cfNode); // set y's parent to yi 
        
        // return yi
        return cfNode;
    }
}
