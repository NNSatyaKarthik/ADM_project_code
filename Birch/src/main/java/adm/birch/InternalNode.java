package adm.birch;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nagasaty on 4/20/17.
 */
public class InternalNode extends Node<CFNode>{
    Logger logger = Logger.getLogger(InternalNode.class);
    ArrayList<CFNode> points;
    private IntegerObj n;
    private Vector LS;
    private Vector SS;
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(String.format("N:%d-LS:%s-SS:%s  :: ", this.getN().value, this.getLS(), this.getSS()));
        for (int i = 0; i < points.size(); i++) {
            CFNode node = points.get(i);
            sb.append(node.toString());
            if((i+1) != points.size()) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public boolean isEmpty() {
        return this.points.isEmpty();
    }

    public int size(){ return this.points.size();}

    public IntegerObj getN() {
        return this.n;
    }

    public Vector getLS() {
        return this.LS;
    }

    public Vector getSS() {
        return this.SS;
    }
    
    public InternalNode(int capacity, Node parentPtr, boolean isLeaf) {
        super(capacity, parentPtr, isLeaf);
        this.points = new ArrayList<>(capacity+1);
    }
    
    public InternalNode(int capacity, Node parentPtr, boolean isLeaf, List<CFNode> points) {
        super(capacity, parentPtr, isLeaf);
        if(this.points == null) this.points = new ArrayList<>(capacity+1);
        this.add(points);
    }

    public void add(List<CFNode> points) {
        if(this.points == null) this.points = new ArrayList<>(getCapacity()+1);
        for(CFNode point: points){
            this.add(point);
        }
    }

    public void add(int index, CFNode point){
        if(point != null){
            points.add(index, point);
            
            if(this.n == null) this.n = new IntegerObj(point.n.value);
            else this.n.addToThis(point.n.value);
            if(this.LS != null) this.LS.addToThis(point.LS);
            else this.LS = new Vector(point.LS.x);

            if(this.SS != null) this.SS.addToThis(point.SS);
            else this.SS = new Vector(point.SS.x);
        }
    }

    public void add(CFNode point){
        this.add(this.points.size(), point);
    }

    public CFNode get(int index){
        return (index < this.points.size())?this.points.get(index):null;
    }
    
    @Override
    public boolean insert(CFNode dataPoint) {
        logger.debug("capacity : "+this.points.size() + "--"+this.getCapacity());
        if(this.points.size() < getCapacity() ){
            
            IntegerObj n = (this.getN()!=null)?new IntegerObj(this.getN().value):null;
            Vector ls = (this.getLS()!=null)?new Vector(this.getLS().x):null;
            Vector ss = (this.getSS()!=null)?new Vector(this.getSS().x):null;
            add(dataPoint);
            if(ls!= null && ss!=null) this.setDelta(new CFEntry(this.getN().sub(n), this.getLS().sub(ls), this.getSS().sub(ss)));
            logger.debug("Delta set after insert of "+dataPoint +" to : "+this.delta);
            return true;
        }else 
            return false;
        // meld 2 internal nodes.. 
        // check if the internal nodes isfull.. then split the internal nodes with 
        // choose 2 clusters such that distance between these 2 should be max among all the internal nodes.. 
        // and then trie to meld 2
    }

    public CFNode split(CFNode dataPoint){
        logger.debug("Incoming x point to split INternal node: "+dataPoint);
        CFEntry e =  new CFEntry(new IntegerObj(this.getN().value), new Vector(this.getLS().x), new Vector(this.getSS().x));
        logger.debug("cfnode "+dataPoint+"is adding to "+e +"-- in internal node"+this);
        // chose 2 leaves which are farthest from each other
        double maxSofar = -1, dist;
        int maxSofarI = -1, maxSofarJ = -1;

        this.add(dataPoint);
        int n = this.points.size();
        CFNode a, b;
        for (int i = 0; i < n; i++) {
            for(int j = i+1; j < n ;j++){
                a = this.points.get(i);
                b = this.points.get(j);
                dist = Distances.getD2(a.getChildPtr().getN().value, a.getChildPtr().getLS(), a.getChildPtr().getSS(), b.getChildPtr().getN().value, b.getChildPtr().getLS(), b.getChildPtr().getSS());
                if(dist > maxSofar){
                    maxSofar = dist;
                    maxSofarI = i;
                    maxSofarJ = j;
                }
            }
        }

        if(maxSofarI == -1 || maxSofarJ == -1)System.err.println("Max sofar is not updated...");

        CFNode src = this.points.get(maxSofarI);
        CFNode dest = this.points.get(maxSofarJ);

        // iterate again and see which one is nearer.
        InternalNode x = new InternalNode(getCapacity(), null, false);
        InternalNode y = new InternalNode(getCapacity(), null, false);
        double dx = 0 , dy = 0 ;
        x.add(src);
        y.add(dest);
        Vector centroidVector;
        for (int i = 0; i < points.size(); i++) {
            if(i == maxSofarI || i== maxSofarJ) continue;
            CFNode point = points.get(i);
            if (point != null) {
                centroidVector = Distances.getCentroid(point.n.value, point.LS);
                dx = Distances.getD0(Distances.getCentroid(x.getN().value, x.getLS()), centroidVector); // distance from centroid to curr vector
                dy = Distances.getD0(Distances.getCentroid(y.getN().value, y.getLS()), centroidVector); // distance from centroid to curr vector
                //TODO min criteria condition is not satisfied here.
                // TODO threshold constraint is not yet implementeed
                if (dx < dy) {
                    x.add(point);
                } else {
                    y.add(point);
                }
            }
        }
        // till here we got 2 leafnodes populated with the x 

        // no need to set this.. as the parent pointer is not changed .. here
//        x.setParentPtr(this.getParentPtr());// update x parent pointer to currentnodes parent pointer
        logger.debug("Old entry values is: " + e);
        this.points = x.points;// update x values to the current node
        this.LS.setValues(x.LS);
        this.SS.setValues(x.SS);
        this.n.setValues(x.getN());
        logger.debug("New Entry values of CFENtry is : (after split) ("+this.n.value+", "+this.LS+", "+this.SS+")");
        logger.debug("x: "+x);
        logger.debug("y:"+y);
        e.n = this.n.sub(e.n);
        e.LS = this.LS.sub(e.LS);
        e.SS = this.SS.sub(e.SS);
        logger.debug("Delta value set for Internal node split of "+dataPoint + " is : "+e);
        setDelta(e);
        CFNode cfNode = new CFNode(y);
        y.setParentPtr(cfNode); // set y's parent to yi 

        // return yi
        return cfNode;
    }

    public void appendDelta(CFEntry delta) {
        if(delta != null){
            this.n.addToThis(delta.n.value);
            this.getLS().addToThis(delta.LS);
            this.getSS().addToThis(delta.SS);
        }
    }

    public void addVectorInfo(Vector data) {
        this.n.addToThis(1);
        this.getLS().addToThis(data);
        this.getSS().addToThis(data.square());
    }
}