package robo2d.testbed.jme;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Surface;
import com.jme3.texture.Texture;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class RockModel {
    AssetManager assetManager;

    public RockModel(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Spatial createRock(List<Point2D> vert) {
        List<List<Vector4f>> control = new ArrayList<List<Vector4f>>();
        List<Vector4f> bottom = new ArrayList<Vector4f>();
        for (Point2D point2D : vert) {
            bottom.add(new Vector4f((float) point2D.getY(), 0, (float) point2D.getX(), 1));
        }
        List<Vector4f> top = new ArrayList<Vector4f>();
        for (Point2D point2D : vert) {
            top.add(new Vector4f((float) point2D.getY(), 3, (float) point2D.getX(), 1));
        }
        control.add(bottom);
        control.add(top);

        List[] knots = new List[2];
        knots[0] = new ArrayList<Float>();
        for (int i = 0; i < vert.size(); i++) {
            knots[0].add((float) i);
        }
        knots[1] = new ArrayList<Float>();
        for (int i = 0; i < vert.size(); i++) {
            knots[1].add((float) i);
        }

        Surface mesh = Surface.createNurbsSurface(control, knots, 10, 10, 1, 1);
        Geometry rock = new Geometry("rock", mesh);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetManager.loadTexture("models/ground/road.jpg");
        mat.setTexture("ColorMap", tex);
        rock.setMaterial(mat);

        return rock;
    }

}
