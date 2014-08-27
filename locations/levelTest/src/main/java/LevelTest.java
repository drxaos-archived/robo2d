import org.apache.commons.io.FileUtils;
import robo2d.game.Game;
import robo2d.game.impl.*;
import robo2d.game.impl.dynamics.DoorImpl;
import robo2d.game.levels.MapParser;
import robo2d.testbed.RobotTest;
import straightedge.geom.KPoint;

import java.io.File;
import java.io.IOException;
//
//class GraphingData extends JPanel {
//
//    MapParser.MapDesc mapDesc;
//
//    GraphingData(MapParser.MapDesc mapDesc) {
//        this.mapDesc = mapDesc;
//    }
//
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2 = (Graphics2D) g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
//        int w = getWidth();
//        int h = getHeight();
//
//        g2.setPaint(Color.BLACK);
//        for (MapParser.MapPolygon polygon : mapDesc.polygons.values()) {
//            int[] x = new int[polygon.points.size()], y = new int[polygon.points.size()];
//            for (int i = 0; i < polygon.points.size(); i++) {
//                x[i] = (int) (polygon.points.get(i).getX() * 10 + 100);
//                y[i] = (int) (polygon.points.get(i).getY() * -10 + 100);
//            }
//            g2.drawPolygon(x, y, x.length);
//        }
//        for (MapParser.MapObject mapObject : mapDesc.objects.values()) {
//            g2.drawLine((int) (mapObject.x * 10 + 100), (int) (mapObject.y * -10 + 100), (int) (mapObject.x * 10 + 100 + Math.cos(mapObject.r) * 20), (int) (mapObject.y * -10 + 100 - Math.sin(mapObject.r) * 20));
//            g2.drawOval((int) (mapObject.x * 10 + 100 - 10), (int) (mapObject.y * -10 + 100 - 10), 20, 20);
//        }
//    }
//
//}

public class LevelTest extends RobotTest {

    @Override
    public Game createGame() {
        try {
            Game game = new Game(getWorld(), getDebugDraw());
            MapParser.MapDesc mapDesc = MapParser.parseXml("locations/levelTest/src/main/java/level.xml", 0.1f);

//            JFrame f = new JFrame();
//            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            f.add(new GraphingData(mapDesc));
//            f.setSize(1000, 1000);
//            f.setLocation(5, 5);
//            f.setVisible(true);

            MapParser.MapVector player = mapDesc.vectors.get("player");
            PlayerImpl playerImpl = new PlayerImpl("player1", player.point(), player.angle());
            game.addPlayer(playerImpl);

            MapParser.MapVector camp = mapDesc.vectors.get("camp");
            CampImpl campImpl = new CampImpl(camp.point(), camp.angle());
            game.addCamp(campImpl);

            MapParser.MapVector helicopter = mapDesc.vectors.get("helicopter");
            HelicopterImpl helicopterImpl = new HelicopterImpl(helicopter.point(), helicopter.angle());
            game.addHelicopter(helicopterImpl);

            MapParser.MapVector controller = mapDesc.vectors.get("cpu");
            ControllerImpl controllerImpl = new ControllerImpl(null, controller.point(), controller.angle(), "TEST-CONTROL-1");
            CpuImpl cpu = new CpuImpl("CPU1", CpuImpl.State.OFF);
            cpu.saveFile("README.TXT", "RoboTech Inc. Controller unit 01-test");
            cpu.saveFile("Debug.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Debug.txt")));
            cpu.saveFile("Boot.java", FileUtils.readFileToString(new File("locations/levelTest/src/main/java/BootCpu.txt")));
            controllerImpl.addCpu(cpu);
            game.addController(controllerImpl);

            MapParser.MapPolygon door = mapDesc.polygons.get("door");
            DoorImpl doorImpl = new DoorImpl(door.points);
            doorImpl.setController(controllerImpl, "door-helipad/control", "open", "close");
            game.addDoor(doorImpl);

            MapParser.MapPolygon platform = mapDesc.polygons.get("platform");
            game.addPlatform(new PlatformImpl(platform.points));

            MapParser.MapPolygon wall = mapDesc.polygons.get("wall1");
            game.addWall(new WallImpl(wall.points, "metal"));

            MapParser.MapPolygon wall1 = mapDesc.polygons.get("wall2");
            game.addWall(new WallImpl(wall1.points, "metal"));

            MapParser.MapVector robot = mapDesc.vectors.get("robot");
            RobotImpl robotImpl = new RobotImpl("MR-BS-01", game, playerImpl, robot.point(), robot.angle());
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
