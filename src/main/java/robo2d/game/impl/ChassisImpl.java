package robo2d.game.impl;

import org.jbox2d.common.Vec2;
import straightedge.geom.KPoint;

import java.util.HashMap;
import java.util.Map;

public class ChassisImpl implements EquipmentImpl, HasEffects {

    Host robot;

    double leftAccel, rightAccel, maxAccel;
    private Map<KPoint, Vec2> effectsMap;
    private final Vec2 LEFT_ENGINE, RIGHT_ENGINE;

    public ChassisImpl(double maxAccel) {
        this.maxAccel = maxAccel;

        LEFT_ENGINE = new Vec2(0, 0);
        RIGHT_ENGINE = new Vec2(0, 0);
        effectsMap = new HashMap<KPoint, Vec2>();
        effectsMap.put(new KPoint(1, 0), RIGHT_ENGINE);
        effectsMap.put(new KPoint(-1, 0), LEFT_ENGINE);
    }

    private void setLeftAcceleration(Double percent) {
        percent = Math.max(Math.min(percent, 100), -100);
        leftAccel = maxAccel * percent / 100;
    }

    private void setRightAcceleration(Double percent) {
        percent = Math.max(Math.min(percent, 100), -100);
        rightAccel = maxAccel * percent / 100;
    }

    public Boolean isWorking() {
        return Math.max(Math.abs(rightAccel), Math.abs(leftAccel)) > 0.01;
    }

    @Override
    public Map<KPoint, Vec2> getEffects() {
        setLeftAcceleration(robot.getComputer().getStateDouble("chassis/left"));
        setRightAcceleration(robot.getComputer().getStateDouble("chassis/right"));

        if (!robot.consumeEnergy((Math.abs(leftAccel) + Math.abs(rightAccel)) / maxAccel / 100)) {
            leftAccel = rightAccel = 0;
        }
        LEFT_ENGINE.set(0, (float) leftAccel);
        RIGHT_ENGINE.set(0, (float) rightAccel);
        return effectsMap;
    }

    @Override
    public void setup(Host robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        robot.getComputer().setStateString("chassis/ready", "true");
    }

    @Override
    public void update() {
        robot.getComputer().setStateString("chassis/working", isWorking() ? "true" : "false");
    }
}
