import robo2d.game.Game;
import robo2d.game.impl.PlatformImpl;
import robo2d.game.impl.PlayerImpl;
import robo2d.game.impl.WallImpl;
import robo2d.game.impl.dynamics.DoorImpl;
import robo2d.game.levels.MapParser;
import robo2d.testbed.RobotTest;

public class Training extends RobotTest {

    @Override
    public Game createGame() {
        try {
            Game game = new Game(getWorld(), getDebugDraw());
            MapParser.MapDesc mapDesc = MapParser.parseXml("locations/training/src/main/java/arrival.xml", 0.2f);

            MapParser.MapVector player = mapDesc.vectors.get("player");
            PlayerImpl playerImpl = new PlayerImpl("player1", player.point(), player.angle());
            game.addPlayer(playerImpl);

//            MapParser.MapObject camp = mapDesc.objects.get("camp");
//            KPoint center = CampImpl.polygon.getCenter();
//            CampImpl campImpl = new CampImpl(new KPoint(camp.x, camp.y), camp.r);
//            game.addCamp(campImpl);
//
//            MapParser.MapObject helicopter = mapDesc.objects.get("helicopter");
//            HelicopterImpl helicopterImpl = new HelicopterImpl(new KPoint(helicopter.x, helicopter.y), helicopter.r);
//            game.addHelicopter(helicopterImpl);
//
//            MapParser.MapObject controller = mapDesc.objects.get("cpu");
//            ControllerImpl controllerImpl = new ControllerImpl(playerImpl, new KPoint(controller.x, controller.y), controller.r, "TEST-CONTROL-1");
//            CpuImpl cpu = new CpuImpl("CPU1", CpuImpl.State.OFF);
//            cpu.saveFile("README.TXT", "RoboTech Inc. Controller unit 01-test");
//            controllerImpl.addCpu(cpu);
//            game.addController(controllerImpl);
//
            MapParser.MapPolygon platform = mapDesc.polygons.get("platform");
            game.addPlatform(new PlatformImpl(platform.points));

            MapParser.MapPolygon wall = mapDesc.polygons.get("wall");
            game.addWall(new WallImpl(wall.points, "metal"));

            MapParser.MapPolygon doorTraining = mapDesc.polygons.get("door-training");
            DoorImpl doorTrainingImpl = new DoorImpl(doorTraining.points, null, 2);
            //doorImpl.setController(controllerImpl, "door-helipad/control", "open", "close");
            game.addDoor(doorTrainingImpl);

            MapParser.MapPolygon doorHelipad = mapDesc.polygons.get("door-helipad");
            DoorImpl doorHelipadImpl = new DoorImpl(doorHelipad.points, null, 2);
            //doorImpl.setController(controllerImpl, "door-helipad/control", "open", "close");
            game.addDoor(doorHelipadImpl);

//            MapParser.MapObject robot = mapDesc.objects.get("robot");
//            RobotImpl robotImpl = new RobotImpl("MR-BS-01", game, playerImpl, new KPoint(robot.x, robot.y), robot.r);
//            ChassisImpl chassis = new ChassisImpl(300d);
//            RadarImpl radar = new RadarImpl(game, 100d);
//            GpsImpl gps = new GpsImpl(game);
//            ComputerImpl computer = new ComputerImpl(ComputerImpl.State.OFF);
//            computer.saveFile("README.TXT", "RoboTech Inc. Military Robot #MR-BS-01");
//            computer.saveFile("Boot.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Boot.txt")));
//            computer.saveFile("Driver.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Driver.txt")));
//            computer.saveFile("Debug.java", FileUtils.readFileToString(new File("locations/baseTest/src/main/java/Debug.txt")));
//            robotImpl.addEquipment(chassis);
//            robotImpl.addEquipment(radar);
//            robotImpl.addEquipment(gps);
//            robotImpl.addEquipment(computer);
//            robotImpl.charge(4000);
//            game.addRobot(robotImpl);

            return game;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Training().start();
    }
}
