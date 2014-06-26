package robo2d.game.impl;

import org.jbox2d.common.Vec2;
import robo2d.game.Game;
import robo2d.game.api.Radar;
import straightedge.geom.KPoint;

public class RadarImpl implements Radar, EquipmentImpl, SatelliteScanner {

    Game game;
    RobotImpl robot;
    double scanDistance;
    int satelliteResolution;

    SatelliteScanData satelliteScanData;

    public RadarImpl(Game game, int satelliteResolution, double scanDistance) {
        this.game = game;
        this.scanDistance = scanDistance;
        this.satelliteResolution = satelliteResolution;
    }

    @Override
    public void setup(RobotImpl robot) {
        this.robot = robot;
    }

    @Override
    public void satelliteRequest(KPoint center, double accuracy) {
        if (!robot.consumeEnergy(0.5)) {
            return;
        }
        game.satelliteRequest(this, new Vec2((float) center.getX(), (float) center.getY()), accuracy, satelliteResolution);
        satelliteScanData = null;
    }

    @Override
    public SatelliteScanData getSatelliteResponse() {
        return satelliteScanData;
    }

    @Override
    public void clearSatelliteResponse() {
        satelliteScanData = null;
    }

    @Override
    public LocatorScanData locate(double angle) {
        if (!robot.consumeEnergy(0.2)) {
            return null;
        }
        return game.resolveDirection(angle, scanDistance, robot);
    }

    @Override
    public Double getAngle() {
        return robot.box.getAngle();
    }

    @Override
    public KPoint getPosition() {
        return robot.box.getPosition();
    }

    @Override
    public RobotImpl getRobot() {
        return robot;
    }

    @Override
    public void setSatResponse(SatelliteScanData response) {
        satelliteScanData = response;
    }
}
