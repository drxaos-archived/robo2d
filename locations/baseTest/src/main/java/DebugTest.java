import com.robotech.military.api.Robot;
import robo2d.game.impl.proxy.Remote;

public class DebugTest {
    public static void main(String[] args) {
        Remote remote = new Remote();
        Robot r = remote.getRobotForDebug();
        Boot1 boot1 = new Boot1();
        boot1.run(r);
    }
}
