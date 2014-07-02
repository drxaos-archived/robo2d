package robo2d.testbed.jme;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class RobotModel {
    AssetManager assetManager;

    public RobotModel(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    Spatial robotModel;
    float finalWidth = 1.2f;
    float finalLength = 1.2f;
    float right = 0, left = 0, front = 0, back = 0, top = 0, bottom = 0;
    float scaleW, scaleL, scaleH;

    public float getFinalWidth() {
        return finalWidth;
    }

    public float getFinalLength() {
        return finalLength;
    }

    public Node createRobot() {
        if(robotModel == null) {
            robotModel = assetManager.loadModel("models/robot/robot.j3o");
            Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat.setFloat("Shininess", 10000);
            Texture tex = assetManager.loadTexture("models/robot/robot.png");
            mat.setTexture("DiffuseMap", tex);
            mat.setTexture("SpecularMap", tex);
            mat.setTexture("ParallaxMap", tex);
            robotModel.setMaterial(mat);

            Vector3f[] vertices = getVertices(robotModel);
            for (Vector3f v : vertices) {
                if (v.z < back) {
                    back = v.z;
                }
                if (v.z > front) {
                    front = v.z;
                }
                if (v.x < right) {
                    right = v.x;
                }
                if (v.x > left) {
                    left = v.x;
                }
                if (v.y < bottom) {
                    bottom = v.y;
                }
                if (v.y > top) {
                    top = v.y;
                }
            }
            scaleW = finalWidth / Math.max(left, Math.abs(right));
            scaleL = finalLength / Math.max(front, Math.abs(back));
            scaleH = Math.max(scaleL, scaleW);
            robotModel.setLocalScale(scaleW, scaleH, scaleL);
            robotModel.setLocalTranslation((right + left) * scaleW, -bottom * scaleH, (front + back) * scaleL);
            robotModel.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        }
        Spatial robot = robotModel.clone();
        Node node = new Node("robot");
        node.attachChild(robot);
        return node;
    }

    public Vector3f[] getVertices(Spatial s) {

        if (s instanceof Geometry) {
            Geometry geometry = (Geometry) s;
            FloatBuffer vertexBuffer = geometry.getMesh().getFloatBuffer(VertexBuffer.Type.Position);
            return BufferUtils.getVector3Array(vertexBuffer);
        } else if (s instanceof Node) {
            Node n = (Node) s;

            ArrayList<Vector3f[]> array = new ArrayList<Vector3f[]>();

            for (Spatial ss : n.getChildren()) {
                array.add(getVertices(ss));
            }

            int count = 0;
            for (Vector3f[] vec : array) {
                count += vec.length;
            }

            Vector3f[] returnn = new Vector3f[count];
            count = -1;
            for (Vector3f[] vec : array) {
                for (Vector3f aVec : vec) {
                    returnn[++count] = aVec;
                }
            }
            return returnn;
        }
        return new Vector3f[0];
    }
}
