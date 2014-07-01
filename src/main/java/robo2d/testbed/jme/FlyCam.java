package robo2d.testbed.jme;

import com.jme3.input.FlyByCamera;
import com.jme3.renderer.Camera;

public class FlyCam extends FlyByCamera {

    public FlyCam(Camera cam) {
        super(cam);
    }

    public Camera getCam() {
        return cam;
    }
}
