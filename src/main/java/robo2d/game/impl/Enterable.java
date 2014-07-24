package robo2d.game.impl;

import java.awt.geom.Point2D;

public interface Enterable {

    boolean canEnter(PlayerImpl player);

    void enter(PlayerImpl player);

    AbstractComputer getComputer();

    Point2D exit();
}
