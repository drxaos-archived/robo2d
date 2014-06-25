package robo2d.game.api;

import straightedge.geom.KPoint;

public interface Radar extends Equipment {

    public static enum Type {
        UNKNOWN,
        EMPTY,
        WALL,
        ENEMY_BOT,
        MATE_BOT,
        ME
    }

    public static class ScanData {
        public Type pixel;
        public double distance;
        public double angle;

        public ScanData(Type pixel, double distance, double angle) {
            this.pixel = pixel;
            this.distance = distance;
            this.angle = angle;
        }
    }

    public static class FullScanData {

        public Type[][] image;
        public double accuracy;
        public int centerX, centerY;

        public FullScanData(Type[][] image, double accuracy, int centerX, int centerY) {
            this.image = image;
            this.accuracy = accuracy;
            this.centerX = centerX;
            this.centerY = centerY;
        }
    }

    FullScanData fullScan(double accuracy);

    ScanData scan(double angle);

    Double getAngle();

    KPoint getPosition();

}
