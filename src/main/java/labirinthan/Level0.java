package labirinthan;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class Level0 extends Level {


    public Level0(SimpleApplication application, BulletAppState bulletAppState) {
        super(application, "Level0");
        this.bulletAppState = bulletAppState;
    }

    @Override
    public void initialize(AppStateManager sm, Application application) {
        super.initialize(sm, application);

        // Adding walls
        blocksInfo.add(buildBlock4(0,0));

        // Adding floor
        floor = new Floor(wallWidth * 6 + passageWidth * 5, 0.1f, wallWidth * 6 + passageWidth * 5, assetManager, localRootNode, (wallWidth * 6 + passageWidth * 5)/2, -0.05f, (wallWidth * 6 + passageWidth * 5)/2, bulletAppState);
        ceiling = new Ceiling(wallWidth * 6 + passageWidth * 5, 0.1f, wallWidth * 6 + passageWidth * 5, assetManager, localRootNode, (wallWidth * 6 + passageWidth * 5)/2, wallHeight-0.05f, (wallWidth * 6 + passageWidth * 5)/2, bulletAppState);
        cross = new Cross(passageWidth, 0.1f, passageWidth, assetManager, localRootNode, blocksInfo.get(0).get(0), -0.1f, blocksInfo.get(0).get(1), bulletAppState);

        //Closing extras
        super.addWall((wallWidth * 6 + passageWidth * 5), wallHeight, wallWidth, (wallWidth * 6 + passageWidth * 5) / 2, wallHeight / 2, wallWidth / 2);
        super.addWall(wallWidth, wallHeight, (wallWidth * 6 + passageWidth * 5), wallWidth / 2, wallHeight / 2, (wallWidth * 6 + passageWidth * 5) / 2);
        super.addWall((wallWidth * 6 + passageWidth * 5), wallHeight, wallWidth, (wallWidth * 6 + passageWidth * 5) / 2, wallHeight / 2, wallWidth * 5.5f + passageWidth * 5);
        super.addWall(wallWidth, wallHeight, (wallWidth * 6 + passageWidth * 5), wallWidth * 5.5f + passageWidth * 5, wallHeight / 2, (wallWidth * 6 + passageWidth * 5) / 2);


        // Adding light
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(10, 10, 10).normalizeLocal());
        localRootNode.addLight(sun);
    }

}