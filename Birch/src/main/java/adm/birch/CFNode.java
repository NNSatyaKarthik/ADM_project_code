package adm.birch;

import java.util.ArrayList;

/**
 * Created by nagasaty on 4/20/17.
 */
public class CFNode {
    Node childPtr;
    IntegerObj n; 
    Vector LS;
    Vector SS;
    
    public CFNode(Node childPtr){
        this.childPtr = childPtr;
        this.n = this.childPtr.getN();
        this.LS = this.childPtr.getLS();
        this.SS = this.childPtr.getSS();
    }
    
    
//    public CFNode(Node childPtr, CFEntry entry) {
//        this.childPtr = childPtr;
////        this.entry = new CFEntry(entry.n, entry.LS, entry.SS);
//        this.entry = entry;
//    }

//    public CFNode(LeafNode childPtr, int n, Vector ls, Vector ss) {
//        this.childPtr = childPtr;
////        this.entry = 
////    }

//    public void setChildPtr(Node childPtr) {
//        this.childPtr = childPtr;
//    }
//
//    public void setEntry(CFEntry entry) {
//        this.entry = entry;
//    }

    public Node getChildPtr() {
        return this.childPtr;
    }

//    public CFEntry getEntry() {
//        return this.entry;
//    }
    
    @Override
    public String toString() {
        return String.format("(%s)--(%d, %s, %s)", childPtr, n.value, LS, SS);
    }

//    public static CFNode getNewObj(InternalNode pathNode) {
//        IntegerObj n  = pathNode.getN();
//        Vector ls = pathNode.getLS();
//        Vector ss =pathNode.getSS();
//    }

//    public void update(CFEntry delta) {
//        this.entry.update(delta.n, delta.LS , delta.SS);
//    }

//    public void addVectorInfoToEntry(Vector x) {
//        this.entry.n+= 1;
//        this.entry.LS.addToThis(x);
//        this.entry.SS.addToThis(x.square());
//        
//    }
}
