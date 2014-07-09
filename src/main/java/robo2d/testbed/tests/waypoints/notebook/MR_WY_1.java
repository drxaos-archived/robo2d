package robo2d.testbed.tests.waypoints.notebook;

import com.robotech.military.api.Chassis;
import com.robotech.military.api.Radar;
import com.robotech.military.os.RobotProgram;

import java.awt.geom.Point2D;
import java.util.ArrayList;


public class MR_WY_1 extends RobotProgram {

    Chassis chassis;
    Radar radar;

    @Override
    public void program() {
        chassis = robot.getEquipment(Chassis.class);
        radar = robot.getEquipment(Radar.class);

        if (chassis == null || radar == null) {
            robot.debug("fail");
            return;
        }

        ArrayList<Point2D> way = new ArrayList<Point2D>();
        way.add(new Point2D.Float(0, -10));
        way.add(new Point2D.Float(12, -5));
        way.add(new Point2D.Float(12, 0));
        way.add(new Point2D.Float(0, 0));
        way.add(new Point2D.Float(-12, 0));
        way.add(new Point2D.Float(-12, 5));
        way.add(new Point2D.Float(0, 10));
        way.add(new Point2D.Float(15, 5));
//        way.add(new KPoint(15, -5));
        way.add(new Point2D.Float(0, -10));

        for (Point2D wayPoint : way) {
            while (!driver.moveSmooth(wayPoint, 4000)) {
                driver.stop();
                Point2D failPoint = radar.getPosition();
                robot.debug(wayPoint);
                robot.debug("Please drag me to that point!");
                while (true) {
                    // save energy
                    Point2D newPoint = radar.getPosition();
                    if (Utils.distance(newPoint, failPoint) > 0.2) {
                        break;
                    }
                    robot.waitForStep();
                }
            }
            robot.debug(null);

            long waitUntil = robot.getTime() + 100;
            double angle = Math.PI / 2;
            int scans = 0;
            int walls = 0;
            while (robot.getTime() < waitUntil) {
                Radar.LocatorScanData scan = radar.locate(angle += 0.001);
                scans++;
                if (scan.pixel == Radar.Type.WALL) {
                    walls++;
                }
            }
            System.out.println("Time: 100ms, Scans: " + scans + ", walls: " + walls);

            if (Utils.distance(radar.getPosition(), new Point2D.Float(0, 0)) < 0.3) {
                driver.stop();
                radar.satelliteRequest(radar.getPosition(), 0.5);
                waitUntil = robot.getTime() + 10000;
                while (radar.getSatelliteResponse() == null && robot.getTime() < waitUntil) {
                    waitForChanges();
                }
                Radar.SatelliteScanData response = radar.getSatelliteResponse();
                if (response != null) {
                    for (int i = 0; i < response.image.length; i++) {
                        for (int j = 0; j < response.image[i].length; j++) {
                            System.out.print(response.image[j][i].name().charAt(0));
                        }
                        System.out.print("\n");
                    }
                }
            }
        }
    }
}
