import org.apache.commons.io.FileUtils;
import robo2d.game.Game;
import robo2d.game.impl.*;
import robo2d.game.impl.dynamics.DoorImpl;
import robo2d.game.levels.MapParser;
import robo2d.testbed.RobotTest;

import javax.imageio.ImageIO;
import java.io.File;

public class Training extends RobotTest {

    @Override
    public Game createGame() {
        try {
            Game game = new Game(getWorld(), getDebugDraw());
            MapParser.MapDesc mapDesc = MapParser.parseXml("locations/training/src/main/java/arrival.xml", 0.2f);

            Terminal.images.put("robotech", ImageIO.read(new File("models/ui/robotech.png")));
            Terminal.images.put("agr", ImageIO.read(new File("models/ui/agr.png")));

            MapParser.MapVector player = mapDesc.vectors.get("player");
            PlayerImpl playerImpl = new PlayerImpl("player1", player.point(), player.angle());
            game.addPlayer(playerImpl);

            {
                MapParser.MapVector helicopter = mapDesc.vectors.get("helicopter");
                HelicopterImpl helicopterImpl = new HelicopterImpl(helicopter.point(), helicopter.angle());
                game.addHelicopter(helicopterImpl);
            }
            {
                MapParser.MapPolygon heliPlatform1 = mapDesc.polygons.get("heliPlatform1");
                game.addPlatform(new PlatformImpl(heliPlatform1.points));

                MapParser.MapPolygon heliPlatform2 = mapDesc.polygons.get("heliPlatform2");
                game.addPlatform(new PlatformImpl(heliPlatform2.points));

                MapParser.MapVector cpuHelipad = mapDesc.vectors.get("cpuHelipad");
                ControllerImpl cpuHelipadImpl = new ControllerImpl(playerImpl, cpuHelipad.point(), cpuHelipad.angle(), "HELIPAD-CONTROL-1");
                CpuImpl cpu = new CpuImpl("CPU1", CpuImpl.State.OFF);
                cpu.saveFile("README.TXT", "RoboTech Inc. Controller unit 01-test");
                cpu.setStateString("console/video", "robotech");
                cpu.setStateString("console/text", "Initialization...");
                cpuHelipadImpl.addCpu(cpu);
                game.addController(cpuHelipadImpl);

                MapParser.MapPolygon doorHelipad = mapDesc.polygons.get("doorHelipad");
                DoorImpl doorHelipadImpl = new DoorImpl(doorHelipad.points, null, 2);
                doorHelipadImpl.setController(cpuHelipadImpl, "door-helipad/control", "open", "close");
                game.addDoor(doorHelipadImpl);
            }
            {
                MapParser.MapPolygon wall = mapDesc.polygons.get("wall");
                game.addWall(new WallImpl(wall.points, "metal"));

                MapParser.MapPolygon platform = mapDesc.polygons.get("platform");
                game.addPlatform(new PlatformImpl(platform.points));

                String[] camps = {"camp1", "camp2", "camp3", "camp4"};
                for (String camp : camps) {
                    MapParser.MapVector campVector = mapDesc.vectors.get(camp);
                    game.addCamp(new CampImpl(campVector.point(), campVector.angle()));
                }
            }

            {
                MapParser.MapVector cpuTraining = mapDesc.vectors.get("cpuTraining");
                ControllerImpl cpuTrainingImpl = new ControllerImpl(playerImpl, cpuTraining.point(), cpuTraining.angle(), "TRAINING-CONTROL-1");
                CpuImpl cpu = new CpuImpl("CPU1", CpuImpl.State.OFF);
                cpu.saveFile("README.TXT", "RoboTech Inc. Controller unit 01-test");
                cpuTrainingImpl.addCpu(cpu);
                game.addController(cpuTrainingImpl);

                MapParser.MapPolygon doorTraining = mapDesc.polygons.get("doorTraining");
                DoorImpl doorTrainingImpl = new DoorImpl(doorTraining.points, null, 2);
                doorTrainingImpl.setController(cpuTrainingImpl, "door-training/control", "open", "close");
                game.addDoor(doorTrainingImpl);
            }

            {
                MapParser.MapPolygon wallDepot = mapDesc.polygons.get("wallDepot");
                game.addWall(new WallImpl(wallDepot.points, "metal"));

                MapParser.MapPolygon doorDepot = mapDesc.polygons.get("doorDepot");
                DoorImpl doorDepotImpl = new DoorImpl(doorDepot.points, null, 2);
                //doorImpl.setController(controllerImpl, "door-helipad/control", "open", "close");
                game.addDoor(doorDepotImpl);
            }
            {
                String[] rocks = {"rock1", "rock2", "rock3", "rockSouth", "rockNorth", "rockEast", "rockWest"};
                for (String rock : rocks) {
                    MapParser.MapPolygon rockPolygon = mapDesc.polygons.get(rock);
                    game.addWall(new WallImpl(rockPolygon.points, "rock"));
                }
            }
            {
                MapParser.MapVector robot = mapDesc.vectors.get("robot");
                RobotImpl robotImpl = new RobotImpl("MR-BS-01", game, playerImpl, robot.point(), robot.angle());
                ChassisImpl chassis = new ChassisImpl(300d);
                RadarImpl radar = new RadarImpl(game, 100d);
                GpsImpl gps = new GpsImpl(game);
                ComputerImpl computer = new ComputerImpl(ComputerImpl.State.OFF);
                computer.saveFile("README.TXT", "RoboTech Inc. Military Robot #MR-BS-01");
                computer.saveFile("Boot.java", FileUtils.readFileToString(new File("locations/training/src/main/java/Boot.txt")));
                computer.saveFile("Driver.java", FileUtils.readFileToString(new File("locations/training/src/main/java/Driver.txt")));
                computer.saveFile("Debug.java", FileUtils.readFileToString(new File("locations/training/src/main/java/Debug.txt")));
                robotImpl.addEquipment(chassis);
                robotImpl.addEquipment(radar);
                robotImpl.addEquipment(gps);
                robotImpl.addEquipment(computer);
                robotImpl.charge(4000);
                game.addRobot(robotImpl);
            }

            return game;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Training().start();
    }
}
