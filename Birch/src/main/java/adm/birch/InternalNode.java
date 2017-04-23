package adm.birch;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by nagasaty on 4/20/17.
 */
public class InternalNode extends Node<CFNode>{
    ArrayList<CFNode> points;
    private int n;
    private Vector LS;
    private Vector SS;
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(String.format("N:%d-LS:%s-SS:%s  :: ", this.getN(), this.getLS(), this.getSS()));
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

    public int getN() {
        return this.n;
    }

    public Vector getLS() {
        return LS;
    }

    public Vector getSS() {
        return SS;
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
            this.n += point.childPtr.getN();
            points.add(index, point);
            if(this.LS != null) this.LS.addToThis(point.childPtr.getLS());
            else this.LS = new Vector(point.childPtr.getLS().x);

            if(this.SS != null) this.SS.addToThis(point.childPtr.getSS().square());
            else this.SS = (new Vector(point.childPtr.getSS().x)).square();
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
        if(this.points.size() < getCapacity()){
            int n = this.getN();
            Vector ls = this.getLS();
            Vector ss = this.getSS();
            add(dataPoint);
            if(ls!= null && ss!=null) setDelta(new CFEntry(this.getN()-n, this.getLS().sub(ls), this.getSS().sub(ss)));
            return true;
        }else 
            return false;
        // meld 2 internal nodes.. 
        // check if the internal nodes isfull.. then split the internal nodes with 
        // choose 2 clusters such that distance between these 2 should be max among all the internal nodes.. 
        // and then trie to meld 2
    }

    public CFNode split(CFNode dataPoint){
        // chose 2 leaves which are farthest from each other
        double maxSofar = -1, dist;
        int maxSofarI = -1, maxSofarJ = -1;

        this.points.add(dataPoint);
        int n = this.points.size();
        CFNode a, b;
        for (int i = 0; i < n; i++) {
            for(int j = i+1; j < n ;j++){
                a = this.points.get(i);
                b = this.points.get(j);
                dist = Distances.getD2(a.getChildPtr().getN(), a.getChildPtr().getLS(), a.getChildPtr().getSS(), b.getChildPtr().getN(), b.getChildPtr().getLS(), b.getChildPtr().getSS());
                if(dist > maxSofar){
                    maxSofar = dist;
                    maxSofarI = i;
                    maxSofarJ = j;
                }
            }
        }

        if(maxSofarI == -1 || maxSofarJ == -1){
            System.err.println("Max sofar is not updated...");
//            System.exit(255);
        }

        CFNode src = this.points.get(maxSofarI);
        CFNode dest = this.points.get(maxSofarJ);

        this.points.set(maxSofarI, null); this.points.set(maxSofarJ, null);

        // iterate again and see which one is nearer.
        InternalNode x = new InternalNode(getCapacity(), null, false);
        InternalNode y = new InternalNode(getCapacity(), null, false);
        double dx = 0 , dy = 0 ;
        x.add(src);
        y.add(dest);
        Vector centroidVector;
        for(CFNode point : points){
            if(point != null){
                centroidVector = Distances.getCentroid(point.getChildPtr().getN(), point.getChildPtr().getLS());
                dx = Distances.getD0(Distances.getCentroid(x.getN(), x.getLS()),centroidVector); // distance from centroid to curr vector
                dy = Distances.getD0(Distances.getCentroid(y.getN(), y.getLS()),centroidVector); // distance from centroid to curr vector
                //TODO min criteria condition is not satisfied here.
                // TODO threshold constraint is not yet implementeed
                if(dx < dy){
                    x.add(point);
                }else{
                    y.add(point);
                }
            }
        }
        // till here we got 2 leafnodes populated with the data 

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

    public void appendDelta(CFEntry delta) {
        if(delta != null){
            this.n += delta.n;
            this.getLS().addToThis(delta.LS);
            this.getSS().addToThis(delta.SS);
        }
    }

    public void addVectorInfo(Vector data) {
        this.n+= 1;
        this.getLS().addToThis(data);
        this.getSS().addToThis(data.square());
    }
}