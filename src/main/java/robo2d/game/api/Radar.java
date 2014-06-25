package robo2d.game.api;

import robo2d.game.api.map.Map;
import straightedge.geom.KPoint;

public interface Radar extends Equipment {

    Map fullScan();

    Double getAngle();

    KPoint getPosition();

}
