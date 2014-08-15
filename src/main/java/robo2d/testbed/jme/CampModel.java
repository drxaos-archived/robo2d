package robo2d.testbed.jme;

import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import straightedge.geom.KPoint;

public class CampModel {
    AssetManager assetManager;

    public CampModel(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    Spatial baseModel1;
    float right = 0, left = 0, front = 0, back = 0, top = 0, bottom = 0;
    float scaleW, scaleL, scaleH;

    public Node createCamp(KPoint pos, float angle) {
        if (baseModel1 == null) {
            baseModel1 = assetManager.loadModel("models/base/Tent_1.obj");
            Vector3f[] vertices = ModelUtils.getVertices(baseModel1);
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
            scaleL = 9.7f / Math.max(front, Math.abs(back));
            scaleH = 4.2f / Math.max(top, Math.abs(bottom));
            baseModel1.setLocalScale(scaleW, scaleH, scaleL);
            baseModel1.setLocalTranslation((right + left) / -2 * scaleW, bottom / -2 * scaleH - 0.4f, (front + back) / -2 * scaleL);
            baseModel1.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        }
        Node node = new Node("camp");
        node.attachChild(baseModel1.clone());

        {
            Node camera = new Node("camera");
            camera.setLocalTranslation(0, 5f, 0);
            camera.lookAt(new Vector3f(0, 0, 25), Vector3f.UNIT_Y);
            node.attachChild(camera);
        }

        node.setLocalRotation(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));
        node.setLocalTranslation((float) pos.getY(), 0, (float) pos.getX());

        node.setUserData("centerZ", node.getWorldTranslation().getZ());
        node.setUserData("centerX", node.getWorldTranslation().getX());

        return node;
    }


}
