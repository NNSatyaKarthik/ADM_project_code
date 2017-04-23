package adm.birch;

/**
 * Created by nagasaty on 4/20/17.
 */
abstract class A{
    abstract void print();
}

class C extends A{

    @Override
    void print() {
        System.out.println("adm.birch.C from adm.birch.A");
    }
}

class B extends A{

    @Override
    void print() {
        System.out.println("adm.birch.B from adm.birch.A");
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
