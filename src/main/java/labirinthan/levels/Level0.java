package labirinthan.levels;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import labirinthan.Labirinthan;
import labirinthan.levels.parts.Cross;
import labirinthan.levels.parts.Decoration;

public class Level0 extends Level {


    public Level0(Labirinthan application, BulletAppState bulletAppState, Node guiNode, AppSettings settings) {
        super(application, "Level0", guiNode, settings);
        this.bulletAppState = bulletAppState;
    }

    @Override
    public void initialize(AppStateManager sm, Application application) {
        super.initialize(sm, application);

        // Adding walls
        blocksInfo.add(buildBlock10(0,0));
        //makeFrameWalls(0,0);

        //addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, 0+(wallWidth * 3 + passageWidth * 6) / 2, wallHeight / 2, 0+wallWidth / 2);

        for(int a=2;a<blocksInfo.get(blocksInfo.size()-1).size();a++){
            blocksDecorationInfo.add(blocksInfo.get(blocksInfo.size()-1).get(a));
        }


        //floor = new Floor(wallWidth * 6 + passageWidth * 5, 0.1f, wallWidth * 6 + passageWidth * 5, assetManager, localRootNode, (wallWidth * 6 + passageWidth * 5)/2, -0.05f, (wallWidth * 6 + passageWidth * 5)/2, bulletAppState);

        //ceiling = new Ceiling(wallWidth * 6 + passageWidth * 5, 0.1f, wallWidth * 6 + passageWidth * 5, assetManager, localRootNode, (wallWidth * 6 + passageWidth * 5)/2, wallHeight-0.05f, (wallWidth * 6 + passageWidth * 5)/2, bulletAppState);
        cross = new Cross(passageWidth, 0.1f, passageWidth, assetManager, localRootNode, blocksInfo.get(0).get(0), -0.05f, blocksInfo.get(0).get(1), bulletAppState);


        for(int i=0;i<blocksDecorationInfo.size()-1;i+=2){
            Decoration decoration = new Decoration(passageWidth, 0.1f, passageWidth, assetManager, localRootNode, blocksDecorationInfo.get(i), -0.05f, blocksDecorationInfo.get(i+1), bulletAppState);
        }

        //Closing extras
        addWall((wallWidth * 6 + passageWidth * 5), wallHeight, wallWidth, (wallWidth * 6 + passageWidth * 5) / 2, wallHeight / 2, wallWidth / 2);
        addWall(wallWidth, wallHeight, (wallWidth * 6 + passageWidth * 5), wallWidth / 2, wallHeight / 2, (wallWidth * 6 + passageWidth * 5) / 2);
        addWall((wallWidth * 6 + passageWidth * 5), wallHeight, wallWidth, (wallWidth * 6 + passageWidth * 5) / 2, wallHeight / 2, wallWidth * 5.5f + passageWidth * 5);
        addWall(wallWidth, wallHeight, (wallWidth * 6 + passageWidth * 5), wallWidth * 5.5f + passageWidth * 5, wallHeight / 2, (wallWidth * 6 + passageWidth * 5) / 2);

        this.application.getViewPort().addProcessor(this.application.filterPostProcessor);
    }


}