package robo2d.testbed.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.*;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.system.AppSettings;
import com.jme3.system.Natives;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.util.SkyFactory;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.DefaultScreenController;
import robo2d.game.Game;
import robo2d.game.api.Chassis;
import robo2d.game.box2d.Physical;
import robo2d.game.box2d.RobotBox;
import robo2d.game.impl.RobotImpl;
import robo2d.game.impl.WallImpl;
import slick2d.NativeLoader;
import straightedge.geom.KPoint;

import java.awt.*;
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

    TerrainQuad terrain;
    RobotModel robotModel;
    GroundModel groundModel;
    RockModel rockModel;
    Nifty nifty;

    Map<RobotImpl, Node> robotMap = new HashMap<RobotImpl, Node>();
    java.util.List<WallImpl> walls = new ArrayList<WallImpl>();

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

        Spatial sky = SkyFactory.createSky(
                assetManager, "models/sky/BrightSky.dds", false);
        sky.setShadowMode(RenderQueue.ShadowMode.Off);
        rootNode.attachChild(sky);

        rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.addLight(createAmbient());
        DirectionalLight sun = createSun();
        rootNode.addLight(sun);

        rockModel = new RockModel(assetManager);
        for (Physical physical : game.getPhysicals()) {
            if (physical instanceof WallImpl) {
                Spatial rock = rockModel.createRock((WallImpl) physical);
                rootNode.attachChild(rock);
            }
        }

        groundModel = new GroundModel(assetManager, cam, game);
        terrain = groundModel.createGround();
        rootNode.attachChild(terrain);

        robotModel = new RobotModel(assetManager);
        for (Physical physical : game.getPhysicals()) {
            if (physical instanceof RobotImpl) {
                Node robotLive = robotModel.createRobot((RobotImpl) physical);
                robotMap.put((RobotImpl) physical, robotLive);
                rootNode.attachChild(robotLive);
            } else if (physical instanceof WallImpl) {
                walls.add((WallImpl) physical);
            }
        }

        Vector3f camPos = cam.getLocation();
        KPoint newPos = game.getPlayer().getBox().getPosition();
        camPos.setZ((float) newPos.x);
        camPos.setX((float) newPos.y);
        cam.setLocation(camPos);
        cam.setRotation(new Quaternion().fromAngleAxis(game.getPlayer().getInitAngle(), new Vector3f(0, 1, 0)));
        float aspect = (float) cam.getWidth() / (float) cam.getHeight();
        cam.setFrustumPerspective(70f, aspect, 0.01f, 200f);

        // GUI
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
//        nifty.fromXml("models/gui/label.xml", "GScreen0");
//        nifty.setDebugOptionPanelColors(true);
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");

        nifty.addScreen("LabelScreen", new ScreenBuilder("Label Nifty Screen") {{
            controller(new DefaultScreenController()); // Screen properties
            layer(new LayerBuilder("Layer1") {{
                childLayoutVertical(); // layer properties, add more...
                panel(new PanelBuilder("Panel1") {{
                    childLayoutCenter(); // panel properties, add more...
                    control(new LabelBuilder("label", "") {{
                        alignCenter();
                        valignCenter();
                        height("5%");
                        width("100%");
                    }});
                }});
            }});
        }}.build(nifty));
        nifty.gotoScreen("LabelScreen"); // start the screen
        guiViewPort.addProcessor(niftyDisplay);

        /* Drop shadows */
        final int SHADOWMAP_SIZE = 2048;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        dlsr.setEdgesThickness(2);
        dlsr.setShadowIntensity(0.5f);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);
    }

    @Override
    public void destroy() {
        super.destroy();
        System.exit(0);
    }

    private Vector3f getTerrainPoint(float x, float z) {
        return getTerrainPoint(x, z, false);
    }

    private Vector3f getTerrainPoint(float x, float z, boolean failOnNoResult) {
        x += 0.001;
        z += 0.001;
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray();
        Vector3f pos = new Vector3f(x, 1000, z);
        Vector3f dir = new Vector3f(x, 999, z);
        dir.subtractLocal(pos).normalizeLocal();
        ray.setOrigin(pos);
        ray.setDirection(dir);
        terrain.collideWith(ray, results);
        CollisionResult result = results.getClosestCollision();
        if (result == null) {
            if (failOnNoResult) {
                throw new TerrainNotFoundException();
            } else {
                return new Vector3f(x, 0, z);
            }
        }
        return result.getContactPoint();
    }

    private RobotImpl getTargetRobot(Vector3f position, Vector3f direction) {
        rootNode.updateGeometricState();
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray();
        ray.setOrigin(position);
        ray.setDirection(direction);
        rootNode.collideWith(ray, results);
        CollisionResult result = results.getClosestCollision();
        if (result == null || result.getDistance() > 3) {
            return null;
        }
        for (Map.Entry<RobotImpl, Node> e : robotMap.entrySet()) {
            if (e.getValue().hasChild(result.getGeometry())) {
                return e.getKey();
            }
        }
        return null;
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (!isSceneRendered()) {
            return;
        }
        updateRobots();
        updatePlayer();
    }

    private boolean isSceneRendered() {
        try {
            getTerrainPoint(cam.getLocation().getX(), cam.getLocation().getZ());
        } catch (TerrainNotFoundException e) {
            return false;
        }
        return true;
    }

    private void updatePlayer() {
        Vector3f cam = getCamera().getLocation();
        game.getPlayer().getBox().setPosition(cam.z, cam.x);
        game.stepSync();
        KPoint newPos = game.getPlayer().getBox().getPosition();
        cam.setY(getTerrainPoint((float) newPos.y, (float) newPos.x).y + 1.3f);
        cam.setZ((float) newPos.x);
        cam.setX((float) newPos.y);
        getCamera().setLocation(cam);

        RobotImpl targetRobot = getTargetRobot(cam, getCamera().getDirection());
        Element label = nifty.getCurrentScreen().findElementByName("label");
        if (label != null) {
            label.getRenderer(TextRenderer.class).setText(targetRobot == null ? "" : targetRobot.getUid());
        }
    }

    private void updateRobots() {
        for (Map.Entry<RobotImpl, Node> e : robotMap.entrySet()) {
            KPoint point = e.getKey().getBox().getPosition();
            float x = (float) point.getY();
            float z = (float) point.getX();
            float angle = (float) e.getKey().getBox().getAngle() + FastMath.PI;
            Node node = e.getValue();
            moveRobot(e.getKey().getUid(), x, z, angle, node);
            Chassis chassis = e.getKey().getEquipment(Chassis.class);
            if (chassis != null && chassis.isWorking()) {
                robotModel.animateChassis(node);
            }
        }
    }

    private void moveRobot(String uid, float x, float z, float angle, Node robot) {
        float size = RobotBox.getSize(uid);

        Quaternion yaw = new Quaternion();
        yaw.fromAngleAxis(angle, new Vector3f(0, 1, 0));

        float radius = size / 2;

        Vector3f x1, x2, z1, z2;
        x1 = getTerrainPoint(x - radius, z);
        x2 = getTerrainPoint(x + radius, z);
        z1 = getTerrainPoint(x, z - radius);
        z2 = getTerrainPoint(x, z + radius);

        Vector3f zAxis = z2.subtract(z1);
        Vector3f xAxis = x2.subtract(x1);
        Vector3f yAxis = new Vector3f(0, 1, 0);
        Quaternion quat = new Quaternion().fromAxes(
                xAxis,
                yAxis,
                zAxis
        );
        robot.setLocalRotation(quat.mult(yaw));
        robot.setLocalTranslation(x, Math.min(x1.y + x2.y, z1.y + z2.y) / 2, z);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }


    private DirectionalLight createSun() {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(1f));
        sun.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        return sun;
    }

    private AmbientLight createAmbient() {
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1f));
        return al;
    }
}
