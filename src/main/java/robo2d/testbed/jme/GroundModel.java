package robo2d.testbed.jme;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.bounding.BoundingSphere;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.terrain.geomipmap.*;
import com.jme3.terrain.geomipmap.grid.ImageTileLoader;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.HeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.plugins.AWTLoader;
import composite.BlendComposite;
import jme3tools.converters.ImageToAwt;
import robo2d.game.Game;
import robo2d.game.box2d.Physical;
import robo2d.game.impl.WallImpl;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroundModel {
    AssetManager assetManager;
    Camera camera;
    Game game;

    public GroundModel(AssetManager assetManager, Camera camera, Game game) {
        this.assetManager = assetManager;
        this.camera = camera;
        this.game = game;
    }

    public TerrainQuad createGround() {
        TerrainGrid terrain = new TerrainGrid("ground", 65, 257, new GroundTileLoader(assetManager, game));

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

        return terrain;
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

    public void setHeightScale(float heightScale) {
        this.heightScale = heightScale;
    }


    public static ConvolveOp getGaussianBlurFilter(int radius,
                                                   boolean horizontal) {
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }

        int size = radius * 2 + 1;
        float[] data = new float[size];

        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;

        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            int index = i + radius;
            data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += data[index];
        }

        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }

        Kernel kernel = null;
        if (horizontal) {
            kernel = new Kernel(size, 1, data);
        } else {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }

    private HeightMap getHeightMapAt(Vector3f location) {
        int x = ((int) Math.abs(location.x)) % 2;
        int z = ((int) Math.abs(location.z)) % 2;

        AbstractHeightMap heightmap = null;

        String name = null;
        try {
            final Texture texture = assetManager.loadTexture("models/ground/ground_" + x + "_" + z + ".png");
//            BufferedImage heightMapAwtImage = ImageToAwt.convert(texture.getImage(), false, true, 0);
//            BufferedImage rocksImg = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
//            Graphics2D g = rocksImg.createGraphics();
//            g.setColor(Color.BLACK);
//            g.setBackground(Color.BLACK);
//            g.fillRect(0, 0, 128, 128);
//            Color rocksColor = new Color(50, 50, 50);
//            g.setColor(rocksColor);
//            g.setBackground(rocksColor);
//            for (Physical physical : game.getPhysicals()) {
//                if (physical instanceof WallImpl) {
//                    java.util.List<Point2D> vertices = ((WallImpl) physical).getVertices();
//                    int[] xp = new int[vertices.size()];
//                    int[] yp = new int[vertices.size()];
//                    for (int i = 0; i < vertices.size(); i++) {
//                        xp[i] = (int) vertices.get(i).getX();
//                        yp[i] = (int) vertices.get(i).getY();
//                    }
//                    g.fillPolygon(xp, yp, vertices.size());
//                }
//            }
//            rocksImg = getGaussianBlurFilter(2, true).filter(rocksImg, null);
//            rocksImg = getGaussianBlurFilter(2, false).filter(rocksImg, null);
//            Graphics2D g2 = heightMapAwtImage.createGraphics();
//            g2.setComposite(BlendComposite.Add);
//            g2.drawImage(rocksImg, 0, 0, null);
//            texture.setImage(new AWTLoader().load(heightMapAwtImage, true));

            heightmap = new ImageBasedHeightMap(texture.getImage());
            heightmap.setHeightScale(heightScale);
            heightmap.load();
//            heightmap.smooth(0.8f, 1);
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