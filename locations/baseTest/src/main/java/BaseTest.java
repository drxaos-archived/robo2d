import com.jme3.math.FastMath;
import org.apache.commons.io.FileUtils;
import robo2d.game.Game;
import robo2d.game.impl.*;
import robo2d.testbed.RobotTest;
import straightedge.geom.KPoint;

import java.io.File;
import java.io.IOException;

public class BaseTest extends RobotTest {

    @Override
    public Game createGame() {
        Game game = new Game(getWorld(), getDebugDraw());

        PlayerImpl player1 = new PlayerImpl("player1", new KPoint(15, 15), FastMath.PI / 4 * 5);
        game.addPlayer(player1);

        BaseImpl base = new BaseImpl(player1, new KPoint(5, 5), FastMath.PI / 4);
        base.setLaptopName("LP-501");
        base.saveFile("Help.java", "/* Run help */");
        base.saveFile("Communicator.java", "/* Run comm */");
        game.addBase(base);

        RobotImpl robot = new RobotImpl("MR_BS_1", game, player1, new KPoint(15, 5), 0);
        ChassisImpl chassis = new ChassisImpl(300d);
        RadarImpl radar = new RadarImpl(game, 100d);
        ComputerImpl computer = new ComputerImpl(true);
        try {
            computer.saveFile("Boot.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Boot.txt")));
            computer.saveFile("Driver.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Driver.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        robot.addEquipment(chassis);
        robot.addEquipment(radar);
        robot.addEquipment(computer);
        robot.charge(4000);
        game.addRobot(robot);
        game.addGps();
        game.addSatellite(20, 2000);

        return game;
    }

    public static void main(String[] args) {
        new BaseTest().start();
    }
}
