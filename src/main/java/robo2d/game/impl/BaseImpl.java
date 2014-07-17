package robo2d.game.impl;

import com.jme3.math.FastMath;
import robo2d.game.box2d.Box;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.StaticBox;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseImpl implements Physical, Enterable {

    public static final float SIZE = 6;

    StaticBox box;
    KPoint pos;
    float angle;
    String laptopName;
    Map<String, String> memory = new HashMap<String, String>();
    PlayerImpl owner;

    PlayerImpl enteredPlayer;

    public KPoint getPos() {
        return pos;
    }

    public float getAngle() {
        return angle;
    }

    public BaseImpl(PlayerImpl owner, KPoint pos, float angle) {
        this.pos = pos;
        this.angle = angle;
        this.owner = owner;

        ArrayList<KPoint> kPoints = new ArrayList<KPoint>();

        kPoints.add(new KPoint(1f, 1f));
        kPoints.add(new KPoint(1f, -1f));
        kPoints.add(new KPoint(-1f, -1f));
        kPoints.add(new KPoint(-1f, 1f));
        kPoints.add(new KPoint(-1f / 4f, 1f));
        kPoints.add(new KPoint(-1f / 4f, 7f / 9f));
        kPoints.add(new KPoint(-8f / 9f, 7f / 9f));
        kPoints.add(new KPoint(-8f / 9f, -8f / 9f));
        kPoints.add(new KPoint(8f / 9f, -8f / 9f));
        kPoints.add(new KPoint(8f / 9f, 7f / 9f));
        kPoints.add(new KPoint(1f / 4f, 7f / 9f));
        kPoints.add(new KPoint(1f / 4f, 1f));
        KPolygon polygon = new KPolygon(kPoints);
        polygon.scale(SIZE * 0.7);
        polygon.rotate(-FastMath.PI / 2);

        box = new StaticBox(polygon, pos, angle);
    }

    public Map<String, String> getMemory() {
        return memory;
    }

    public void saveFile(String fileName, String content) {
        memory.put(fileName, content);
    }

    public String loadFile(String fileName) {
        return memory.get(fileName);
    }

    @Override
    public Box getBox() {
        return box;
    }

    @Override
    public boolean canEnter(PlayerImpl player) {
        return owner == player;
    }

    @Override
    public void enter(PlayerImpl player) {
        if (enteredPlayer == null) {
            enteredPlayer = player;
            Terminal.open(this);
        }
    }

    @Override
    public Point2D exit() {
        if (enteredPlayer != null) {
            Terminal.close(this);
            enteredPlayer = null;
            return getBox().getPosition();
        } else {
            return null;
        }
    }

    public String getLaptopName() {
        return laptopName;
    }

    public void setLaptopName(String laptopName) {
        this.laptopName = laptopName;
    }
}
