package labirinthan;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class Level0 extends Level {
    public Level0(SimpleApplication application) {
        super(application, "Level0");
    }

    @Override
    public void initialize(AppStateManager sm, Application application) {
        super.initialize(sm, application);
        walls.add(new Wall((wallWidth*6+passageWidth*5), wallHeight, wallWidth, assetManager, localRootNode, (wallWidth*6+passageWidth*5)/2, wallHeight/2, wallWidth/2));
        walls.add(new Wall(wallWidth, wallHeight, wallWidth*3+passageWidth*2, assetManager, localRootNode, wallWidth/2, wallHeight/2, (wallWidth*3+passageWidth*2)/2));
        walls.add(new Wall(wallWidth, wallHeight, wallWidth*2+passageWidth*1, assetManager, localRootNode, wallWidth*3.5f+passageWidth*3, wallHeight/2, (wallWidth*2+passageWidth*1)/2));
        walls.add(new Wall(wallWidth, wallHeight, wallWidth*3+passageWidth*2, assetManager, localRootNode, wallWidth*5.5f+passageWidth*5, wallHeight/2, (wallWidth*3+passageWidth*2)/2));
        walls.add(new Wall(wallWidth, wallHeight, wallWidth*3+passageWidth*2, assetManager, localRootNode, wallWidth*5.5f+passageWidth*5, wallHeight/2, wallWidth*4.5f+passageWidth*4,ColorRGBA.Cyan));
        walls.add(new Wall(wallWidth, wallHeight, wallWidth*3+passageWidth*2, assetManager, localRootNode, wallWidth/2, wallHeight/2, wallWidth*4.5f+passageWidth*4,ColorRGBA.Brown));
        walls.add(new Wall(wallWidth, wallHeight, wallWidth*2+passageWidth, assetManager, localRootNode, wallWidth*1.5f+passageWidth, wallHeight/2, wallWidth*2+passageWidth*1.5f,ColorRGBA.Red));
        walls.add(new Wall(wallWidth, wallHeight, wallWidth*4+passageWidth*3, assetManager, localRootNode, wallWidth*4.5f+passageWidth*4, wallHeight/2, wallWidth*3+passageWidth*2.5f,ColorRGBA.Pink));
        walls.add(new Wall(wallWidth, wallHeight, wallWidth*2+passageWidth, assetManager, localRootNode, wallWidth*1.5f+passageWidth, wallHeight/2, wallWidth*4+passageWidth*3.5f,ColorRGBA.Green));
        walls.add(new Wall(wallWidth, wallHeight, wallWidth*2+passageWidth, assetManager, localRootNode, wallWidth*3.5f+passageWidth*3, wallHeight/2, wallWidth*3+passageWidth*2.5f,ColorRGBA.Red));
        walls.add(new Wall((wallWidth*6+passageWidth*5), wallHeight, wallWidth, assetManager, localRootNode, (wallWidth*6+passageWidth*5)/2, wallHeight/2, wallWidth*5.5f+passageWidth*5));
        walls.add(new Wall(wallWidth*3+passageWidth*2, wallHeight, wallWidth, assetManager, localRootNode, wallWidth*2.5f+passageWidth*2, wallHeight/2, wallWidth*1.5f+passageWidth*1));
        walls.add(new Wall(wallWidth*4+passageWidth*3, wallHeight, wallWidth, assetManager, localRootNode, wallWidth*3+passageWidth*2.5f, wallHeight/2, wallWidth*2.5f+passageWidth*2));
        walls.add(new Wall(wallWidth*3+passageWidth*2, wallHeight, wallWidth, assetManager, localRootNode, wallWidth*2.5f+passageWidth*2, wallHeight/2, wallWidth*1.5f+passageWidth*1));
        walls.add(new Wall(wallWidth*2+passageWidth*1, wallHeight, wallWidth, assetManager, localRootNode, wallWidth*1+passageWidth*0.5f, wallHeight/2, wallWidth*3.5f+passageWidth*3,ColorRGBA.Yellow));
        walls.add(new Wall(wallWidth*2+passageWidth*1, wallHeight, wallWidth, assetManager, localRootNode, wallWidth*3+passageWidth*2.5f, wallHeight/2, wallWidth*3.5f+passageWidth*3,ColorRGBA.Yellow));
        walls.add(new Wall(wallWidth*2+passageWidth*1, wallHeight, wallWidth, assetManager, localRootNode, wallWidth*2+passageWidth*1.5f, wallHeight/2, wallWidth*4.5f+passageWidth*4,ColorRGBA.Yellow));
        walls.add(new Wall(wallWidth*2+passageWidth*1, wallHeight, wallWidth, assetManager, localRootNode, wallWidth*4+passageWidth*3.5f, wallHeight/2, wallWidth*4.5f+passageWidth*4,ColorRGBA.Yellow));
        floor = new Floor(50, 0, 50, assetManager, localRootNode, 0,0,0);
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(10,10,10).normalizeLocal());
        localRootNode.addLight(sun);
    }
}
