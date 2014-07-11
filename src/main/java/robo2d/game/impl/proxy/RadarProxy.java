package robo2d.game.impl.proxy;

import com.robotech.military.api.Point;
import com.robotech.military.api.Radar;

import java.rmi.Remote;
import java.rmi.RemoteException;

public class RadarProxy implements Radar, Remote {
    Radar radar;

    @Override
    public Boolean satelliteRequest(Point point, double v) throws RemoteException {
        return radar.satelliteRequest(point, v);
    }

    @Override
    public SatelliteScanData getSatelliteResponse() throws RemoteException {
        return radar.getSatelliteResponse();
    }

    @Override
    public void clearSatelliteResponse() throws RemoteException {
        radar.clearSatelliteResponse();
    }

    @Override
    public LocatorScanData locate(double v) throws RemoteException {
        return radar.locate(v);
    }

    @Override
    public Double getAngle() throws RemoteException {
        return radar.getAngle();
    }

    @Override
    public Point getPosition() throws RemoteException {
        return radar.getPosition();
    }

    public RadarProxy(Radar radar) throws RemoteException {
        this.radar = radar;
    }
}
