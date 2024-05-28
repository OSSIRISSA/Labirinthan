package labirinthan;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;

import java.util.ArrayList;

public class Level extends AbstractAppState {
    protected final Node rootNode;
    protected final Node localRootNode;
    protected final AssetManager assetManager;
    public static float wallHeight = 6;
    public static float wallWidth = 1;
    public static float passageWidth = 4;
    public Floor floor;
    public Ceiling ceiling;
    public Cross cross;
    protected ArrayList<Wall> walls = new ArrayList<>();
    protected ArrayList<ArrayList<Float>> blocksInfo = new ArrayList<>();
    protected BulletAppState bulletAppState;

    public Level(SimpleApplication application, String name) {
        this.rootNode = application.getRootNode();
        localRootNode = new Node(name);
        assetManager = application.getAssetManager();
    }

    @Override
    public void initialize(AppStateManager sm, Application application){
        super.initialize(sm,application);
        rootNode.attachChild(localRootNode);
    }

    @Override
    public void cleanup(){
        rootNode.detachChild(localRootNode);
        super.cleanup();
    }
    public void addWall(float x, float y, float z, float px, float py, float pz) {
        walls.add(new Wall(x, y, z, assetManager, localRootNode, px, py, pz, bulletAppState));
    }

    public ArrayList<Float> buildBlock1(float startX, float startZ){
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX+(wallWidth * 3 + passageWidth * 2) / 2, wallHeight / 2, startZ+wallWidth / 2);
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth / 2);
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX+(wallWidth * 3 + passageWidth * 2) / 2, wallHeight / 2, startZ+wallWidth * 5.5f + passageWidth * 5);
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth * 5.5f + passageWidth * 5);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth / 2, wallHeight / 2, startZ+(wallWidth * 3 + passageWidth * 2) / 2);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth * 5.5f + passageWidth * 5, wallHeight / 2, startZ+(wallWidth * 3 + passageWidth * 2) / 2);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth * 5.5f + passageWidth * 5, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth / 2, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);


        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, startX+wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ+(wallWidth * 2 + passageWidth) / 2);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, startX+wallWidth * 1.5f + passageWidth, wallHeight / 2, startZ+wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 4 + passageWidth * 3, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, startX+wallWidth * 1.5f + passageWidth, wallHeight / 2, startZ+wallWidth * 4 + passageWidth * 3.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, startX+wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ+wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX+wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 4 + passageWidth * 3, wallHeight, wallWidth, startX+wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX+wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        ArrayList<Float> res = new ArrayList<>();
        res.add(startX+wallWidth*3+passageWidth*2.5f);
        res.add(startZ+wallWidth*2+passageWidth*1.5f);
        return res;
    }

    public ArrayList<Float> buildBlock2(float startX, float startZ){
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX+(wallWidth * 3 + passageWidth * 2) / 2, wallHeight / 2, startZ+wallWidth / 2);
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth / 2);
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX+(wallWidth * 3 + passageWidth * 2) / 2, wallHeight / 2, startZ+wallWidth * 5.5f + passageWidth * 5);
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth * 5.5f + passageWidth * 5);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth / 2, wallHeight / 2, startZ+(wallWidth * 3 + passageWidth * 2) / 2);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth * 5.5f + passageWidth * 5, wallHeight / 2, startZ+(wallWidth * 3 + passageWidth * 2) / 2);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth * 5.5f + passageWidth * 5, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth / 2, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);


        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth*2.5f+passageWidth*2, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, startX+wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ+wallWidth * 2 + passageWidth*1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 4 + passageWidth * 3, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth * 1.5f + passageWidth * 1, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, startX+wallWidth*3.5f+passageWidth*3, wallHeight / 2, startZ+wallWidth * 4 + passageWidth * 3.5f);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX+wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX+wallWidth*4+passageWidth*3.5f);
        res.add(startZ+wallWidth*4+passageWidth*3.5f);
        return res;
    }
}
