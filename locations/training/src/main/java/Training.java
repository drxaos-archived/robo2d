import org.apache.commons.io.FileUtils;
import robo2d.game.Game;
import robo2d.game.impl.*;
import robo2d.game.levels.MapParser;
import robo2d.testbed.RobotTest;
import straightedge.geom.KPoint;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LevelTest extends RobotTest {

    @Override
    public Game createGame() {
        try {
            Game game = new Game(getWorld(), getDebugDraw());
            MapParser.MapDesc mapDesc = MapParser.parseXml("locations/levelTest/src/main/java/arrival.xml", 0.1f);

            MapParser.MapObject player = mapDesc.objects.get("player");
            PlayerImpl playerImpl = new PlayerImpl("player1", new KPoint(player.x, player.y), player.r);
            game.addPlayer(playerImpl);

            MapParser.MapObject camp = mapDesc.objects.get("camp");
            KPoint center = CampImpl.polygon.getCenter();
            CampImpl campImpl = new CampImpl(new KPoint(camp.x, camp.y), camp.r);
            game.addCamp(campImpl);

            MapParser.MapObject helicopter = mapDesc.objects.get("helicopter");
            HelicopterImpl helicopterImpl = new HelicopterImpl(new KPoint(helicopter.x, helicopter.y), helicopter.r);
            game.addHelicopter(helicopterImpl);

            MapParser.MapObject controller = mapDesc.objects.get("cpu");
            ControllerImpl controllerImpl = new ControllerImpl(playerImpl, new KPoint(controller.x, controller.y), controller.r, "TEST-CONTROL-1");
            CpuImpl cpu = new CpuImpl("CPU1", CpuImpl.State.OFF);
            cpu.saveFile("README.TXT", "RoboTech Inc. Controller unit 01-test");
            controllerImpl.addCpu(cpu);
            game.addController(controllerImpl);

            MapParser.MapPolygon platform = mapDesc.polygons.get("platform");
            game.addPlatform(new PlatformImpl(platform.points));

            MapParser.MapPolygon wall = mapDesc.polygons.get("wall1");
            game.addWall(new WallImpl(wall.points, "metal"));

            MapParser.MapPolygon wall1 = mapDesc.polygons.get("wall2");
            game.addWall(new WallImpl(wall1.points, "metal"));

            MapParser.MapObject robot = mapDesc.objects.get("robot");
            RobotImpl robotImpl = new RobotImpl("MR-BS-01", game, playerImpl, new KPoint(robot.x, robot.y), robot.r);
            ChassisImpl chassis = new ChassisImpl(300d);
            RadarImpl radar = new RadarImpl(game, 100d);
            GpsImpl gps = new GpsImpl(game);
            ComputerImpl computer = new ComputerImpl(ComputerImpl.State.OFF);
            computer.saveFile("README.TXT", "RoboTech Inc. Military Robot #MR-BS-01");
            computer.saveFile("Boot.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Boot.txt")));
            computer.saveFile("Driver.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Driver.txt")));
            computer.saveFile("Debug.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Debug.txt")));
            robotImpl.addEquipment(chassis);
            robotImpl.addEquipment(radar);
            robotImpl.addEquipment(gps);
            robotImpl.addEquipment(computer);
            robotImpl.charge(4000);
            game.addRobot(robotImpl);

            return game;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new LevelTest().start();
    }
}
