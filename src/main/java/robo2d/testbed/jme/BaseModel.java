package robo2d.testbed.jme;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.*;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import straightedge.geom.KPoint;

import java.awt.geom.Point2D;

public class BaseModel {
    AssetManager assetManager;

    public BaseModel(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    Spatial baseModel1, laptopModel1, barrelModel1, rackModel1;
    float right = 0, left = 0, front = 0, back = 0, top = 0, bottom = 0;
    float scaleW, scaleL, scaleH;

    public Node createBase(KPoint pos, float angle) {
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
            baseModel1.setLocalTranslation((right + left) / -2 * scaleW, bottom / -2 * scaleH - 0.3f, (front + back) / -2 * scaleL);
            baseModel1.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        }
        if (laptopModel1 == null) {
            laptopModel1 = assetManager.loadModel("models/computer/laptop/Computer_Laptop.obj");
            float scale = 0.8f;
            laptopModel1.setLocalScale(scale, scale, scale);
            laptopModel1.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.PI * 8 / 5, Vector3f.UNIT_Y));
            laptopModel1.setLocalTranslation(-2, 0, 0);
            laptopModel1.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            laptopModel1.setName("laptop");
        }
        if (barrelModel1 == null) {
            barrelModel1 = assetManager.loadModel("models/barrel/barrel.obj");
            float scale = 0.0017f;
            barrelModel1.setLocalScale(scale, scale, scale);
            barrelModel1.setLocalTranslation(-3, 0, -2.5f);
            barrelModel1.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        }
        if (rackModel1 == null) {
            Point2D v2 = new Point2D.Float(-1.2f, 2.4f);
            Point2D v1 = new Point2D.Float(0, 3.4f);

            Mesh mesh = new Mesh();

            final float H = 3;

            Vector3f[] vertices = new Vector3f[4];
            vertices[0] = new Vector3f((float) v1.getY(), 0, (float) v1.getX());
            vertices[1] = new Vector3f((float) v2.getY(), 0, (float) v2.getX());
            vertices[2] = new Vector3f((float) v2.getY(), H, (float) v2.getX());
            vertices[3] = new Vector3f((float) v1.getY(), H, (float) v1.getX());

            Vector2f[] texCoord = new Vector2f[4];
            texCoord[1] = new Vector2f(1, 0);
            texCoord[2] = new Vector2f(1, 1);
            texCoord[3] = new Vector2f(0, 1);
            texCoord[0] = new Vector2f(0, 0);

            Vector3f[] normals = new Vector3f[4];
            normals[0] = normals[1] = normals[2] = normals[3] = vertices[3].subtract(vertices[0]).cross(vertices[1].subtract(vertices[0]));

            int[] indexes = {
                    3, 1, 0,
                    3, 2, 1,
            };

            mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
            mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
            mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indexes));
            mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
            mesh.updateBound();

            rackModel1 = new Geometry("mesh", mesh);
            Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat.setFloat("Shininess", 10000);
            Texture tex = assetManager.loadTexture("models/computer/rack/server.jpg");
            mat.setTexture("DiffuseMap", tex);
            mat.setTexture("SpecularMap", tex);
            mat.setTexture("ParallaxMap", tex);
            rackModel1.setMaterial(mat);
        }
        Node node = new Node("base");
        node.attachChild(baseModel1.clone());
        node.attachChild(laptopModel1.clone());
        //node.attachChild(rackModel1.clone());
        //node.attachChild(barrelModel1.clone());

        node.setLocalRotation(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));
        node.setLocalTranslation((float) pos.getY(), 0, (float) pos.getX());

        node.setUserData("centerZ", node.getWorldTranslation().getZ());
        node.setUserData("centerX", node.getWorldTranslation().getX());

        return node;
    }


}
