package th;

import net.sf.jauvm.vm.VirtualMachine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;


class A implements Serializable {
    public A() {
        Test123.out += "b";
    }
}

class B extends A implements Serializable {
    public B() {
        Test123.out += "a";
    }
}

class Test123 implements Runnable, Serializable {
    public static String out = "S;";

    @Override
    public void run() {
        A a = new A();
        Test123.out += ";";
        B b = new B();
    }
}


public class Jau {

    public static void main(String[] args) throws Throwable {

        File file = new File("Test123.jau");
        VirtualMachine vm;
        if (file.exists()) {
            FileInputStream f = new FileInputStream(file);
            vm = VirtualMachine.create(f);
            f.close();
        } else {
            vm = VirtualMachine.create(new Test123());
        }

        for (int i = 0; i < 100; i++) {
            vm.run(1);
            FileOutputStream f = new FileOutputStream(file);
            vm.save(f);
            f.close();
            System.out.println("saved " + i);
        }
    }

}
