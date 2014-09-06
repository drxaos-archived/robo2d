package th;

import net.sf.jauvm.vm.VirtualMachine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;

class Test123 implements Runnable, Serializable {
    @Override
    public void run() {
        long i = 0;
        while (true) {
            System.out.println("turn " + i++);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

        vm.run(50);
        System.out.println("$$$$$$$$$$$$$$$$$ 100 $$$$$$$$$$$$$$$$$");

        FileOutputStream f = new FileOutputStream(file);
        vm.save(f);
        f.close();
    }

}
