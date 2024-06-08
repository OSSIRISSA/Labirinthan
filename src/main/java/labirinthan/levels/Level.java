package labirinthan.levels;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import labirinthan.Labirinthan;
import labirinthan.levels.parts.*;
import labirinthan.levels.traps.TrapMaster;
import labirinthan.levels.traps.TrapType;
import labirinthan.props.TorchHolder;
import labirinthan.levels.puzzles.PuzzleCabinet;

import java.util.ArrayList;
import java.util.Random;

public class Level extends AbstractAppState {
    protected final Node rootNode;
    protected final Node localRootNode;

    protected final AssetManager assetManager;
    private final Node guiNode;
    public final Node localPuzzleNode;
    private Node currentNode;
    private final AppSettings settings;
    protected Labirinthan application;
    public static float wallHeight = 6;
    public static float wallWidth = 1;
    public static float passageWidth = 4;
    public Floor floor;
    public Ceiling ceiling;
    public PuzzleCabinet cross;
    public TrapMaster trap;

    protected ArrayList<Wall> walls = new ArrayList<>();
    public ArrayList<ArrayList<Float>> blocksInfo = new ArrayList<>();
    protected ArrayList<Float> blocksDecorationInfo = new ArrayList<>();
    protected Node[][] blocks;

    public ArrayList<TorchHolder>[][] allTorches;

    protected BulletAppState bulletAppState;
    public Random random = new Random();
    public int labyrinthSizeX;
    public int labyrinthSizeZ;

    public int currentLabyrinthSizeX;
    public int currentLabyrinthSizeZ;
    public int clearSpan;
    public int chooseCross;


    private boolean isFirstTorch = true;

    public Level(Labirinthan application, String name, Node guiNode, AppSettings settings) {
        this.rootNode = application.getRootNode();
        this.guiNode = guiNode;
        this.settings = settings;
        this.application = application;
        localRootNode = new Node(name);
        assetManager = application.getAssetManager();
        localPuzzleNode = new Node(name+"Puzzle");
    }

    @Override
    public void initialize(AppStateManager sm, Application application){
        super.initialize(sm,application);
        rootNode.attachChild(localRootNode);
        guiNode.attachChild(localPuzzleNode);
    }

    @Override
    public void cleanup(){
        super.cleanup();
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localPuzzleNode);
    }
    public void addWall(float x, float y, float z, float px, float py, float pz) {
        if(x==wallWidth){
            z-=0.01f;
        }
        if(z==wallWidth){
            x-=0.01f;
        }
        walls.add(new Wall(x, y, z, assetManager, currentNode, px, py, pz, bulletAppState));
    }

    public void addWallBorder(float x, float y, float z, float px, float py, float pz) {
        if(x==wallWidth){
            z-=0.01f;
        }
        if(z==wallWidth){
            x-=0.01f;
        }
        walls.add(new Wall(x, y, z, assetManager, localRootNode, px, py, pz, bulletAppState));
    }

    private void loadTorch(float x, float z, int direction, int roomsNumber, ArrayList<TorchHolder> allTorches) {
        if(random.nextInt(10)!=0){
            TorchHolder torchHolder = new TorchHolder(application, assetManager, rootNode, isFirstTorch, roomsNumber, allTorches);
            isFirstTorch = false;
            currentNode.attachChild(torchHolder);
            switch (direction){
                case 1 -> torchHolder.rotateTorch(0, -FastMath.HALF_PI, 0);
                case 2 -> {
                    //x+=wallWidth/2;
                    torchHolder.rotateTorch(0, FastMath.PI, 0);
                }
                case 3 -> torchHolder.rotateTorch(0, FastMath.HALF_PI, 0);
            }
            torchHolder.moveTorch(x, 2.5f, z);
            torchHolder.updateTorchStatus(true);
            allTorches.add(torchHolder);
        }
    }

    public ArrayList<Float> buildBlock1(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches){

        currentNode = node;
        makeFloor(startX,startZ);
        makeCeiling(startX,startZ);

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

        loadTorch(startX+wallWidth+passageWidth,startZ+wallWidth+passageWidth*0,3, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*3+passageWidth*3,startZ+wallWidth*3+passageWidth*2.5f,4, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*4.5f+passageWidth*4,startZ+wallWidth,3, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*4+passageWidth*4, 1, roomsNumber, allTorches);
        loadTorch(startX+wallWidth+passageWidth,startZ+wallWidth*5+passageWidth*5, 1, roomsNumber, allTorches);

        makeTrap(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*2+passageWidth*1.5f,1);
        makeTrap(startX+wallWidth*3+passageWidth*2.5f,startZ+wallWidth*5+passageWidth*4.5f,1);

        createDecor(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*3+passageWidth*2.5f,3f);
        createDecor(startX+wallWidth*1+passageWidth*0.5f,startZ+wallWidth*4+passageWidth*3.5f,3f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX+wallWidth*3+passageWidth*2.5f);
        res.add(startZ+wallWidth*2+passageWidth*1.5f);

        return res;
    }

    public ArrayList<Float> buildBlock2(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches){
        currentNode = node;
        makeFloor(startX,startZ);
        makeCeiling(startX,startZ);

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

        loadTorch(startX+wallWidth+passageWidth*0.5f,startZ+wallWidth+passageWidth*0,3, roomsNumber, allTorches);
        loadTorch(startX+wallWidth,startZ+wallWidth*4+passageWidth*3.5f,2, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth,3, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*5+passageWidth*4,startZ+wallWidth*2+passageWidth*1.5f,2, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*5+passageWidth*5,1, roomsNumber, allTorches);

        makeTrap(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*2+passageWidth*1.5f,1);
        makeTrap(startX+wallWidth*3+passageWidth*2.5f,startZ+wallWidth*4+passageWidth*3.5f,1);
        makeTrap(startX+wallWidth*5+passageWidth*4.5f,startZ+wallWidth*4+passageWidth*3.5f,1);

        createDecor(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth*1+passageWidth*0.5f,4f);
        createDecor(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth*5+passageWidth*4.5f,2f);
        createDecor(startX+wallWidth*5+passageWidth*4.5f,startZ+wallWidth*1+passageWidth*0.5f,3f);
        createDecor(startX+wallWidth*3+passageWidth*2.5f, startZ+wallWidth*2+passageWidth*1.5f,1f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX+wallWidth*4+passageWidth*3.5f);
        res.add(startZ+wallWidth*4+passageWidth*3.5f);

        return res;
    }

    public ArrayList<Float> buildBlock3(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches){
        currentNode = node;
        makeFloor(startX,startZ);
        makeCeiling(startX,startZ);

        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, startX+wallWidth*1.5f+passageWidth* 1, wallHeight / 2, startZ+wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth*2, startX+wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth*2);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth * 3.5f + passageWidth *3, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth * 4.5f + passageWidth *4, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX+wallWidth * 4.5f + passageWidth *4, wallHeight / 2, startZ+wallWidth * 1 + passageWidth * 0.5f);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 5 + passageWidth * 4.5f, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX+wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);

        loadTorch(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth+passageWidth*0,3, roomsNumber, allTorches);
        loadTorch(startX+wallWidth+passageWidth*0.5f,startZ+wallWidth*2+passageWidth*1,3, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*5+passageWidth*5,startZ+wallWidth*2+passageWidth*0.5f,4, roomsNumber, allTorches);
        loadTorch(startX+wallWidth,startZ+wallWidth*4+passageWidth*3.5f,2, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*5+passageWidth*5,1, roomsNumber, allTorches);

        makeTrap(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*2+passageWidth*1.5f,5);
        makeTrap(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth*5+passageWidth*4.5f, 2);

        createDecor(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*1+passageWidth*0.5f,4f);
        createDecor(startX+wallWidth*3+passageWidth*2.5f,startZ+wallWidth*4+passageWidth*3.5f,4f);
        createDecor(startX+wallWidth*5+passageWidth*4.5f,startZ+wallWidth*4+passageWidth*3.5f,3f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX+wallWidth*5+passageWidth*4.5f);
        res.add(startZ+wallWidth+passageWidth*0.5f);
        return res;
    }

    public ArrayList<Float> buildBlock4(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches){
        currentNode = node;
        makeFloor(startX,startZ);
        makeCeiling(startX,startZ);

        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth *2, startX+wallWidth*1.5f+passageWidth* 1, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth*1, startX+wallWidth * 1.5f + passageWidth * 1, wallHeight / 2, startZ+wallWidth * 4 + passageWidth*3.5f);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth*2, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth*3);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth * 2.5f + passageWidth *2, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 5 + passageWidth * 4.5f, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 5 + passageWidth * 4, wallHeight, wallWidth, startX+wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX+wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);

        loadTorch(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth+passageWidth*0,3, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*5+passageWidth*4.5f,startZ+wallWidth*2+passageWidth*1,3, roomsNumber, allTorches);
        loadTorch(startX+wallWidth+passageWidth*0.5f,startZ+wallWidth*5+passageWidth*5,1, roomsNumber, allTorches);

        makeTrap(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*2+passageWidth*1.5f, 5);
        makeTrap(startX+wallWidth*3+passageWidth*2.5f,startZ+wallWidth*4+passageWidth*3.5f,4);

        createDecor(startX+wallWidth*1+passageWidth*0.5f,startZ+wallWidth*1+passageWidth*0.5f,3f);
        createDecor(startX+wallWidth*3+passageWidth*2.5f,startZ+wallWidth*3+passageWidth*2.5f,2f);
        createDecor(startX+wallWidth*5+passageWidth*4.5f,startZ+wallWidth*4+passageWidth*3.5f,3f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX+wallWidth*4+passageWidth*3.5f);
        res.add(startZ+wallWidth*4+passageWidth*3.5f);
        return res;
    }

    public ArrayList<Float> buildBlock5(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches){
        currentNode = node;
        makeFloor(startX,startZ);
        makeCeiling(startX,startZ);

        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*2.5f+passageWidth* 2, wallHeight / 2, startZ+wallWidth * 1 + passageWidth * 0.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*2.5f+passageWidth* 2, wallHeight / 2, startZ+wallWidth * 4 + passageWidth * 3.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*1.5f+passageWidth* 1, wallHeight / 2, startZ+wallWidth * 5 + passageWidth * 4.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*1.5f+passageWidth* 1, wallHeight / 2, startZ+wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*3.5f+passageWidth* 3, wallHeight / 2, startZ+wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*4.5f+passageWidth* 4, wallHeight / 2, startZ+wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 5 + passageWidth * 4, wallHeight, wallWidth, startX+wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);

        loadTorch(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth*2+passageWidth*2,1, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*3+passageWidth*2,startZ+wallWidth*4+passageWidth*3.5f,2, roomsNumber, allTorches);
        loadTorch(startX+wallWidth+passageWidth*0.5f,startZ+wallWidth*5+passageWidth*5,1, roomsNumber, allTorches);

        makeTrap(startX+wallWidth*3+passageWidth*2.5f,startZ+wallWidth*2+passageWidth*1.5f,5);
        makeTrap(startX+wallWidth*3+passageWidth*2.5f,startZ+wallWidth*5+passageWidth*4.5f,1);

        createDecor(startX+wallWidth*5+passageWidth*4.5f,startZ+wallWidth*1+passageWidth*0.5f,4f);
        createDecor(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*3+passageWidth*2.5f,3f);
        createDecor(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth*5+passageWidth*4.5f,2f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX+wallWidth*5+passageWidth*4.5f);
        res.add(startZ+wallWidth*2+passageWidth*1.5f);
        return res;
    }

    public ArrayList<Float> buildBlock6(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches){
        currentNode = node;
        makeFloor(startX,startZ);
        makeCeiling(startX,startZ);

        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*2.5f+passageWidth* 2, wallHeight / 2, startZ+wallWidth * 1 + passageWidth * 0.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*4.5f+passageWidth* 4, wallHeight / 2, startZ+wallWidth * 1 + passageWidth * 0.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*4.5f+passageWidth* 4, wallHeight / 2, startZ+wallWidth * 5 + passageWidth * 4.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*1.5f+passageWidth* 1, wallHeight / 2, startZ+wallWidth * 5 + passageWidth * 4.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*3.5f+passageWidth* 3, wallHeight / 2, startZ+wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth *2, startX+wallWidth*2.5f+passageWidth* 2, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 5 + passageWidth * 4, wallHeight, wallWidth, startX+wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 4 + passageWidth * 3, wallHeight, wallWidth, startX+wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);

        loadTorch(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth*2+passageWidth*2,1, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*5+passageWidth*4.5f,startZ+wallWidth*2+passageWidth*2,1, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth*5+passageWidth*5,1, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*5+passageWidth*5,startZ+wallWidth*4+passageWidth*3.5f,4, roomsNumber, allTorches);

        makeTrap(startX+wallWidth*3+passageWidth*2.5f,startZ+wallWidth*2+passageWidth*1.5f,5);
        makeTrap(startX+wallWidth*3+passageWidth*2.5f,startZ+wallWidth*1+passageWidth*0.5f,5);
        makeTrap(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*4+passageWidth*3.5f,5);

        createDecor(startX+wallWidth*1+passageWidth*0.5f,startZ+wallWidth*1+passageWidth*0.5f,2f);
        createDecor(startX+wallWidth*5+passageWidth*4.5f,startZ+wallWidth*1+passageWidth*0.5f,3f);
        createDecor(startX+wallWidth*5+passageWidth*4.5f,startZ+wallWidth*5+passageWidth*4.5f,1f);
        createDecor(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth*3+passageWidth*2.5f,4f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX+wallWidth*3+passageWidth*2.5f);
        res.add(startZ+wallWidth*4+passageWidth*3.5f);
        return res;
    }

    public ArrayList<Float> buildBlock7(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches){
        currentNode = node;
        makeFloor(startX,startZ);
        makeCeiling(startX,startZ);

        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*4.5f+passageWidth* 4, wallHeight / 2, startZ+wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*4.5f+passageWidth* 4, wallHeight / 2, startZ+wallWidth * 5 + passageWidth * 4.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*2.5f+passageWidth* 2, wallHeight / 2, startZ+wallWidth * 5 + passageWidth * 4.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*2.5f+passageWidth* 2, wallHeight / 2, startZ+wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*1.5f+passageWidth* 1, wallHeight / 2, startZ+wallWidth * 4 + passageWidth * 3.5f);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth *2, startX+wallWidth*3.5f+passageWidth* 3, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 4 + passageWidth * 3, wallHeight, wallWidth, startX+wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);

        loadTorch(startX+wallWidth*5+passageWidth*5,startZ+wallWidth*1+passageWidth*0.5f,4, roomsNumber, allTorches);
        loadTorch(startX+wallWidth,startZ+wallWidth*4+passageWidth*3.5f,2, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*3+passageWidth*2,startZ+wallWidth*3+passageWidth*2.5f,2, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*5+passageWidth*5,startZ+wallWidth*4+passageWidth*3.5f,4, roomsNumber, allTorches);

        makeTrap(startX+wallWidth*1+passageWidth*0.5f,startZ+wallWidth*2+passageWidth*1.5f,5);
        makeTrap(startX+wallWidth*3+passageWidth*2.5f,startZ+wallWidth*5+passageWidth*4.5f,5);

        createDecor(startX+wallWidth*5+passageWidth*4.5f,startZ+wallWidth*2+passageWidth*1.5f,1f);
        createDecor(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*3+passageWidth*2.5f,2f);
        createDecor(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth*4+passageWidth*3.5f,2f);


        ArrayList<Float> res = new ArrayList<>();
        res.add(startX+wallWidth*4+passageWidth*3.5f);
        res.add(startZ+wallWidth*2+passageWidth*1.5f);
        return res;
    }

    public ArrayList<Float> buildBlock8(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches){
        currentNode = node;
        makeFloor(startX,startZ);
        makeCeiling(startX,startZ);

        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth *2, startX+wallWidth*1.5f+passageWidth* 1, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth *2, startX+wallWidth*2.5f+passageWidth* 2, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*2.5f+passageWidth* 2, wallHeight / 2, startZ+wallWidth * 1 + passageWidth * 0.5f);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth *2, startX+wallWidth*3.5f+passageWidth* 3, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*4.5f+passageWidth* 4, wallHeight / 2, startZ+wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 4 + passageWidth * 3, wallHeight, wallWidth, startX+wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);

        loadTorch(startX+wallWidth+passageWidth*0.5f,startZ+wallWidth,3, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth,3, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*3+passageWidth*2,startZ+wallWidth*4+passageWidth*3.5f,2, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth*5+passageWidth*5,1, roomsNumber, allTorches);

        makeTrap(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth*4+passageWidth*3.5f,1);
        makeTrap(startX+wallWidth*3+passageWidth*2.5f,startZ+wallWidth*2+passageWidth*1.5f,5);

        createDecor(startX+wallWidth*5+passageWidth*4.5f,startZ+wallWidth*5+passageWidth*4.5f,4f);
        createDecor(startX+wallWidth*1+passageWidth*0.5f,startZ+wallWidth*4+passageWidth*3.5f,1f);
        createDecor(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*2+passageWidth*1.5f,1f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX+wallWidth*5+passageWidth*4.5f);
        res.add(startZ+wallWidth*4+passageWidth*3.5f);
        return res;
    }

    public ArrayList<Float> buildBlock9(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches){
        currentNode = node;
        makeFloor(startX,startZ);
        makeCeiling(startX,startZ);

        addWall(wallWidth, wallHeight, wallWidth * 4 + passageWidth *3, startX+wallWidth*1.5f+passageWidth* 1, wallHeight / 2, startZ+wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*2.5f+passageWidth* 2, wallHeight / 2, startZ+wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*2.5f+passageWidth* 2, wallHeight / 2, startZ+wallWidth * 5 + passageWidth * 4.5f);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth *2, startX+wallWidth*4.5f+passageWidth* 4, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 4 + passageWidth * 3, wallHeight, wallWidth, startX+wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);

        loadTorch(startX+wallWidth*1+passageWidth*0.5f,startZ+wallWidth,3, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*4+passageWidth*4,startZ+wallWidth*2+passageWidth*1.5f,4, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*5+passageWidth*4,startZ+wallWidth*3+passageWidth*2.5f,2, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*2+passageWidth*1,startZ+wallWidth*4+passageWidth*3.5f,2, roomsNumber, allTorches);

        makeTrap(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth*3+passageWidth*2.5f,1);
        makeTrap(startX+wallWidth*3+passageWidth*2.5f,startZ+wallWidth*4+passageWidth*3.5f,5);

        createDecor(startX+wallWidth*1+passageWidth*0.5f,startZ+wallWidth*4+passageWidth*3.5f,1f);
        createDecor(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*3+passageWidth*2.5f,4f);
        createDecor(startX+wallWidth*5+passageWidth*4.5f,startZ+wallWidth*2+passageWidth*1.5f,1f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX+wallWidth*1+passageWidth*0.5f);
        res.add(startZ+wallWidth*5+passageWidth*4.5f);
        return res;
    }

    public ArrayList<Float> buildBlock10(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches){
        currentNode = node;
        makeFloor(startX,startZ);
        makeCeiling(startX,startZ);

        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth *2, startX+wallWidth*1.5f+passageWidth* 1, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth, wallHeight, wallWidth * 4 + passageWidth *3, startX+wallWidth*2.5f+passageWidth* 2, wallHeight / 2, startZ+wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*3.5f+passageWidth* 3, wallHeight / 2, startZ+wallWidth * 5 + passageWidth * 4.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*3.5f+passageWidth* 3, wallHeight / 2, startZ+wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth *1, startX+wallWidth*4.5f+passageWidth* 4, wallHeight / 2, startZ+wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX+wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 5 + passageWidth * 4.5f, wallHeight / 2, startZ+wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX+wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ+wallWidth * 2.5f + passageWidth * 2);

        loadTorch(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth,3, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*1+passageWidth*0.5f,startZ+wallWidth*5+passageWidth*5,1, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*3+passageWidth*3,1, roomsNumber, allTorches);
        loadTorch(startX+wallWidth*5+passageWidth*5,startZ+wallWidth*4+passageWidth*3.5f,4, roomsNumber, allTorches);

        makeTrap(startX+wallWidth*2+passageWidth*1.5f,startZ+wallWidth*3+passageWidth*2.5f,5);
        makeTrap(startX+wallWidth*4+passageWidth*3.5f,startZ+wallWidth*4+passageWidth*3.5f,4);

        createDecor(startX+wallWidth*5+passageWidth*4.5f,startZ+wallWidth*1+passageWidth*0.5f,4f);
        createDecor(startX+wallWidth*3+passageWidth*2.5f,startZ+wallWidth*2+passageWidth*1.5f,3f);
        createDecor(startX+wallWidth*5+passageWidth*4.5f,startZ+wallWidth*2+passageWidth*1.5f,3f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX+wallWidth*4+passageWidth*3.5f);
        res.add(startZ+wallWidth*5+passageWidth*4.5f);
        return res;
    }
    public void makeFrameWalls(float startX, float startZ){
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX+(wallWidth * 3 + passageWidth * 2) / 2, wallHeight / 2, startZ+wallWidth / 2);
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth / 2);
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX+(wallWidth * 3 + passageWidth * 2) / 2, wallHeight / 2, startZ+wallWidth * 5.5f + passageWidth * 5);
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth * 5.5f + passageWidth * 5);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth / 2, wallHeight / 2, startZ+(wallWidth * 3 + passageWidth * 2) / 2);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth * 5.5f + passageWidth * 5, wallHeight / 2, startZ+(wallWidth * 3 + passageWidth * 2) / 2);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth * 5.5f + passageWidth * 5, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth / 2, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
    }

    public void makeZWall(float startX, float startZ){
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX+(wallWidth * 3 + passageWidth * 2) / 2, wallHeight / 2, startZ+wallWidth / 2);
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX+wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ+wallWidth / 2);
    }

    public void makeXWall(float startX, float startZ){
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth / 2, wallHeight / 2, startZ+(wallWidth * 3 + passageWidth * 2) / 2);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX+wallWidth / 2, wallHeight / 2, startZ+wallWidth * 4.5f + passageWidth * 4);
    }

    public void makeFloor(float startX, float startZ){
        floor = new Floor((wallWidth * 5 + passageWidth * 5)/2, 0.1f, (wallWidth * 5 + passageWidth * 5)/2, assetManager, currentNode, startX+((wallWidth * 5 + passageWidth * 5)*0.25f), -0.05f, startZ+((wallWidth * 5 + passageWidth * 5)*0.25f), bulletAppState);
        floor = new Floor((wallWidth * 5 + passageWidth * 5)/2, 0.1f, (wallWidth * 5 + passageWidth * 5)/2, assetManager, currentNode, startX+((wallWidth * 5 + passageWidth * 5)*0.75f), -0.05f, startZ+((wallWidth * 5 + passageWidth * 5)*0.75f), bulletAppState);
        floor = new Floor((wallWidth * 5 + passageWidth * 5)/2, 0.1f, (wallWidth * 5 + passageWidth * 5)/2, assetManager, currentNode, startX+((wallWidth * 5 + passageWidth * 5)*0.75f), -0.05f, startZ+((wallWidth * 5 + passageWidth * 5)*0.25f), bulletAppState);
        floor = new Floor((wallWidth * 5 + passageWidth * 5)/2, 0.1f, (wallWidth * 5 + passageWidth * 5)/2, assetManager, currentNode, startX+((wallWidth * 5 + passageWidth * 5)*0.25f), -0.05f, startZ+((wallWidth * 5 + passageWidth * 5)*0.75f), bulletAppState);
    }

    public void makeCeiling(float startX, float startZ){
        ceiling = new Ceiling((wallWidth * 5 + passageWidth * 5)/2, 0.1f, (wallWidth * 5 + passageWidth * 5)/2, assetManager, currentNode, startX+((wallWidth * 5 + passageWidth * 5)*0.25f), wallHeight-0.05f, startZ+((wallWidth * 5 + passageWidth * 5)*0.75f), bulletAppState);
        ceiling = new Ceiling((wallWidth * 5 + passageWidth * 5)/2, 0.1f, (wallWidth * 5 + passageWidth * 5)/2, assetManager, currentNode, startX+((wallWidth * 5 + passageWidth * 5)*0.75f), wallHeight-0.05f, startZ+((wallWidth * 5 + passageWidth * 5)*0.75f), bulletAppState);
        ceiling = new Ceiling((wallWidth * 5 + passageWidth * 5)/2, 0.1f, (wallWidth * 5 + passageWidth * 5)/2, assetManager, currentNode, startX+((wallWidth * 5 + passageWidth * 5)*0.25f), wallHeight-0.05f, startZ+((wallWidth * 5 + passageWidth * 5)*0.25f), bulletAppState);
        ceiling = new Ceiling((wallWidth * 5 + passageWidth * 5)/2, 0.1f, (wallWidth * 5 + passageWidth * 5)/2, assetManager, currentNode, startX+((wallWidth * 5 + passageWidth * 5)*0.75f), wallHeight-0.05f, startZ+((wallWidth * 5 + passageWidth * 5)*0.25f), bulletAppState);
    }

    public void makeTrap(float x, float z, int direction){
        trap = new TrapMaster(application, assetManager, currentNode, x, -0.05f, z, direction);
    }

    public void createDecor(float x, float z, float direction){
        Decoration decoration = new Decoration(direction,assetManager, currentNode, x, -0.05f, z, bulletAppState);
    }

    public void updateBlocks(float x, float z) {
        int xPlacement = (int) (x/(wallWidth * 5 + passageWidth * 5));
        int zPlacement = (int) (z/(wallWidth * 5 + passageWidth * 5));
    }
}