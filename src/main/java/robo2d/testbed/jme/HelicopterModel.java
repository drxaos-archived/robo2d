package robo2d.testbed.jme;

import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import straightedge.geom.KPoint;

public class HelicopterModel {
    AssetManager assetManager;

    public HelicopterModel(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    Spatial heliModel1;
    float right = 0, left = 0, front = 0, back = 0, top = 0, bottom = 0;
    float scaleW, scaleL, scaleH;

    public Node createHelicopter(KPoint pos, float angle) {
        if (heliModel1 == null) {
            heliModel1 = assetManager.loadModel("models/helicopter/Helicopter.obj");
            Vector3f[] vertices = ModelUtils.getVertices(heliModel1);
            for (Vector3f v : vertices) {
                if (v.x < right) {
                    right = v.x;
                }
                if (v.x > left) {
                    left = v.x;
                }
                if (v.z < back) {
                    back = v.z;
                }
                if (v.z > front) {
                    front = v.z;
                }
                if (v.y < bottom) {
                    bottom = v.y;
                }
                if (v.y > top) {
                    top = v.y;
                }
            }
            scaleW = 6.2f / Math.max(left, Math.abs(right));
            scaleL = 4.7f / Math.max(front, Math.abs(back));
            scaleH = 4.2f / Math.max(top, Math.abs(bottom));
            heliModel1.setLocalScale(scaleW, scaleH, scaleL);
            heliModel1.setLocalTranslation((right + left) / -2 * scaleW + 0.8f, bottom / -2 * scaleH, (front + back) / -2 * scaleL - 0.8f);
            heliModel1.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        }
        Node node = new Node("helicopter");
        node.attachChild(heliModel1.clone());

        node.setLocalRotation(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));
        node.setLocalTranslation((float) pos.getY(), 0, (float) pos.getX());

        node.setUserData("centerZ", node.getWorldTranslation().getZ());
        node.setUserData("centerX", node.getWorldTranslation().getX());

        return node;
    }

}
