package labirinthan;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import java.util.Random;
import java.util.random.RandomGenerator;

public class Level1 extends Level {


    public Level1(SimpleApplication application, BulletAppState bulletAppState) {
        super(application, "Level1");
        this.bulletAppState = bulletAppState;
    }

    @Override
    public void initialize(AppStateManager sm, Application application) {
        super.initialize(sm, application);
        labyrinthSizeX = 3;
        labyrinthSizeZ = 3;
        clearSpan = 1;

        float currentX;
        float currentZ;
        for(int i=0;i<labyrinthSizeX;i++){
            for(int j=0;j<labyrinthSizeZ;j++){
                currentX = i*5*(wallWidth+passageWidth);
                currentZ = j*5*(wallWidth+passageWidth);
                int blockNumber = random.nextInt(1,5);
                switch (blockNumber){
                    case 1 -> blocksInfo.add(buildBlock1(currentX,currentZ));
                    case 2 -> blocksInfo.add(buildBlock2(currentX,currentZ));
                    case 3 -> blocksInfo.add(buildBlock3(currentX,currentZ));
                    case 4 -> blocksInfo.add(buildBlock4(currentX,currentZ));
                }
                for(int a=2;a<blocksInfo.get(blocksInfo.size()-1).size();a++){
                    blocksDecorationInfo.add(blocksInfo.get(blocksInfo.size()-1).get(a));
                }
                if((i<clearSpan)&&(j<clearSpan)){
                    blocksInfo.remove(blocksInfo.size()-1);
                }
            }
        }


        // Adding floor
        floor = new Floor(labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth, 0.1f, labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth, assetManager, localRootNode, (labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth)/2, -0.05f, (labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth)/2, bulletAppState);
        ceiling = new Ceiling(labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth, 0.1f, labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth, assetManager, localRootNode, (labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth)/2, wallHeight-0.05f, (labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth)/2, bulletAppState);
        int chooseCross = random.nextInt(blocksInfo.size());
        cross = new Cross(passageWidth, 0.1f, passageWidth, assetManager, localRootNode, blocksInfo.get(chooseCross).get(0), -0.05f, blocksInfo.get(chooseCross).get(1), bulletAppState);

        for(int i=0;i<blocksDecorationInfo.size()-1;i+=2){
            Decoration decoration = new Decoration(passageWidth, 0.1f, passageWidth, assetManager, localRootNode, blocksDecorationInfo.get(i), -0.05f, blocksDecorationInfo.get(i+1), bulletAppState);
        }

        //Closing extras
        super.addWall(labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth, wallHeight, wallWidth, (labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth) / 2, wallHeight / 2, wallWidth / 2);
        super.addWall(wallWidth, wallHeight, labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth, wallWidth / 2, wallHeight / 2, (labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth) / 2);
        super.addWall(labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth, wallHeight, wallWidth, (labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth) / 2, wallHeight / 2, labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth*0.5f);
        super.addWall(wallWidth, wallHeight, labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth, labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth*0.5f, wallHeight / 2, (labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth) / 2);


        // Adding light
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(10, 10, 10).normalizeLocal());
        localRootNode.addLight(sun);
    }

}