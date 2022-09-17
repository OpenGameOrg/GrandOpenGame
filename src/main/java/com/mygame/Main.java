package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import jme3utilities.sky.SkyControl;
import jme3utilities.sky.StarsOption;
import jme3utilities.sky.SunAndStars;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    private final Vector3f playerWalkDirection = Vector3f.ZERO;
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private CharacterControl playerControl;
    private Camera camera;
    private Vector3f camDir;
    private Vector3f camLeft;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        camera = getCamera();

        var scene = assetManager.loadModel("Scenes/yivysky-osm.j3o");
        
        var bulletAppState = new BulletAppState();
        bulletAppState.setDebugEnabled(true);
        stateManager.attach(bulletAppState);
                
        setupLight();
        setupSky();
        
        rootNode.attachChild(scene);
        var floor = rootNode.getChild("Floor");
        var floorControl = new RigidBodyControl(0);
        floor.addControl(floorControl);
        bulletAppState.getPhysicsSpace().add(floorControl);
        
        var player = rootNode.getChild("Player");                                                               
        var box = (BoundingBox) player.getWorldBound();
        var radius = box.getXExtent();
        var height = box.getYExtent();
        var shape = new CapsuleCollisionShape(radius, height);
        playerControl = new CharacterControl(shape, 1.0f);
        player.addControl(playerControl);
        
        bulletAppState.getPhysicsSpace().add(playerControl);
        
        this.getFlyByCamera().setEnabled(false);
        var chaseCamera = new ChaseCamera(camera, player, inputManager);
        
        addInputMappings();
                                                                                         
    }
    
    private void setupLight() {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        rootNode.addLight(sun);
        rootNode.addLight(new AmbientLight());
        
        rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        var shadowRenderer
                = new DirectionalLightShadowRenderer(assetManager, 4_096, 4);
        ViewPort viewPort = getViewPort();
        viewPort.addProcessor(shadowRenderer);
        shadowRenderer.setEdgeFilteringMode(EdgeFilteringMode.PCFPOISSON);
        shadowRenderer.setLight(sun);
        shadowRenderer.setShadowIntensity(0.3f);
        shadowRenderer.setShadowZExtend(256f);
        shadowRenderer.setShadowZFadeLength(128f);
        
    }
    
    private void setupSky() {
//        var sky = SkyFactory.createSky(assetManager,
//                "Textures/Sky/Bright/BrightSky.dds",
//                SkyFactory.EnvMapType.CubeMap);
//        
          var sky = new SkyControl(assetManager, camera,
                  0.5f, StarsOption.TopDome, true);
                             
          rootNode.addControl(sky);
         
          sky.setSolarDiameter(0.1f);
          sky.setCloudsRate(0.7f);
          sky.setCloudiness(0.8f);
          sky.setCloudsYOffset(0.4f);
          
          SunAndStars sunAndStars = sky.getSunAndStars();
          sunAndStars.setHour(16f); // 4 p.m.
          sunAndStars.setObserverLatitude(0.63f); // 36 degrees north
          
          sky.setEnabled(true);
    }

    private void addInputMappings() {
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(createInputListener(), "Up", "Down", "Left", "Right", "Jump");
    }
    
    private ActionListener createInputListener() {
        return (name, keyPressed, tpf) -> {
            if (name.equals("Up") ) {
                up = keyPressed;
            } else if (name.equals("Down")) {
                down = keyPressed;
            } else if (name.equals("Left")) {
                left = keyPressed;
            } else if (name.equals("Right")) {
                right = keyPressed;
            } else if (name.equals("Jump")) {
                playerControl.jump();
            }
        };
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        camDir = camera.getDirection().clone();
        camLeft = camera.getLeft().clone();
        
        camDir.y = 0;
        camLeft.y = 0;
        
        camDir.normalizeLocal();
        camLeft.normalizeLocal();
        
        playerWalkDirection.set(0, 0, 0);
        
        if (left) playerWalkDirection.addLocal(camLeft);
        if (right) playerWalkDirection.addLocal(camLeft.negate() );
        if (up) playerWalkDirection.addLocal(camDir);
        if (down) playerWalkDirection.addLocal(camDir.negate());
        
        playerWalkDirection.multLocal(10f).multLocal(tpf);
        playerControl.setWalkDirection(playerWalkDirection);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}

