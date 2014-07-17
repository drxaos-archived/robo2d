package robo2d.game.impl;

import com.robotech.military.api.*;
import robo2d.game.Game;
import robo2d.game.box2d.Box;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.RobotBox;
import straightedge.geom.KPoint;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

public class RobotImpl implements Robot, Physical, Enterable {

    Set<EquipmentImpl> equipment = new HashSet<EquipmentImpl>();
    Set<HasEffects> hasEffects = new HashSet<HasEffects>();
    ComputerImpl computer;

    Game game;
    PlayerImpl owner;
    RobotBox box;
    String uid;
    double energy = 0d;

    final Object sync = new Object();
    String debugMsg;
    Point2D debugPoint;

    PlayerImpl enteredPlayer;

    public RobotImpl(String uid, Game game, PlayerImpl owner, KPoint position, double angle) {
        this.uid = uid;
        this.owner = owner;
        this.game = game;
        box = new RobotBox(uid, position, angle);
    }

    public <T extends EquipmentImpl> T getEquipment(Class<T> type) {
        for (EquipmentImpl eq : equipment) {
            if (type.isAssignableFrom(eq.getClass())) {
                return (T) eq;
            }
        }
        return null;
    }

    public PlayerImpl getOwner() {
        return owner;
    }

    @Override
    public Chassis getChassis() {
        return getEquipment(ChassisImpl.class);
    }

    public ComputerImpl getComputer() {
        return computer;
    }

    @Override
    public Radar getRadar() {
        return getEquipment(RadarImpl.class);
    }

    @Override
    public Radio getRadio() {
        return null;
    }

    @Override
    public Turret getTurret() {
        return null;
    }

    @Override
    public Extention[] getExtentions() {
        return new Extention[0];
    }

    public void addEquipment(EquipmentImpl equipment) {
        this.equipment.add(equipment);
        if (equipment instanceof HasEffects) {
            hasEffects.add((HasEffects) equipment);
        }
        if (equipment instanceof ComputerImpl) {
            computer = (ComputerImpl) equipment;
        }
        equipment.setup(this);
    }

    @Override
    public Double getEnergy() {
        return energy;
    }

    @Override
    public String getUid() {
        return uid;
    }

    public boolean consumeEnergy(double amount) {
        if (energy > amount) {
            energy -= amount;
            if (energy < 0.01) {
                energy = 0;
            }
            return true;
        } else {
            energy = 0;
            return false;
        }
    }

    public void charge(double energy) {
        this.energy += energy;
    }

    @Override
    public Long getTime() {
        return game.getTime();
    }

    @Override
    public void waitForStep() {
        try {
            synchronized (sync) {
                sync.wait();
            }
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void debug(Object dbg) {
        if (dbg == null) {
            debugPoint = null;
            debugMsg = null;
        } else if (dbg instanceof Point2D) {
            debugPoint = (Point2D) dbg;
        } else {
            debugMsg = dbg.toString();
        }
    }

    public String getDebug() {
        return debugMsg;
    }

    public Point2D getDebugPoint() {
        return debugPoint;
    }

    @Override
    public Box getBox() {
        return box;
    }

    public void applyEffects() {
        for (HasEffects eq : hasEffects) {
            box.applyForces(uid, eq.getEffects());
        }
    }


    public void sync() {
        synchronized (sync) {
            sync.notifyAll();
        }
    }

    @Override
    public boolean canEnter(PlayerImpl player) {
        return owner == player;
    }

    @Override
    public void enter(PlayerImpl player) {
        if (enteredPlayer == null) {
            enteredPlayer = player;
            Terminal.open(getComputer());
        }
    }

    @Override
    public Point2D exit() {
        if (enteredPlayer != null) {
            Terminal.close(getComputer());
            enteredPlayer = null;
            return getBox().getPosition();
        } else {
            return null;
        }
    }
}
