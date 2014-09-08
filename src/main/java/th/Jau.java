package th;

import net.sf.jauvm.vm.GlobalCodeCache;
import net.sf.jauvm.vm.VirtualMachine;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
    private String zzz = "";

    @Override
    public void run() {
        zzz = "qwerty";
        A a = new A();
        Test123.out += ";";
        B b = new B();
        System.out.println(Test123.out);
    }
}


public class Jau {

    public static void main(String[] args) throws Throwable {

        GlobalCodeCache.setCodeLoader(new GlobalCodeCache.CodeLoader() {
            @Override
            public InputStream getBytecodeStream(Class<?> cls) {
                if (cls.getCanonicalName().startsWith("java.lang.")) {
                    return null;
                }
                if (cls.getCanonicalName().startsWith("java.io.")) {
                    return null;
                }
                return super.getBytecodeStream(cls);
            }
        });

        File file = new File("Test123.jau");
        VirtualMachine vm;
        if (file.exists()) {
            FileInputStream f = new FileInputStream(file);
            vm = VirtualMachine.create(f);
            f.close();
        } else {
            vm = VirtualMachine.create(new Test123());
        }

        for (int i = 0; i < 1000000; i++) {
            if (vm.run(1)) {
                break;
            }
//            try {
//                FileOutputStream f = new FileOutputStream(file);
//                vm.save(f);
//                f.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            System.out.println("saved " + i);
        }
    }

}
