import com.jme3.math.FastMath;
import org.apache.commons.io.FileUtils;
import robo2d.game.Game;
import robo2d.game.impl.*;
import robo2d.testbed.RobotTest;
import straightedge.geom.KPoint;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BaseTest extends RobotTest {

    @Override
    public Game createGame() {
        try {
            Game game = new Game(getWorld(), getDebugDraw());

            PlayerImpl player1 = new PlayerImpl("player1", new KPoint(5, 5), FastMath.PI / 4 * 5);
            game.addPlayer(player1);

            CampImpl base = new CampImpl(player1, new KPoint(5, 5), FastMath.PI / 4, "BP-501");
//            LaptopImpl laptop = new LaptopImpl("LP-501");
//            laptop.saveFile("README.TXT", "RoboTech Inc. Personal Laptop #LP-501");
//            laptop.saveFile("Cams.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Cams.txt")));
//            laptop.addCamera("BP-501");
//            base.addLaptop(laptop);
            game.addCamp(base);

            ArrayList<Point2D> points = new ArrayList<Point2D>();
            points.clear();
            points.add(new Point2D.Double(-2, -2));
            points.add(new Point2D.Double(-2, 2));
            points.add(new Point2D.Double(0, 4));
            points.add(new Point2D.Double(2, 2));
            points.add(new Point2D.Double(2, -2));
            game.addPlatform(new PlatformImpl(points, 0));

            points.clear();
            points.add(new Point2D.Double(-5, -5));
            points.add(new Point2D.Double(-5, -8));
            points.add(new Point2D.Double(-10, -9));
            game.addWall(new WallImpl(points, 0));

            points.clear();
            points.add(new Point2D.Double(-15, -15));
            points.add(new Point2D.Double(-15, -18));
            points.add(new Point2D.Double(-20, -19));
            game.addWall(new WallImpl(points, 0, "metal"));

            RobotImpl robot = new RobotImpl("MR-BS-01", game, player1, new KPoint(15, 5), 0);
            ChassisImpl chassis = new ChassisImpl(300d);
            RadarImpl radar = new RadarImpl(game, 100d);
            GpsImpl gps = new GpsImpl(game);
            ComputerImpl computer = new ComputerImpl(ComputerImpl.State.ON);
            computer.saveFile("README.TXT", "RoboTech Inc. Military Robot #MR-BS-01");
            computer.saveFile("Boot.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Boot.txt")));
            computer.saveFile("Driver.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Driver.txt")));
            computer.saveFile("Debug.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Debug.txt")));
            robot.addEquipment(chassis);
            robot.addEquipment(radar);
            robot.addEquipment(gps);
            robot.addEquipment(computer);
            robot.charge(4000);
            game.addRobot(robot);

            return game;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new BaseTest().start();
    }
}
