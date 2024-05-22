package labirinthan;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.sql.Array;
import java.util.ArrayList;

public class Level extends AbstractAppState {
    protected final Node rootNode;
    protected final Node localRootNode;
    protected final AssetManager assetManager;
    public float wallHeight = 6;
    public float wallWidth = 1;
    public float passageWidth = 4;
    public Floor floor;

    protected ArrayList<Wall> walls = new ArrayList<>();

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
}
