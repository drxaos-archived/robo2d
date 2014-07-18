package robo2d.game.impl.proxy;

import com.robotech.military.api.Point;

public class Radar implements com.robotech.military.api.Radar {
    com.robotech.military.api.Radar radar;

    public Radar(com.robotech.military.api.Radar radar) {
        this.radar = radar;
    }

    @Override
    public Boolean satelliteRequest(Point center, double accuracy) {
        return radar.satelliteRequest(center, accuracy);
    }

    @Override
    public SatelliteScanData getSatelliteResponse() {
        return radar.getSatelliteResponse();
    }

    @Override
    public void clearSatelliteResponse() {
        radar.clearSatelliteResponse();
    }

    @Override
    public LocatorScanData locate(double angle) {
        return radar.locate(angle);
    }

    @Override
    public Double getAngle() {
        return radar.getAngle();
    }

    @Override
    public Point getPosition() {
        return radar.getPosition();
    }
}
