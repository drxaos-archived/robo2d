package robo2d.game.impl;

import robo2d.game.api.*;
import robo2d.game.api.map.Obj;
import robo2d.game.box2d.Box;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.RobotBox;
import straightedge.geom.KPoint;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

public class RobotImpl implements Robot, Obj, Physical {

    RobotBox box;
    PlayerImpl owner;
    ChassisImpl chassis;
    Map<String, String> init;
    Double energy;

    public RobotImpl(PlayerImpl owner, ChassisImpl chassis, Map<String, String> init, Double energy, KPoint position, double angle) {
        this.owner = owner;
        this.chassis = chassis;
        this.init = init;
        this.energy = energy;

        box = new RobotBox(position, angle);
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public Type getType() {
        return Type.BOT;
    }

    @Override
    public List<Point2D> getVertices() {
        return null;
    }

    @Override
    public Radar getRadar() {
        return null;
    }

    @Override
    public Radio getRadio() {
        return null;
    }

    @Override
    public Chassis getChassis() {
        return chassis;
    }

    @Override
    public Turret getTurret() {
        return null;
    }

    @Override
    public Map<String, String> getInitializer() {
        return init;
    }

    @Override
    public Double getEnergy() {
        return energy;
    }

    @Override
    public Box getBox() {
        return box;
    }
}
