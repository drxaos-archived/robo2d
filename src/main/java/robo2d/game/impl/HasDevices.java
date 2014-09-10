package robo2d.game.impl;

public interface HasDevices {
    String[] getDevices();

    void activate(String device);

    void deactivate(String device);

    void close();
}
