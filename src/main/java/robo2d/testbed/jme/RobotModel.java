package robo2d.testbed.jme;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import robo2d.game.impl.RobotImpl;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class RobotModel {
    AssetManager assetManager;

    public RobotModel(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    Spatial robotModel, robotModel2, agrModel;
    float right = 0, left = 0, front = 0, back = 0, top = 0, bottom = 0;
    float scaleW, scaleL, scaleH;

    protected Node createRobot(RobotImpl robot) {
        if (robot.getUid().startsWith("AGR")) {
            return createRobotAgr();
        } else {
            return createRobot();
        }
    }

    protected Node createRobot() {
        if (robotModel == null) {
            robotModel = assetManager.loadModel("models/robot/robot.j3o");
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
            scaleW = 1.2f / Math.max(left, Math.abs(right));
            scaleL = 1.2f / Math.max(front, Math.abs(back));
            scaleH = Math.max(scaleL, scaleW);
            robotModel.setLocalScale(scaleW, scaleH, scaleL);
            robotModel.setLocalTranslation((right + left) * scaleW, -bottom * scaleH, (front + back) * scaleL);
            robotModel.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

            Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat.setFloat("Shininess", 10000);
            Texture tex = assetManager.loadTexture("models/robot/robot.png");
            mat.setTexture("DiffuseMap", tex);
            mat.setTexture("SpecularMap", tex);
            mat.setTexture("ParallaxMap", tex);
            robotModel.setMaterial(mat);

            robotModel2 = robotModel.clone();
            Material mat2 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat2.setFloat("Shininess", 10000);
            Texture tex2 = assetManager.loadTexture("models/robot/robot2.png");
            mat2.setTexture("DiffuseMap", tex2);
            mat2.setTexture("SpecularMap", tex2);
            mat2.setTexture("ParallaxMap", tex2);
            robotModel2.setMaterial(mat2);

            robotModel.setName("r1");
            robotModel2.setName("r2");
        }
        Node node = new Node("robot");
        node.attachChild(robotModel.clone());
        node.attachChild(robotModel2.clone());

        return node;
    }

    protected Node createRobotAgr() {
        if (agrModel == null) {
            agrModel = assetManager.loadModel("models/agr/AGR.obj");
            Vector3f[] vertices = getVertices(agrModel);
            for (Vector3f v : vertices) {
                if (v.x < front) {
                    front = v.x;
                }
                if (v.x > back) {
                    back = v.x;
                }
                if (v.z < right) {
                    right = v.z;
                }
                if (v.z > left) {
                    left = v.z;
                }
                if (v.y < bottom) {
                    bottom = v.y;
                }
                if (v.y > top) {
                    top = v.y;
                }
            }
            bottom += 170;//fix model
            scaleW = 1.2f / (Math.abs(left) + Math.abs(right)) * 2;
            scaleL = 1.2f / (Math.abs(front) + Math.abs(back)) * 2;
            scaleH = Math.max(scaleL, scaleW);
            agrModel.setLocalScale(scaleL, scaleH, scaleW);
            agrModel.setLocalTranslation((right + left) * scaleW - 0.1f, -bottom * scaleH, 0.1f + (front + back) * scaleL);
            agrModel.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.PI / 2, Vector3f.UNIT_Y));
            agrModel.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

            agrModel.setName("agr1");
        }
        Node node = new Node("agr");
        node.attachChild(agrModel.clone());

        return node;
    }

    public void animateChassis(Node robot) {
        if (robot.getChild("r1") != null && robot.getChild("r2") != null) {
            if ((System.currentTimeMillis() / 100) % 2 == 0) {
                robot.getChild("r1").setCullHint(Spatial.CullHint.Dynamic);
                robot.getChild("r2").setCullHint(Spatial.CullHint.Always);
            } else {
                robot.getChild("r2").setCullHint(Spatial.CullHint.Dynamic);
                robot.getChild("r1").setCullHint(Spatial.CullHint.Always);
            }
        }
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
