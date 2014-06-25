package robo2d.game.impl;

import org.jbox2d.common.Vec2;
import robo2d.game.Game;
import robo2d.game.api.Radar;
import straightedge.geom.KPoint;

public class RadarImpl implements Radar, EquipmentImpl {

    Game game;
    RobotImpl robot;
    int resolution;
    double scanDistance;

    public RadarImpl(Game game, int resolution, double scanDistance) {
        this.game = game;
        this.resolution = resolution;
        this.scanDistance = scanDistance;
    }

    @Override
    public void setup(RobotImpl robot) {
        this.robot = robot;
    }

    @Override
    public FullScanData fullScan(double accuracy) {
        Vec2 pos = robot.getBox().getPositionVec2();
        double centerX = Math.round(pos.x / accuracy) * accuracy;
        double centerY = Math.round(pos.y / accuracy) * accuracy;
        Type[][] map = new Type[resolution * 2 + 1][resolution * 2 + 1];
        for (int x = -resolution; x <= resolution; x++) {
            for (int y = -resolution; y <= resolution; y++) {
                if (Math.sqrt(x * x + y * y) > resolution) {
                    map[x + resolution][y + resolution] = Type.UNKNOWN;
                } else {
                    double resolveX = centerX + ((Math.random() - 0.5) * 0.4 + x) * accuracy;
                    double resolveY = centerY + ((Math.random() - 0.5) * 0.4 + y) * accuracy;
                    map[x + resolution][y + resolution] = game.resolvePoint(resolveX, resolveY, robot);
                }
            }
        }
        return new FullScanData(map, accuracy, resolution, resolution);
    }

    @Override
    public ScanData scan(double angle) {
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
}
