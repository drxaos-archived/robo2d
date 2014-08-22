package robo2d.testbed.jme;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import robo2d.game.impl.RobotImpl;

public class RobotModel {
    AssetManager assetManager;

    public RobotModel(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    Spatial robotModel1, robotModel2, agrModel1, agrModel2;
    float right = 0, left = 0, front = 0, back = 0, top = 0, bottom = 0;
    float scaleW, scaleL, scaleH;

    protected Node createRobot(RobotImpl robot) {
        Node node;
        if (robot.getUid().startsWith("AGR")) {
            node = createRobotAgr();
        } else {
            node = createRobot();
        }
        return node;
    }

    protected Node createRobot() {
        if (robotModel1 == null) {
            robotModel1 = assetManager.loadModel("models/robot/robot.obj");
            Vector3f[] vertices = ModelUtils.getVertices(robotModel1);
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
            robotModel1.setLocalScale(scaleW, scaleH, scaleL);
            robotModel1.setLocalTranslation((right + left) * scaleW, -bottom * scaleH, (front + back) * scaleL);
            robotModel1.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

            Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat.setFloat("Shininess", 10000);
            Texture tex = assetManager.loadTexture("models/robot/robot.png");
            mat.setTexture("DiffuseMap", tex);
            mat.setTexture("SpecularMap", tex);
            mat.setTexture("ParallaxMap", tex);
            robotModel1.setMaterial(mat);

            robotModel2 = robotModel1.clone();
            Material mat2 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat2.setFloat("Shininess", 10000);
            Texture tex2 = assetManager.loadTexture("models/robot/robot2.png");
            mat2.setTexture("DiffuseMap", tex2);
            mat2.setTexture("SpecularMap", tex2);
            mat2.setTexture("ParallaxMap", tex2);
            robotModel2.setMaterial(mat2);

            robotModel1.setName("r1");
            robotModel2.setName("r2");
        }
        Node node = new Node("robot");
        node.attachChild(robotModel1.clone());
        node.attachChild(robotModel2.clone());

        {
            Node player = new Node("player");
            player.setLocalTranslation(-0.35f, 1.7f, -0.3f);
            node.attachChild(player);
        }

        return node;
    }

    protected Node createRobotAgr() {
        if (agrModel1 == null) {
            agrModel1 = assetManager.loadModel("models/agr/AGR.obj");
            Vector3f[] vertices = ModelUtils.getVertices(agrModel1);
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
            agrModel1.setLocalScale(scaleL, scaleH, scaleW);
            agrModel1.setLocalTranslation((right + left) * scaleW - 0.1f, -bottom * scaleH, 0.1f + (front + back) * scaleL);
            agrModel1.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.PI / 2, Vector3f.UNIT_Y));
            agrModel1.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

            agrModel2 = agrModel1.clone();
            Material mat3 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat3.setFloat("Shininess", 10000);
            Texture tex3 = assetManager.loadTexture("models/agr/tire2.tga");
            mat3.setTexture("DiffuseMap", tex3);
            mat3.setTexture("SpecularMap", tex3);
            mat3.setTexture("ParallaxMap", tex3);
            ((Node) agrModel2).getChild("AGR-geom-3").setMaterial(mat3);

            agrModel1.setName("r1");
            agrModel2.setName("r2");
        }
        Node node = new Node("agr");
        node.attachChild(agrModel1.clone());
        node.attachChild(agrModel2.clone());

        return node;
    }

    public void animateChassis(Node robot) {
        if ((System.currentTimeMillis() / 100) % 2 == 0) {
            robot.getChild("r1").setCullHint(Spatial.CullHint.Dynamic);
            robot.getChild("r2").setCullHint(Spatial.CullHint.Always);
        } else {
            robot.getChild("r2").setCullHint(Spatial.CullHint.Dynamic);
            robot.getChild("r1").setCullHint(Spatial.CullHint.Always);
        }
    }
}
