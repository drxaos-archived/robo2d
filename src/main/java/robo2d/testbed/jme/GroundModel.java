package robo2d.testbed.jme;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.*;
import com.jme3.terrain.geomipmap.grid.ImageTileLoader;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.HeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import robo2d.game.Game;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroundModel {
    GroundObjectsControl groundObjectsControl;
    AssetManager assetManager;
    Camera camera;
    Game game;

    public GroundModel(GroundObjectsControl groundObjectsControl, AssetManager assetManager, Camera camera, Game game) {
        this.groundObjectsControl = groundObjectsControl;
        this.assetManager = assetManager;
        this.camera = camera;
        this.game = game;
    }

    public Node createGround() {
        TerrainGrid terrain = new TerrainGrid("grid", 65, 257, new GroundTileLoader(assetManager, game));
        terrain.addListener(new TerrainGridListener() {
            @Override
            public void gridMoved(Vector3f vector3f) {

            }

            @Override
            public void tileAttached(Vector3f vector3f, TerrainQuad terrainQuad) {
                groundObjectsControl.onTerrainLoaded();
            }

            @Override
            public void tileDetached(Vector3f vector3f, TerrainQuad terrainQuad) {

            }
        });

        Material mat = new Material(assetManager,
                "Common/MatDefs/Terrain/Terrain.j3md");
        mat.setBoolean("useTriPlanarMapping", false);
        mat.setTexture("Alpha", assetManager.loadTexture("models/ground/alphamap.png"));

        Texture dirt = assetManager.loadTexture("models/ground/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("Tex1", dirt);
        mat.setFloat("Tex1Scale", 16);

        Texture grass = assetManager.loadTexture("models/ground/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("Tex2", grass);
        mat.setFloat("Tex2Scale", 64);

        Texture rock = assetManager.loadTexture("models/ground/ground.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("Tex3", rock);
        mat.setFloat("Tex3Scale", 450);
        terrain.setMaterial(mat);

        terrain.setLocalScale(2f, 1, 2f);
        terrain.setShadowMode(RenderQueue.ShadowMode.Receive);

        TerrainLodControl control = new TerrainGridLodControl(terrain, camera);
        control.setLodCalculator(new DistanceLodCalculator(65, 2.7f)); // patch size, and a multiplier
        terrain.addControl(control);

        Node node = new Node("ground");
        node.attachChild(terrain);

        return node;
    }

}

class GroundTileLoader implements TerrainGridTileLoader {
    private static final Logger logger = Logger.getLogger(ImageTileLoader.class.getName());
    private final AssetManager assetManager;
    private int patchSize;
    private int quadSize;
    private float heightScale = 0.1f;
    Material mat;
    Texture alphaDefault;
    Game game;

    GroundTileLoader(AssetManager assetManager, Game game) {
        this.assetManager = assetManager;
        this.game = game;
    }

    private HeightMap getHeightMapAt(Vector3f location) {
        int x = ((int) Math.abs(location.x)) % 2;
        int z = ((int) Math.abs(location.z)) % 2;

        AbstractHeightMap heightmap = null;

        String name = null;
        try {
            final Texture texture = assetManager.loadTexture("models/ground/ground_" + x + "_" + z + ".png");
            heightmap = new ImageBasedHeightMap(texture.getImage());
            heightmap.setHeightScale(heightScale);
            heightmap.load();
        } catch (AssetNotFoundException e) {
            logger.log(Level.WARNING, "Asset {0} not found, loading zero heightmap instead", name);
        }
        return heightmap;
    }

    public void setSize(int size) {
        this.patchSize = size - 1;
    }

    public TerrainQuad getTerrainQuadAt(Vector3f location) {
        int x = ((int) Math.abs(location.x));
        int z = ((int) Math.abs(location.z));

        HeightMap heightMapAt = getHeightMapAt(location);
        TerrainQuad q = new TerrainQuad("Quad" + location, patchSize, quadSize, heightMapAt == null ? null : heightMapAt.getHeightMap());

        if (mat == null) {
            mat = new Material(assetManager,
                    "Common/MatDefs/Terrain/Terrain.j3md");
            mat.setBoolean("useTriPlanarMapping", false);

            Texture dirt = assetManager.loadTexture("models/ground/dirt.jpg");
            dirt.setWrap(Texture.WrapMode.Repeat);
            mat.setTexture("Tex1", dirt);
            mat.setFloat("Tex1Scale", 16);

            Texture grass = assetManager.loadTexture("models/ground/grass.jpg");
            grass.setWrap(Texture.WrapMode.Repeat);
            mat.setTexture("Tex2", grass);
            mat.setFloat("Tex2Scale", 64);

            Texture rock = assetManager.loadTexture("models/ground/ground.jpg");
            rock.setWrap(Texture.WrapMode.Repeat);
            mat.setTexture("Tex3", rock);
            mat.setFloat("Tex3Scale", 450);
        }
        Material m = mat.clone();
        Texture alpha = null;
        try {
            alpha = assetManager.loadTexture("models/ground/alphamap_" + x + "_" + z + ".png");
        } catch (AssetNotFoundException e) {
            if (alphaDefault == null) {
                alphaDefault = assetManager.loadTexture("models/ground/alphamap.png");
            }
            alpha = alphaDefault;
        }
        m.setTexture("Alpha", alpha);
        q.setMaterial(m);

        return q;
    }

    public void setPatchSize(int patchSize) {
        this.patchSize = patchSize;
    }

    public void setQuadSize(int quadSize) {
        this.quadSize = quadSize;
    }

    public void write(JmeExporter ex) throws IOException {
        //TODO: serialization
    }

    public void read(JmeImporter im) throws IOException {
        //TODO: serialization
    }
}