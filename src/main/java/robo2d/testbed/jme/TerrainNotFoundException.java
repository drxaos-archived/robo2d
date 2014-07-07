package robo2d.testbed.jme;

public class TerrainNotFoundException extends RuntimeException {
    public TerrainNotFoundException() {
    }

    public TerrainNotFoundException(String message) {
        super(message);
    }

    public TerrainNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TerrainNotFoundException(Throwable cause) {
        super(cause);
    }
}
