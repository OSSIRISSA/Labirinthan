package labirinthan;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class Level0 extends Level {

    private BulletAppState bulletAppState;

    public Level0(SimpleApplication application, BulletAppState bulletAppState) {
        super(application, "Level0");
        this.bulletAppState = bulletAppState;
    }

    @Override
    public void initialize(AppStateManager sm, Application application) {
        super.initialize(sm, application);

        // Adding walls
        addWall((wallWidth * 6 + passageWidth * 5), wallHeight, wallWidth, (wallWidth * 6 + passageWidth * 5) / 2, wallHeight / 2, wallWidth / 2);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, wallWidth / 2, wallHeight / 2, (wallWidth * 3 + passageWidth * 2) / 2);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, (wallWidth * 2 + passageWidth) / 2);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, wallWidth * 5.5f + passageWidth * 5, wallHeight / 2, (wallWidth * 3 + passageWidth * 2) / 2);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, wallWidth * 5.5f + passageWidth * 5, wallHeight / 2, wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, wallWidth / 2, wallHeight / 2, wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, wallWidth * 1.5f + passageWidth, wallHeight / 2, wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 4 + passageWidth * 3, wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, wallWidth * 1.5f + passageWidth, wallHeight / 2, wallWidth * 4 + passageWidth * 3.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, wallWidth * 3 + passageWidth * 2.5f);
        addWall((wallWidth * 6 + passageWidth * 5), wallHeight, wallWidth, (wallWidth * 6 + passageWidth * 5) / 2, wallHeight / 2, wallWidth * 5.5f + passageWidth * 5);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 4 + passageWidth * 3, wallHeight, wallWidth, wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, wallWidth * 4.5f + passageWidth * 4);

        // Adding floor
        floor = new Floor(50, 0.1f, 50, assetManager, localRootNode, 0, -0.1f, 0, bulletAppState);

        // Adding light
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(10, 10, 10).normalizeLocal());
        localRootNode.addLight(sun);
    }

    private void addWall(float x, float y, float z, float px, float py, float pz) {
        walls.add(new Wall(x, y, z, assetManager, localRootNode, px, py, pz, bulletAppState));
    }
}