package robo2d.testbed.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.system.AppSettings;
import com.jme3.system.Natives;
import com.jme3.terrain.geomipmap.TerrainGrid;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import robo2d.game.Game;
import robo2d.game.impl.RobotImpl;
import slick2d.NativeLoader;
import straightedge.geom.KPoint;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LiveFrame extends SimpleApplication {

    static {
        NativeLoader.load("build/natives");
        Natives.setExtractionDir("build/natives");
    }

    public static LiveFrame create(Game game) {
        final LiveFrame app = new LiveFrame(game);
        AppSettings appSettings = new AppSettings(true);
        appSettings.setFullscreen(false);
        appSettings.setVSync(true);
        appSettings.setResolution(1024, 768);
        appSettings.setDepthBits(24);
        appSettings.setBitsPerPixel(24);
        app.setSettings(appSettings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        Thread thread = new Thread() {
            @Override
            public void run() {
                app.start();
            }
        };
        thread.setDaemon(true);
        thread.start();
//        app.startCanvas();
//        JmeCanvasContext ctx = (JmeCanvasContext) app.getContext();
//        Dimension dim = new Dimension(640, 480);
//        ctx.getCanvas().setPreferredSize(dim);
//        app.canvas = ctx.getCanvas();
        return app;
    }


    Canvas canvas;

    public Canvas getCanvas() {
        return canvas;
    }

    Game game;

    Map<RobotImpl, Node> robotMap = new HashMap<RobotImpl, Node>();

    public LiveFrame(Game game) {
        this.game = game;
    }

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator("./", FileLocator.class);

        rootNode.detachAllChildren();
        flyCam.setMoveSpeed(10);
        flyCam.setDragToRotate(true);
        getCamera().setLocation(new Vector3f(-15, 25, -15));
        getCamera().lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));

        rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.addLight(createAmbient());
        DirectionalLight sun = createSun();
        rootNode.addLight(sun);

        TerrainQuad terrain = createGround();
        rootNode.attachChild(terrain);
//        TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
//        terrain.addControl(control);

        for (RobotImpl robot : game.getRobots()) {
            Node robotLive = createRobot();
            robotMap.put(robot, robotLive);
            rootNode.attachChild(robotLive);
        }

        /* Drop shadows */
        final int SHADOWMAP_SIZE = 2048;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        dlsr.setEdgesThickness(2);
        dlsr.setShadowIntensity(0.7f);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);
    }

    @Override
    public void simpleUpdate(float tpf) {
        for (Map.Entry<RobotImpl, Node> e : robotMap.entrySet()) {
            KPoint point = e.getKey().getBox().getPosition();
            e.getValue().setLocalTranslation((float) point.getY(), 0, (float) point.getX());
            Quaternion roll = new Quaternion();
            roll.fromAngleAxis((float) e.getKey().getBox().getAngle() + FastMath.PI, new Vector3f(0, 1, 0));
            e.getValue().setLocalRotation(roll);
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
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
                for (int i = 0; i < vec.length; i++) {
                    returnn[++count] = vec[i];
                }
            }
            return returnn;
        }
        return new Vector3f[0];
    }

    private Node createRobot() {
        Spatial robot = assetManager.loadModel("models/robot/robot.j3o");

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setFloat("Shininess", 10000);
        Texture tex = assetManager.loadTexture("models/robot/robot.png");
        mat.setTexture("DiffuseMap", tex);
//        mat.setTexture("NormalMap", tex);
        mat.setTexture("SpecularMap", tex);
        mat.setTexture("ParallaxMap", tex);
        robot.setMaterial(mat);

        float right = 0, left = 0, front = 0, back = 0, top = 0, bottom = 0;
        float scaleW, scaleL, scaleH;
        Vector3f[] vertices = getVertices(robot);
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
        robot.setLocalScale(scaleW, scaleH, scaleL);
        robot.setLocalTranslation((right + left) * scaleW, Math.abs(bottom) * scaleH, (front + back) * scaleL);
//        Quaternion roll = new Quaternion();
//        roll.fromAngleAxis(FastMath.PI / 2, new Vector3f(1, 0, 0));
//        robot.setLocalRotation(roll);
        robot.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        Node node = new Node("robot");
        node.attachChild(robot);
        return node;
    }

    private Spatial createSphere() {
        Spatial sphere = assetManager.loadModel("models/sphere/sphere.obj");

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        Texture tex = assetManager.loadTexture("models/sphere/sphere.png");
        mat.setTexture("DiffuseMap", tex);
        mat.setTexture("SpecularMap", tex);
        mat.setTexture("ParallaxMap", tex);
        sphere.setMaterial(mat);

        float right = 0, left = 0, front = 0, back = 0, top = 0, bottom = 0;
        float scaleW, scaleL, scaleH;
        Vector3f[] vertices = getVertices(sphere);
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
        scaleW = 0.5f / Math.max(left, Math.abs(right));
        scaleL = 0.5f / Math.max(front, Math.abs(back));
        scaleH = Math.max(scaleL, scaleW);
        sphere.setLocalScale(scaleW, scaleH, scaleL);
        sphere.setLocalTranslation(0, Math.abs(bottom) * scaleH, 0);
        sphere.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        return sphere;
    }

    private TerrainQuad createGround() {
        Material mat = new Material(assetManager,
                "Common/MatDefs/Terrain/Terrain.j3md");
        mat.setBoolean("useTriPlanarMapping", false);
        mat.setTexture("Alpha", assetManager.loadTexture("models/ground/alphamap.png"));

        Texture grass = assetManager.loadTexture("models/ground/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("Tex1", grass);
        mat.setFloat("Tex1Scale", 64);

        Texture dirt = assetManager.loadTexture("models/ground/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("Tex2", dirt);
        mat.setFloat("Tex2Scale", 16);

        // ROCK texture
        Texture rock = assetManager.loadTexture("models/ground/road.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("Tex3", rock);
        mat.setFloat("Tex3Scale", 128);

        TerrainQuad terrain = new TerrainQuad("ground", 65, 513, null);
        terrain.setShadowMode(RenderQueue.ShadowMode.Receive);
        TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
        control.setLodCalculator( new DistanceLodCalculator(65, 2.7f) ); // patch size, and a multiplier
        terrain.addControl(control);
        terrain.setMaterial(mat);

        return terrain;
    }

    private DirectionalLight createSun() {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(1.5f));
        sun.setDirection(new Vector3f(0.3f, -1f, 0.7f).normalizeLocal());
        return sun;
    }

    private AmbientLight createAmbient() {
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.2f));
        return al;
    }
}
