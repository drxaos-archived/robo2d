package robo2d.testbed.jme;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import robo2d.game.impl.ControllerImpl;
import straightedge.geom.KPoint;

public class ControllerModel {
    AssetManager assetManager;

    public ControllerModel(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    Spatial cubeModel1;

    public Node createCube(ControllerImpl controller) {
        KPoint pos = controller.getPos();
        double angle = controller.getAngle();

        if (cubeModel1 == null) {

            Box box1 = new Box(0.5f, 0.5f, 0.5f);
            cubeModel1 = new Geometry("Box", box1);
            cubeModel1.setLocalTranslation(new Vector3f(0, 0.5f - 0.01f, 0));
//            Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//            mat1.setColor("Color", ColorRGBA.Blue);
//            blue.setMaterial(mat1);

            Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat.setFloat("Shininess", 10000);
            Texture tex = assetManager.loadTexture("models/controller/cube.jpg");
            mat.setTexture("DiffuseMap", tex);
            mat.setTexture("SpecularMap", tex);
            mat.setTexture("ParallaxMap", tex);
            cubeModel1.setMaterial(mat);
        }
        Node node = new Node("controller");
        node.attachChild(cubeModel1.clone());

        {
            Node player = new Node("player");
            player.setLocalTranslation(-1.5f, 1.0f, 0f);
            node.attachChild(player);
        }

        node.setLocalRotation(new Quaternion().fromAngleAxis((float) angle, Vector3f.UNIT_Y));
        node.setLocalTranslation((float) pos.getY(), 0, (float) pos.getX());

        return node;
    }


}
