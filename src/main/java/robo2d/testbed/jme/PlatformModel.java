package robo2d.testbed.jme;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.*;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import jme3tools.optimize.GeometryBatchFactory;
import org.jbox2d.common.Vec2;
import robo2d.game.impl.PlatformImpl;
import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class PlatformModel {
    AssetManager assetManager;

    public PlatformModel(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Spatial createPlatform(PlatformImpl platform) {
        List<Point2D> vert = platform.getVertices();
        List<KPolygon> triangulation = platform.getTriangulation();

        Node node = new Node("platform");

        final float H = 1.0f;
        final float BW = 4.0f;

        for (int i = 0; i < triangulation.size(); i++) {
            ArrayList<KPoint> points = triangulation.get(i).getPoints();

            Mesh mesh = new Mesh();
            Vector3f[] vertices = new Vector3f[3];
            vertices[0] = new Vector3f((float) points.get(0).getY(), H * 3 / 10, (float) points.get(0).getX());
            vertices[1] = new Vector3f((float) points.get(1).getY(), H * 3 / 10, (float) points.get(1).getX());
            vertices[2] = new Vector3f((float) points.get(2).getY(), H * 3 / 10, (float) points.get(2).getX());

            Vector2f[] texCoord = new Vector2f[3];
            texCoord[0] = new Vector2f((float) points.get(0).getY(), (float) points.get(0).getX());
            texCoord[1] = new Vector2f((float) points.get(1).getY(), (float) points.get(1).getX());
            texCoord[2] = new Vector2f((float) points.get(2).getY(), (float) points.get(2).getX());

            Vector3f[] normals = new Vector3f[3];
            normals[0] = normals[1] = normals[2] = Vector3f.UNIT_Y;

            int[] indexes = {2, 1, 0};

            mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
            mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
            mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indexes));
            mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
            mesh.updateBound();

            mesh.scaleTextureCoordinates(new Vector2f(0.2f, 0.2f));

            Geometry geo = new Geometry("topMesh", mesh);
            node.attachChild(geo);
        }

        Vec2 center = new Vec2();
        int pointsCounter = 0;

        for (int i = 0; i < vert.size(); i++) {
            Point2D v0 = vert.get(i > 0 ? i - 1 : vert.size() - 1);
            Point2D v1 = vert.get(i);
            Point2D v2 = vert.get((i + 1) % vert.size());
            Point2D v3 = vert.get((i + 2) % vert.size());

            if (v1.equals(v2)) {
                continue;
            }

            center = center.add(new Vec2((float) v1.getX(), (float) v1.getY()));
            pointsCounter++;

            Mesh mesh = new Mesh();

            final float W = (float) v1.distance(v2);

            Vector3f[] vertices = new Vector3f[4];
            double a0 = KPoint.findAngle((float) v0.getX(), (float) v0.getY(), (float) v1.getX(), (float) v1.getY()) + Math.PI / 2;
            double a1 = KPoint.findAngle((float) v1.getX(), (float) v1.getY(), (float) v2.getX(), (float) v2.getY()) + Math.PI / 2;
            while (a1 < a0) {
                a1 += Math.PI * 2;
            }
            while (a1 > a0) {
                a1 -= Math.PI * 2;
            }
            double a2 = KPoint.findAngle((float) v2.getX(), (float) v2.getY(), (float) v3.getX(), (float) v3.getY()) + Math.PI / 2;
            while (a2 < a1) {
                a2 += Math.PI * 2;
            }
            while (a2 > a1) {
                a2 -= Math.PI * 2;
            }
            // a0 > a1 > a2
            double a01 = (a0 + a1) / 2;
            double a12 = (a1 + a2) / 2;
            vertices[0] = new Vector3f((float) v1.getY() + FastMath.sin((float) a01) * BW, -H * 7 / 10, (float) v1.getX() + FastMath.cos((float) a01) * BW);
            vertices[1] = new Vector3f((float) v2.getY() + FastMath.sin((float) a12) * BW, -H * 7 / 10, (float) v2.getX() + FastMath.cos((float) a12) * BW);
            vertices[2] = new Vector3f((float) v2.getY(), H * 3 / 10, (float) v2.getX());
            vertices[3] = new Vector3f((float) v1.getY(), H * 3 / 10, (float) v1.getX());

            Vector2f[] texCoord = new Vector2f[4];
            texCoord[0] = new Vector2f(0, 0);
            texCoord[1] = new Vector2f(W, 0);
            texCoord[2] = new Vector2f(W, H);
            texCoord[3] = new Vector2f(0, H);

            Vector3f[] normals = new Vector3f[4];
            normals[0] = normals[1] = normals[2] = normals[3] = Vector3f.UNIT_Y;

            int[] indexes = {
                    3, 1, 0,
                    3, 2, 1,
            };

            mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
            mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
            mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indexes));
            mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
            mesh.updateBound();

            mesh.scaleTextureCoordinates(new Vector2f(0.2f, 0.2f));

            Geometry geo = new Geometry("mesh", mesh);
            node.attachChild(geo);
        }


        Spatial spatial = GeometryBatchFactory.optimize(node);

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setFloat("Shininess", 10000);
        Texture tex = assetManager.loadTexture("models/ground/metal.jpg");
        tex.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", tex);
        mat.setTexture("SpecularMap", tex);
        mat.setTexture("ParallaxMap", tex);
        spatial.setMaterial(mat);

        center = center.mul(1f / pointsCounter);
        spatial.setUserData("centerZ", center.x);
        spatial.setUserData("centerX", center.y);

        return spatial;
    }

}
