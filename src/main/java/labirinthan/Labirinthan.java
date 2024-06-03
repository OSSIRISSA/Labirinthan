package labirinthan;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import labirinthan.GUI.MainMenu;

import java.awt.*;

public class Labirinthan extends SimpleApplication {

    private BulletAppState bulletAppState;
    public static final float X = Level.wallWidth*1+Level.passageWidth*0.5f;
    public static final float Y = 0;
    public static final float Z = Level.wallWidth*3+Level.passageWidth*2.5f;
    public static Level level;

    public static boolean isFlying = false;


    public static void main(String[] args) {
        Labirinthan app = new Labirinthan();

        // Set the game to fullscreen
        AppSettings settings = new AppSettings(true);
        settings.setFullscreen(true);
        settings.setResolution(1920, 1080);
        settings.setTitle("Labirinthan");
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //init main folder for assets
        assetManager.registerLocator("assets/", FileLocator.class);

        // Initialize Main Menu GUI
        //MainMenu mainMenu = new MainMenu(this, guiNode, settings, assetManager);
        //mainMenu.createHomeScreen();

        //init Physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // Enable physics debug
        //bulletAppState.setDebugEnabled(true);

        //Add All lighted map          ----------(TEMPORARY)----------
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(10f));
        rootNode.addLight(al);

        startGame();
        flyCam.setMoveSpeed(100);
    }

    public void startGame() {
        guiNode.detachAllChildren(); // Remove the home screen elements

        startLevel0();
        stateManager.attach(new MainCharacter());

        // Hide the mouse cursor and enable game input
        inputManager.setCursorVisible(false);
        flyCam.setEnabled(true);

        //temporary add character
        //Spatial character = assetManager.loadModel("Models/scene/scene.j3o");
        //rootNode.attachChild(character);
        //AnimComposer composer = character.getControl(AnimComposer.class);
        //System.out.println(composer);
    }

    public void stopLevel() {
        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);
    }

    public void startLevel0() {
        Level1 level1 = new Level1(this, bulletAppState);
        stateManager.attach(level1);
        addFog();
    }

    private void addFog(){
        FogFilter fog = new FogFilter();
        fog.setFogColor(new ColorRGBA(179/255f,229/255f,251/255f,1f));
        fog.setFogDistance(100f); // Distance at which fog starts
        fog.setFogDensity(4.0f); // Density of the fog

        // Add the fog filter to the post processor
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(fog);
        viewPort.addProcessor(fpp);
        level = new Level0(this, bulletAppState, guiNode, settings);
        stateManager.attach(level);
    }

    public void startLevel1() {
        guiNode.detachAllChildren(); // Remove the home screen elements
        stateManager.detach(level);

        level = new Level1(this, bulletAppState, guiNode, settings);
        stateManager.attach(level);
        stateManager.attach(new MainCharacter());

        // Hide the mouse cursor and enable game input
        inputManager.setCursorVisible(false);
        flyCam.setEnabled(true);

    }
}
