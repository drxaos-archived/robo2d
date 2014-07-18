package robo2d.game.impl.proxy;

public class Turret implements com.robotech.military.api.Turret {
    com.robotech.military.api.Turret turret;

    public Turret(com.robotech.military.api.Turret turret) {
        this.turret = turret;
    }

    @Override
    public void shock() {
        turret.shock();
    }

    @Override
    public void fire() {
        turret.fire();
    }
}
