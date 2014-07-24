package robo2d.game.impl;

import com.robotech.military.api.Point;

public interface Host {
    AbstractComputer getComputer();

    boolean consumeEnergy(double v);

    String getUid();

    Point getPosition();

    Double getAngle();
}
