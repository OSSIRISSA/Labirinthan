package labirinthan;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapFont;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;
import com.jme3.system.AppSettings;
import labirinthan.GUI.MainMenu;

public class Labirinthan extends SimpleApplication {

    private BulletAppState bulletAppState;
    public static final float X = Level.wallWidth*1+Level.passageWidth*0.5f;
    public static final float Y = 0;
    public static final float Z = Level.wallWidth*3+Level.passageWidth*2.5f;


    public static boolean isFlying = true;

    public static void main(String[] args) {
        Labirinthan app = new Labirinthan();

        // Set the game to fullscreen
        AppSettings settings = new AppSettings(true);
        settings.setFullscreen(true);
        settings.setResolution(1920, 1080); // Set your desired resolution
        settings.setTitle("Labirinthan");
        app.setSettings(settings);
        app.setShowSettings(false); // Disable the settings dialog
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //init main folder for assets
        assetManager.registerLocator("assets/", FileLocator.class);

        // Initialize Main Menu GUI
        //MainMenu mainMenu = new MainMenu(this, guiNode, settings, assetManager);
        //mainMenu.createHomeScreen();

        //flyCam.setMoveSpeed(100);

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
    }

    public void startLevel0() {
        Level1 level1 = new Level1(this, bulletAppState);
        stateManager.attach(level1);
    }
}
