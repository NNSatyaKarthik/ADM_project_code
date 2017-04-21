/**
 * Created by nagasaty on 4/20/17.
 */
abstract class A{
    abstract void print();
}

class C extends A{

    @Override
    void print() {
        System.out.println("C from A");
    }
}

class B extends A{

    @Override
    void print() {
        System.out.println("B from A");
    }
}


public class Test {
    public static void main(String[] args) {
        A ref = null;
        ref = new C();
        System.out.println(ref instanceof A);
        System.out.println(ref instanceof B);
        ref = new B();
        System.out.println(ref instanceof A);
        System.out.println(ref instanceof B);
    }
}
