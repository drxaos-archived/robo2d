import com.robotech.military.internal.*;
import com.robotech.military.api.*;

public class Debug {

    public static void debug() {
        LocalConnection localConnection = new LocalConnection("debug");
        Robot robot = new Robot(localConnection);
        try {
            Program boot = (Program) Class.forName("Boot").newInstance();
            boot.run(robot);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            localConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
