package labirinthan.levels;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import labirinthan.Labirinthan;
import labirinthan.levels.parts.Cross;
import labirinthan.puzzles.PuzzleCabinet;
import labirinthan.puzzles.PuzzleType;

public class Level1 extends Level {


    public Level1(Labirinthan application, BulletAppState bulletAppState, Node guiNode, AppSettings settings) {
        super(application, "Level1", guiNode, settings);
        this.bulletAppState = bulletAppState;
        this.blocks = new Node[3][3];
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

                blocks[i][j]= new Node();
                int blockNumber = random.nextInt(1,11);
                switch (blockNumber){
                    case 1 -> blocksInfo.add(buildBlock1(currentX,currentZ,blocks[i][j]));
                    case 2 -> blocksInfo.add(buildBlock2(currentX,currentZ,blocks[i][j]));
                    case 3 -> blocksInfo.add(buildBlock3(currentX,currentZ,blocks[i][j]));
                    case 4 -> blocksInfo.add(buildBlock4(currentX,currentZ,blocks[i][j]));
                    case 5 -> blocksInfo.add(buildBlock5(currentX,currentZ,blocks[i][j]));
                    case 6 -> blocksInfo.add(buildBlock6(currentX,currentZ,blocks[i][j]));
                    case 7 -> blocksInfo.add(buildBlock7(currentX,currentZ,blocks[i][j]));
                    case 8 -> blocksInfo.add(buildBlock8(currentX,currentZ,blocks[i][j]));
                    case 9 -> blocksInfo.add(buildBlock9(currentX,currentZ,blocks[i][j]));
                    case 10 -> blocksInfo.add(buildBlock10(currentX,currentZ,blocks[i][j]));
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
        cross = new PuzzleCabinet((Labirinthan) application, assetManager, localRootNode, blocksInfo.get(chooseCross).get(0), -0.05f, blocksInfo.get(chooseCross).get(1), PuzzleType.PYRAMID);

        //for(int i=0;i<blocksDecorationInfo.size()-1;i+=3){
        //}

        //Closing extras
        super.addWall(labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth, wallHeight, wallWidth, (labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth) / 2, wallHeight / 2, wallWidth / 2);
        super.addWall(wallWidth, wallHeight, labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth, wallWidth / 2, wallHeight / 2, (labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth) / 2);
        super.addWall(labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth, wallHeight, wallWidth, (labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth) / 2, wallHeight / 2, labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth*0.5f);
        super.addWall(wallWidth, wallHeight, labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth, labyrinthSizeX*5*(wallWidth+passageWidth)+wallWidth*0.5f, wallHeight / 2, (labyrinthSizeZ*5*(wallWidth+passageWidth)+wallWidth) / 2);


        this.application.getViewPort().addProcessor(this.application.filterPostProcessor);
    }
}