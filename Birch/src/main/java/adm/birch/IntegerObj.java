package adm.birch;

/**
 * Created by nagasaty on 4/24/17.
 */
class IntegerObj {
    int value;
    IntegerObj(int val) {
        this.value = val;
    }
    public void addToThis(int n){
        this.value += n;
    }

    public IntegerObj sub(IntegerObj n) {
        return new IntegerObj(this.value-n.value);
    }
    
    public void subFromThis(int n){
        this.value = this.value-n;
    }

    public Integer sub(Integer n) {
        return (this.value-n);
    }

    public void setValues(IntegerObj values) {
        this.value = values.value;
    }
}