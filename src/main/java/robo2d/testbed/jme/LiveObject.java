package robo2d.testbed.jme;

import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class LiveObject {
    Spatial spatial;
    Transform localTransform;

    public LiveObject(Spatial spatial) {
        this.spatial = spatial;
        localTransform = spatial.getLocalTransform().clone();
    }

    public void setPos(float angle, float x, float y) {
        Quaternion roll = new Quaternion();
        roll.fromAngleAxis(angle, new Vector3f(0, 1, 0));
        spatial.setLocalRotation(roll.mult(localTransform.getRotation()));
        spatial.setLocalTranslation(new Vector3f(y, 0, x).add(localTransform.getTranslation()));
    }
}
