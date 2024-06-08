package labirinthan;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.system.AppSettings;
import labirinthan.GUI.MainHUD;
import labirinthan.GUI.MainMenu;
import labirinthan.character.MainCharacter;
import labirinthan.levels.Level;
import labirinthan.levels.Level0;
import labirinthan.levels.Level1;

public class Labirinthan extends SimpleApplication {

    public BulletAppState bulletAppState;
    public MainCharacter character;
    private MainHUD mainHUD;
    public FilterPostProcessor filterPostProcessor;

    public static final float X = Level.wallWidth * 1 + Level.passageWidth * 0.5f;
    public static final float Y = 0;
    public static final float Z = Level.wallWidth * 3 + Level.passageWidth * 2.5f;
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
        filterPostProcessor = new FilterPostProcessor(assetManager);
        //init main folder for assets
        stateManager.detach(stateManager.getState(StatsAppState.class));
        assetManager.registerLocator("assets/", FileLocator.class);

        // Initialize Main Menu GUI
        MainMenu mainMenu = new MainMenu(this, guiNode, settings, assetManager);
        mainMenu.createHomeScreen();

        //init Physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // Enable physics debug
        //bulletAppState.setDebugEnabled(true);

        //COMMENT THIS IF YOU WANT TO INTERACT WITH MAIN MENU
        //startGame();
        //THIS

        flyCam.setMoveSpeed(10);
    }

    public void startGame() {
        guiNode.detachAllChildren();

        level = new Level0(this, bulletAppState, guiNode, settings);
        stateManager.attach(level);
        addFog();

        mainHUD = new MainHUD(guiNode, settings, assetManager);
        mainHUD.createMainHUD();
        character = new MainCharacter(mainHUD, settings);
        stateManager.attach(character);

        levelPreparation(false);
    }

    public void levelPreparation(boolean toTurnOff) {
        inputManager.setCursorVisible(toTurnOff);
        flyCam.setEnabled(!toTurnOff);
    }

    private void addFog() {
        FogFilter fog = new FogFilter();
        fog.setFogColor(new ColorRGBA(0.01f, 0.01f, 0.01f, 1f));
        fog.setFogDistance(600f);
        fog.setFogDensity(4.0f);

        filterPostProcessor.addFilter(fog);
    }

    public void startLevel1() {
        guiNode.attachChild(level.localPuzzleNode);
        stateManager.detach(character);
        stateManager.detach(level);
        bulletAppState.cleanup();
        stateManager.detach(bulletAppState);

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.setDebugEnabled(true);

        level = new Level1(this, bulletAppState, guiNode, settings);
        stateManager.attach(level);
        stateManager.attach(character);

        levelPreparation(false);

        character.isPuzzleFound = false;
    }
}