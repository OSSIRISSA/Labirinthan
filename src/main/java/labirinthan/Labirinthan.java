package labirinthan;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;

public class Labirinthan extends SimpleApplication {

    private BulletAppState bulletAppState;
    public static final float X = Level.wallWidth*1+Level.passageWidth*0.5f;
    public static final float Y = 0;
    public static final float Z = Level.wallWidth*3+Level.passageWidth*2.5f;


    public static void main(String[] args) {
        Labirinthan app = new Labirinthan();
        app.setShowSettings(true);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator("assets/", FileLocator.class);
        flyCam.setMoveSpeed(100);

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // Enable physics debug
        //bulletAppState.setDebugEnabled(true);

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(10f));
        rootNode.addLight(al);

        startLevel0();
        stateManager.attach(new MainCharacter());
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

    public void startLevel0() {
        Level0 level0 = new Level0(this, bulletAppState);
        stateManager.attach(level0);
    }
}