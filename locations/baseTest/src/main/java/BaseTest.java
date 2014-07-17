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
        try {
            Game game = new Game(getWorld(), getDebugDraw());

            PlayerImpl player1 = new PlayerImpl("player1", new KPoint(15, 15), FastMath.PI / 4 * 5);
            game.addPlayer(player1);

            BaseImpl base = new BaseImpl(player1, new KPoint(5, 5), FastMath.PI / 4);
            base.setLaptopName("LP-501");
            base.saveFile("README.TXT", "RoboTech Inc. Personal Laptop #LP-501");
            base.saveFile("Help.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Help.txt")));
            base.saveFile("CommLink.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/CommLink.txt")));
            base.saveFile("messages.txt", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/messages.txt")));
            game.addBase(base);

            RobotImpl robot = new RobotImpl("MR-BS-01", game, player1, new KPoint(15, 5), 0);
            ChassisImpl chassis = new ChassisImpl(300d);
            RadarImpl radar = new RadarImpl(game, 100d);
            ComputerImpl computer = new ComputerImpl(true);
            computer.saveFile("README.TXT", "RoboTech Inc. Military Robot #MR-BS-01");
            computer.saveFile("Boot.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Boot.txt")));
            computer.saveFile("Driver.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Driver.txt")));
            robot.addEquipment(chassis);
            robot.addEquipment(radar);
            robot.addEquipment(computer);
            robot.charge(4000);
            game.addRobot(robot);
            game.addGps();
            game.addSatellite(20, 2000);

            return game;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new BaseTest().start();
    }
}
