package labirinthan.levels;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import labirinthan.Labirinthan;
import labirinthan.levels.parts.Cross;

import java.util.ArrayList;

public class Level1 extends Level {


    public Level1(Labirinthan application, BulletAppState bulletAppState, Node guiNode, AppSettings settings) {
        super(application, "Level1", guiNode, settings);
        this.labyrinthSizeX = 3;
        this.labyrinthSizeZ = 3;
        this.currentLabyrinthSizeX = 1;
        this.currentLabyrinthSizeZ = 1;
        this.bulletAppState = bulletAppState;
        this.blocks = new Node[labyrinthSizeX][labyrinthSizeZ];
        this.allTorches = new ArrayList[labyrinthSizeX][labyrinthSizeZ];
    }

    @Override
    public void initialize(AppStateManager sm, Application application) {
        super.initialize(sm, application);
        clearSpan = 1;

        float currentX;
        float currentZ;
        for(int i=0;i<labyrinthSizeX;i++){
            for(int j=0;j<labyrinthSizeZ;j++){
                currentX = i*5*(wallWidth+passageWidth);
                currentZ = j*5*(wallWidth+passageWidth);

                allTorches[i][j] = new ArrayList<>();
                blocks[i][j]= new Node();
                int blockNumber = random.nextInt(1,11);
                switch (blockNumber){
                    case 1 -> blocksInfo.add(buildBlock1(currentX,currentZ,blocks[i][j],labyrinthSizeX*labyrinthSizeZ,allTorches[i][j]));
                    case 2 -> blocksInfo.add(buildBlock2(currentX,currentZ,blocks[i][j],labyrinthSizeX*labyrinthSizeZ,allTorches[i][j]));
                    case 3 -> blocksInfo.add(buildBlock3(currentX,currentZ,blocks[i][j],labyrinthSizeX*labyrinthSizeZ,allTorches[i][j]));
                    case 4 -> blocksInfo.add(buildBlock4(currentX,currentZ,blocks[i][j],labyrinthSizeX*labyrinthSizeZ,allTorches[i][j]));
                    case 5 -> blocksInfo.add(buildBlock5(currentX,currentZ,blocks[i][j],labyrinthSizeX*labyrinthSizeZ,allTorches[i][j]));
                    case 6 -> blocksInfo.add(buildBlock6(currentX,currentZ,blocks[i][j],labyrinthSizeX*labyrinthSizeZ,allTorches[i][j]));
                    case 7 -> blocksInfo.add(buildBlock7(currentX,currentZ,blocks[i][j],labyrinthSizeX*labyrinthSizeZ,allTorches[i][j]));
                    case 8 -> blocksInfo.add(buildBlock8(currentX,currentZ,blocks[i][j],labyrinthSizeX*labyrinthSizeZ,allTorches[i][j]));
                    case 9 -> blocksInfo.add(buildBlock9(currentX,currentZ,blocks[i][j],labyrinthSizeX*labyrinthSizeZ,allTorches[i][j]));
                    case 10 -> blocksInfo.add(buildBlock10(currentX,currentZ,blocks[i][j],labyrinthSizeX*labyrinthSizeZ,allTorches[i][j]));
                }
                localRootNode.attachChild(blocks[i][j]);
                if((i<clearSpan)&&(j<clearSpan)){
                    blocksInfo.remove(blocksInfo.size()-1);
                }
                if((i!=0)&&(j!=0)){
                    makeXWall(currentX,currentZ);
                    makeZWall(currentX,currentZ);
                }
                if((i==0)&&(j!=0)){
                    makeZWall(currentX,currentZ);
                }
                if((j==0)&&(i!=0)){
                    makeXWall(currentX,currentZ);
                }
            }
        }


        //Adding floor
        //floor = new Floor(labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth, 0.1f, labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth, assetManager, localRootNode, (labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth)/2, -0.05f, (labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth)/2, bulletAppState);
        //ceiling = new Ceiling(labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth, 0.1f, labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth, assetManager, localRootNode, (labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth)/2, wallHeight-0.05f, (labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth)/2, bulletAppState);
        chooseCross = random.nextInt(blocksInfo.size());
        cross = new Cross(passageWidth, 0.1f, passageWidth, assetManager, localRootNode, blocksInfo.get(chooseCross).get(0), -0.05f, blocksInfo.get(chooseCross).get(1), bulletAppState);

        //for(int i=0;i<blocksDecorationInfo.size()-1;i+=3){
        //}

        //Closing extras
        addWall(labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth, wallHeight, wallWidth, (labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth) / 2, wallHeight / 2, wallWidth / 2);
        addWall(wallWidth, wallHeight, labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth, wallWidth / 2, wallHeight / 2, (labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth) / 2);
        addWall(labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth, wallHeight, wallWidth, (labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth) / 2, wallHeight / 2, labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth*0.5f);
        addWall(wallWidth, wallHeight, labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth, labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth*0.5f, wallHeight / 2, (labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth) / 2);


        this.application.getViewPort().addProcessor(this.application.filterPostProcessor);
    }
    /*@Override
    public void updateBlocks(float x, float z) {
        currentLabyrinthSizeX = 1;
        currentLabyrinthSizeZ = 1;
        localRootNode.detachAllChildren();
        int xPlacement = (int) (x/(wallWidth * 5 + passageWidth * 5));
        int zPlacement = (int) (z/(wallWidth * 5 + passageWidth * 5));
        localRootNode.attachChild(blocks[xPlacement][zPlacement]);
        if(x/(wallWidth * 5 + passageWidth * 5)-xPlacement>0.5f){
            if(xPlacement+1<labyrinthSizeX){
                localRootNode.attachChild(blocks[xPlacement+1][zPlacement]);
                makeXWall((xPlacement+1)*5*(wallWidth+passageWidth), zPlacement*5*(wallWidth+passageWidth));
                currentLabyrinthSizeX++;
            }
        }
        else {
            if(xPlacement-1>-1) {
                localRootNode.attachChild(blocks[xPlacement - 1][zPlacement]);
                makeXWall(xPlacement*5*(wallWidth+passageWidth), zPlacement*5*(wallWidth+passageWidth));
                currentLabyrinthSizeX++;
            }
        }
        if(z/(wallWidth * 5 + passageWidth * 5)-zPlacement>0.5f){
            if(zPlacement+1<labyrinthSizeZ){
                localRootNode.attachChild(blocks[xPlacement][zPlacement+1]);
                makeZWall(xPlacement*5*(wallWidth+passageWidth), (zPlacement+1)*5*(wallWidth+passageWidth));
                currentLabyrinthSizeZ++;
            }
        }
        else {
            if(zPlacement-1>-1){
                localRootNode.attachChild(blocks[xPlacement][zPlacement-1]);
                makeZWall(xPlacement*5*(wallWidth+passageWidth), zPlacement*5*(wallWidth+passageWidth));
                currentLabyrinthSizeZ++;
            }
        }
        addWallBorder(currentLabyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth, wallHeight, wallWidth, xPlacement+(currentLabyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth) / 2, wallHeight / 2, zPlacement+wallWidth / 2);
        addWallBorder(wallWidth, wallHeight, currentLabyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth, xPlacement+wallWidth / 2, wallHeight / 2, zPlacement+(currentLabyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth) / 2);
        addWallBorder(currentLabyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth, wallHeight, wallWidth, xPlacement+(currentLabyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth) / 2, wallHeight / 2, zPlacement+currentLabyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth*0.5f);
        addWallBorder(wallWidth, wallHeight, currentLabyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth, xPlacement+currentLabyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth*0.5f, wallHeight / 2, zPlacement+(currentLabyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth) / 2);
    }*/
}