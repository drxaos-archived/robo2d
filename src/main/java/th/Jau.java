package th;

import net.sf.jauvm.Interpreter;
import net.sf.jauvm.interpretable;

class Test123 implements Runnable {
    @Override
    @interpretable
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

    public static void main(String[] args) {

        new Interpreter(new Test123()).run();

    }

}
