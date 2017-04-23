package adm.birch;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by nagasaty on 4/20/17.
 */
abstract class Node<T>{
    private int capacity;
    private List<T> data;
    private boolean isSorted;
    private boolean isInternal;
    private int minElements;
    private Node parentNodePtr;
    CFNode parentPtr;
    CFEntry delta; 
    
    public Node(int capacity, Node parentNodePtr,  boolean isLeaf) {
        this(capacity, (int)Math.ceil(capacity/2), parentNodePtr);
        if(isLeaf) {
            this.isSorted = false;
            this.isInternal = false;
        }else {
            this.isSorted = true;
            this.isInternal = true;
        }
    }

    private Node(int capacity, int minElements, Node parentNodePtr){
        this.capacity = capacity;
        this.data = new ArrayList<T>();
        this.minElements = minElements;
        this.parentNodePtr = parentNodePtr;
    }
    
    public boolean isFull(){ return this.data.size() == this.capacity;}

    public boolean isEmpty() { return this.data.size() == 0; }

    public boolean isConstraintSatisfied(){ return this.data.size() >= this.minElements; }

    public abstract boolean insert(T dataPoint);

    public List<T> getData() {
        return this.data;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isSorted() {
        return isSorted;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public int getMinElements() {
        return minElements;
    }

    //called only for root elements
    public void setMinElements(int minElements) {
        this.minElements = minElements;
    }
    
    abstract public Vector getLS();
    abstract public Vector getSS();
    abstract public int getN();
    
    public Node getParentNodePtr() {
        return parentNodePtr;
    }

    public void setParentNodePtr(Node parentNodePtr) {
        this.parentNodePtr = parentNodePtr;
    }

    public CFNode getParentPtr() {
        return parentPtr;
    }

    public void setParentPtr(CFNode parentPtr) {
        this.parentPtr = parentPtr;
    }
    
    public void setDelta(CFEntry delta){
        this.delta = delta;
    }
    
    public CFEntry getDelta(){
        return this.delta;
    }

    public void metaSync(CFEntry delta, Stack<InternalNode> path){
        for(int i = 0 ; i < path.size(); i++){
            InternalNode node = path.get(i);
            node.appendDelta(delta);
        }
    }
}
