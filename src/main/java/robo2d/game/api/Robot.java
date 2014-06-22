package robo2d.game.api;

import java.util.Map;

public interface Robot {

    Radar getRadar();

    Radio getRadio();

    Chassis getChassis();

    Turret getTurret();

    Map<String, String> getInitializer();

    Double getEnergy();
}
