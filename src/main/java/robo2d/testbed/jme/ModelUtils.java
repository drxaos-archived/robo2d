package robo2d.testbed.jme;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.*;
import com.jme3.scene.debug.Arrow;
import com.jme3.util.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class ModelUtils {

    public static Vector3f[] getVertices(Spatial s) {

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

    public static void attachCoordinateAxes(AssetManager assetManager, Node rootNode, Vector3f pos) {
        Arrow arrow = new Arrow(Vector3f.UNIT_X);
        arrow.setLineWidth(4); // make arrow thicker
        putShape(assetManager, rootNode, arrow, ColorRGBA.Red).setLocalTranslation(pos);

        arrow = new Arrow(Vector3f.UNIT_Y);
        arrow.setLineWidth(4); // make arrow thicker
        putShape(assetManager, rootNode, arrow, ColorRGBA.Green).setLocalTranslation(pos);

        arrow = new Arrow(Vector3f.UNIT_Z);
        arrow.setLineWidth(4); // make arrow thicker
        putShape(assetManager, rootNode, arrow, ColorRGBA.Blue).setLocalTranslation(pos);
    }

    public static Geometry putShape(AssetManager assetManager, Node rootNode, Mesh shape, ColorRGBA color) {
        Geometry g = new Geometry("coordinate axis", shape);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        rootNode.attachChild(g);
        return g;
    }
}
