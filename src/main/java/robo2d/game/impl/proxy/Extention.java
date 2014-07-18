package robo2d.game.impl.proxy;

import java.io.Serializable;

public class Extention implements com.robotech.military.api.Extention {
    com.robotech.military.api.Extention extention;

    public Extention(com.robotech.military.api.Extention extention) {
        this.extention = extention;
    }

    @Override
    public Serializable invoke(String method, Serializable... args) {
        return extention.invoke(method, args);
    }
}
