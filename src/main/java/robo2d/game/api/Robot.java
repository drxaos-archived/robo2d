package robo2d.game.api;

import java.util.Collections;
import java.util.Map;

final public class Robot {

    protected final Radar radar;
    protected final Radio radio;
    protected final Chassis chassis;
    protected final Turret turret;
    protected final Map<String, String> init;

    public Robot(Radar radar, Radio radio, Chassis chassis, Turret turret, Map<String, String> init) {
        this.radar = radar;
        this.radio = radio;
        this.chassis = chassis;
        this.turret = turret;
        this.init = Collections.unmodifiableMap(init);
    }
}
