package robo2d.testbed.jme;

import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class StaticCamera {
    Node node;

    public StaticCamera(Node node) {
        this.node = node;
    }

    public void alignCam(Camera camera) {
        Spatial cam = node.getChild("camera");
        if (cam == null) {
            cam = node;
        }
        camera.setLocation(cam.getWorldTranslation());
        camera.setRotation(cam.getWorldRotation());
    }
}
