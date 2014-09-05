package th;

import net.sf.jauvm.Interpreter;
import net.sf.jauvm.SilentObjectCreator;
import net.sf.jauvm.vm.VirtualMachine;

class Test123 implements Runnable {
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

        VirtualMachine vm = VirtualMachine.create(new Test123());
        vm.run(100);
        System.out.println("$$$$$$$$$$$$$$$$$ 100 $$$$$$$$$$$$$$$$$");
        vm.run(100);
        System.out.println("$$$$$$$$$$$$$$$$$ 100 $$$$$$$$$$$$$$$$$");
        vm.run(100);
        System.out.println("$$$$$$$$$$$$$$$$$ 100 $$$$$$$$$$$$$$$$$");
        vm.run(100);
        System.out.println("$$$$$$$$$$$$$$$$$ 100 $$$$$$$$$$$$$$$$$");
        vm.run(100);
        System.out.println("$$$$$$$$$$$$$$$$$ 100 $$$$$$$$$$$$$$$$$");

    }

}
