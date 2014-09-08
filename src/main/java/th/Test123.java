package th;

class A {
    double[][] map;

    public A() {
        Test123.out += "b";
        map = new double[100][100];
        map[55][66] = 77;
    }
}

class B extends A {
    public B() {
        Test123.out += "a";
    }
}

class Api {
    public static void println(String str) {
        System.out.println(str);
    }
}

public class Test123 implements Runnable {
    public static String out = "S;";
    private String zzz = "";

    public Test123() {
        Api.println("asdasdasd");
    }

    @Override
    public void run() {
        Api.println("start");
        zzz = "qwerty";
        A a = new A();
        Test123.out += ";";
        B b = new B();
        Api.println(out);
        a = null;
        b = null;
        Api.println("end");
    }

    public static void main(String[] a) {
        new Test123().run();
    }
}