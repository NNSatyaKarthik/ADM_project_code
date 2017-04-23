package adm.birch;

/**
 * Created by nagasaty on 4/20/17.
 */
public class CFNode {
    Node childPtr;
    CFEntry entry;

    public CFNode(Node childPtr, CFEntry entry) {
        this.childPtr = childPtr;
        this.entry = entry;
    }

    public void setChildPtr(Node childPtr) {
        this.childPtr = childPtr;
    }

    public void setEntry(CFEntry entry) {
        this.entry = entry;
    }

    public Node getChildPtr() {
        return childPtr;
    }

    public CFEntry getEntry() {
        return entry;
    }
    
    @Override
    public String toString() {
        return String.format("(%s-->%s)", entry, childPtr);
    }

    public void update(CFEntry delta) {
        this.entry.update(delta.n, delta.LS , delta.SS);
    }

    public void addVectorInfoToEntry(Vector data) {
        this.entry.n+= 1;
        this.entry.LS.addToThis(data);
        this.entry.SS.addToThis(data.square());
        
    }
}
