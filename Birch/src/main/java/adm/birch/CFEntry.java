package adm.birch;

/**
 * Created by nagasaty on 4/20/17.
 */
public class CFEntry {
    int n;
    Vector LS; 
    Vector SS;

    public CFEntry(int n, Vector LS, Vector SS) {
        this.n = n;
        this.LS = LS;
        this.SS = SS;
    }
    
    @Override
    public String toString() {
        return String.format("(%d, %s, %s)", n, LS, SS);
    }

    public void update(int n, Vector ls, Vector ss) {
        this.n += n;
        this.LS.addToThis(ls);
        this.SS.addToThis(ss);
    }
}
