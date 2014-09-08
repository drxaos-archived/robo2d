package th;

class A {
    public A() {
        Test123.out += "b";
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
    }

    public static void main(String[] a) {
        new Test123().run();
    }
}