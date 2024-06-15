/**
 * Task: Game
 * File: Level.java
 *
 *  @author Iryna Hryshchenko
 */
package labirinthan.levels;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import labirinthan.Labirinthan;
import labirinthan.levels.parts.Ceiling;
import labirinthan.levels.parts.Decoration;
import labirinthan.levels.parts.Floor;
import labirinthan.levels.parts.Wall;
import labirinthan.levels.puzzles.PuzzleCabinet;
import labirinthan.levels.traps.TrapMaster;
import labirinthan.props.TorchHolder;

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
    public Decoration decoration;

    protected ArrayList<Wall> walls = new ArrayList<>();
    public ArrayList<ArrayList<Float>> blocksInfo = new ArrayList<>();
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

    public static AudioNode creepySound;
    public static AudioNode trapSound;

    private boolean isFirstTorch = true;

    /**
     * Level constructor
     * @param application - Labirinthan app
     * @param name - level name
     * @param guiNode - guiNode
     * @param settings - settings
     */
    public Level(Labirinthan application, String name, Node guiNode, AppSettings settings) {
        this.rootNode = application.getRootNode();
        this.guiNode = guiNode;
        this.settings = settings;
        this.application = application;
        localRootNode = new Node(name);
        assetManager = application.getAssetManager();
        localPuzzleNode = new Node(name + "Puzzle");

        AudioNode music = new AudioNode(assetManager, "Sounds/level-music.wav", AudioData.DataType.Buffer);
        music.setPositional(false); // Use true for 3D sounds
        music.setLooping(true); // Set to true if you want the sound to loop
        music.setVolume(2*4*settings.getFloat("Master Volume")*settings.getFloat("Music Volume")); // Set the volume (1 is default, 0 is silent)
        localRootNode.attachChild(music);
        music.play();
    }

    @Override
    public void initialize(AppStateManager sm, Application application) {
        super.initialize(sm, application);
        rootNode.attachChild(localRootNode);
        guiNode.attachChild(localPuzzleNode);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localPuzzleNode);
    }

    /**
     * Adding Wall
     * @param x - size x
     * @param y - size y
     * @param z - size z
     * @param px - x
     * @param py - y
     * @param pz - z
     */
    public void addWall(float x, float y, float z, float px, float py, float pz) {
        if (x == wallWidth) {
            z -= 0.01f;
        }
        if (z == wallWidth) {
            x -= 0.01f;
        }
        walls.add(new Wall(x, y, z, assetManager, currentNode, px, py, pz, bulletAppState));
    }

    /**
     * Loading torch
     * @param x - x
     * @param z - z
     * @param direction - facing direction
     * @param roomsNumber - number of rooms
     * @param allTorches - array of all torches
     */
    private void loadTorch(float x, float z, int direction, int roomsNumber, ArrayList<TorchHolder> allTorches) {
        if (random.nextInt(10) != 0) {
            TorchHolder torchHolder = new TorchHolder(application, assetManager, rootNode, isFirstTorch, roomsNumber, allTorches);
            isFirstTorch = false;
            currentNode.attachChild(torchHolder);
            switch (direction) {
                case 1 -> torchHolder.rotateTorch(0, -FastMath.HALF_PI, 0);
                case 2 -> torchHolder.rotateTorch(0, FastMath.PI, 0);
                case 3 -> torchHolder.rotateTorch(0, FastMath.HALF_PI, 0);
            }
            torchHolder.moveTorch(x, 2.5f, z);
            torchHolder.updateTorchStatus(true);
            allTorches.add(torchHolder);
        }
    }

    /**
     * Creating Block1
     * @param startX - startX
     * @param startZ - startZ
     * @param node - node
     * @param roomsNumber - number of rooms
     * @param allTorches - array of all torches
     * @return - possible cross' coords
     */
    public ArrayList<Float> buildBlock1(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches) {

        currentNode = node;
        makeFloor(startX, startZ);
        makeCeiling(startX, startZ);

        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + (wallWidth * 2 + passageWidth) / 2);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, startX + wallWidth * 1.5f + passageWidth, wallHeight / 2, startZ + wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 4 + passageWidth * 3, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, startX + wallWidth * 1.5f + passageWidth, wallHeight / 2, startZ + wallWidth * 4 + passageWidth * 3.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 4 + passageWidth * 3, wallHeight, wallWidth, startX + wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);

        loadTorch(startX + wallWidth + passageWidth, startZ + wallWidth + passageWidth * 0, 3, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 3 + passageWidth * 3, startZ + wallWidth * 3 + passageWidth * 2.5f, 4, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 4.5f + passageWidth * 4, startZ + wallWidth, 3, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 4 + passageWidth * 4, 1, roomsNumber, allTorches);
        loadTorch(startX + wallWidth + passageWidth, startZ + wallWidth * 5 + passageWidth * 5, 1, roomsNumber, allTorches);

        makeTrap(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 2 + passageWidth * 1.5f, 1);
        makeTrap(startX + wallWidth * 3 + passageWidth * 2.5f, startZ + wallWidth * 5 + passageWidth * 4.5f, 1);

        createDecor(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 3 + passageWidth * 2.5f, 3f);
        createDecor(startX + wallWidth * 1 + passageWidth * 0.5f, startZ + wallWidth * 4 + passageWidth * 3.5f, 3f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX + wallWidth * 3 + passageWidth * 2.5f);
        res.add(startZ + wallWidth * 2 + passageWidth * 1.5f);
        res.add(2f);

        return res;
    }

    /**
     * Creating Block2
     * @param startX - startX
     * @param startZ - startZ
     * @param node - node
     * @param roomsNumber - number of rooms
     * @param allTorches - array of all torches
     * @return - possible cross' coords
     */
    public ArrayList<Float> buildBlock2(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches) {
        currentNode = node;
        makeFloor(startX, startZ);
        makeCeiling(startX, startZ);

        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 4 + passageWidth * 3, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 1.5f + passageWidth * 1, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + wallWidth * 4 + passageWidth * 3.5f);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);

        loadTorch(startX + wallWidth + passageWidth * 0.5f, startZ + wallWidth + passageWidth * 0, 3, roomsNumber, allTorches);
        loadTorch(startX + wallWidth, startZ + wallWidth * 4 + passageWidth * 3.5f, 2, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth, 3, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 5 + passageWidth * 4, startZ + wallWidth * 2 + passageWidth * 1.5f, 2, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 5 + passageWidth * 5, 1, roomsNumber, allTorches);

        makeTrap(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 2 + passageWidth * 1.5f, 1);
        makeTrap(startX + wallWidth * 3 + passageWidth * 2.5f, startZ + wallWidth * 4 + passageWidth * 3.5f, 1);
        makeTrap(startX + wallWidth * 5 + passageWidth * 4.5f, startZ + wallWidth * 4 + passageWidth * 3.5f, 1);

        createDecor(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth * 1 + passageWidth * 0.5f, 4f);
        createDecor(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth * 5 + passageWidth * 4.5f, 2f);
        createDecor(startX + wallWidth * 5 + passageWidth * 4.5f, startZ + wallWidth * 1 + passageWidth * 0.5f, 3f);
        createDecor(startX + wallWidth * 3 + passageWidth * 2.5f, startZ + wallWidth * 2 + passageWidth * 1.5f, 1f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX + wallWidth * 4 + passageWidth * 3.5f);
        res.add(startZ + wallWidth * 4 + passageWidth * 3.5f);
        res.add(2f);

        return res;
    }

    /**
     * Creating Block3
     * @param startX - startX
     * @param startZ - startZ
     * @param node - node
     * @param roomsNumber - number of rooms
     * @param allTorches - array of all torches
     * @return - possible cross' coords
     */
    public ArrayList<Float> buildBlock3(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches) {
        currentNode = node;
        makeFloor(startX, startZ);
        makeCeiling(startX, startZ);

        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth, startX + wallWidth * 1.5f + passageWidth * 1, wallHeight / 2, startZ + wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 1 + passageWidth * 0.5f);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 5 + passageWidth * 4.5f, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);

        loadTorch(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth + passageWidth * 0, 3, roomsNumber, allTorches);
        loadTorch(startX + wallWidth + passageWidth * 0.5f, startZ + wallWidth * 2 + passageWidth * 1, 3, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 5 + passageWidth * 5, startZ + wallWidth * 2 + passageWidth * 0.5f, 4, roomsNumber, allTorches);
        loadTorch(startX + wallWidth, startZ + wallWidth * 4 + passageWidth * 3.5f, 2, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 5 + passageWidth * 5, 1, roomsNumber, allTorches);

        makeTrap(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 2 + passageWidth * 1.5f, 5);
        makeTrap(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth * 5 + passageWidth * 4.5f, 2);

        createDecor(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 1 + passageWidth * 0.5f, 4f);
        createDecor(startX + wallWidth * 3 + passageWidth * 2.5f, startZ + wallWidth * 4 + passageWidth * 3.5f, 4f);
        createDecor(startX + wallWidth * 5 + passageWidth * 4.5f, startZ + wallWidth * 4 + passageWidth * 3.5f, 3f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX + wallWidth * 5 + passageWidth * 4.5f);
        res.add(startZ + wallWidth + passageWidth * 0.5f);
        res.add(3f);
        return res;
    }

    /**
     * Creating Block4
     * @param startX - startX
     * @param startZ - startZ
     * @param node - node
     * @param roomsNumber - number of rooms
     * @param allTorches - array of all torches
     * @return - possible cross' coords
     */
    public ArrayList<Float> buildBlock4(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches) {
        currentNode = node;
        makeFloor(startX, startZ);
        makeCeiling(startX, startZ);

        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 1.5f + passageWidth * 1, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 1.5f + passageWidth * 1, wallHeight / 2, startZ + wallWidth * 4 + passageWidth * 3.5f);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 5 + passageWidth * 4.5f, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 5 + passageWidth * 4, wallHeight, wallWidth, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);

        loadTorch(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth + passageWidth * 0, 3, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 5 + passageWidth * 4.5f, startZ + wallWidth * 2 + passageWidth * 1, 3, roomsNumber, allTorches);
        loadTorch(startX + wallWidth + passageWidth * 0.5f, startZ + wallWidth * 5 + passageWidth * 5, 1, roomsNumber, allTorches);

        makeTrap(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 2 + passageWidth * 1.5f, 5);
        makeTrap(startX + wallWidth * 3 + passageWidth * 2.5f, startZ + wallWidth * 4 + passageWidth * 3.5f, 4);

        createDecor(startX + wallWidth * 1 + passageWidth * 0.5f, startZ + wallWidth * 1 + passageWidth * 0.5f, 3f);
        createDecor(startX + wallWidth * 3 + passageWidth * 2.5f, startZ + wallWidth * 3 + passageWidth * 2.5f, 2f);
        createDecor(startX + wallWidth * 5 + passageWidth * 4.5f, startZ + wallWidth * 4 + passageWidth * 3.5f, 3f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX + wallWidth * 4 + passageWidth * 3.5f);
        res.add(startZ + wallWidth * 4 + passageWidth * 3.5f);
        res.add(4f);
        return res;
    }

    /**
     * Creating Block5
     * @param startX - startX
     * @param startZ - startZ
     * @param node - node
     * @param roomsNumber - number of rooms
     * @param allTorches - array of all torches
     * @return - possible cross' coords
     */
    public ArrayList<Float> buildBlock5(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches) {
        currentNode = node;
        makeFloor(startX, startZ);
        makeCeiling(startX, startZ);

        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 1 + passageWidth * 0.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 4 + passageWidth * 3.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 1.5f + passageWidth * 1, wallHeight / 2, startZ + wallWidth * 5 + passageWidth * 4.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 1.5f + passageWidth * 1, wallHeight / 2, startZ + wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 5 + passageWidth * 4, wallHeight, wallWidth, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);

        loadTorch(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth * 2 + passageWidth * 2, 1, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 3 + passageWidth * 2, startZ + wallWidth * 4 + passageWidth * 3.5f, 2, roomsNumber, allTorches);
        loadTorch(startX + wallWidth + passageWidth * 0.5f, startZ + wallWidth * 5 + passageWidth * 5, 1, roomsNumber, allTorches);

        makeTrap(startX + wallWidth * 3 + passageWidth * 2.5f, startZ + wallWidth * 2 + passageWidth * 1.5f, 5);
        makeTrap(startX + wallWidth * 3 + passageWidth * 2.5f, startZ + wallWidth * 5 + passageWidth * 4.5f, 1);

        createDecor(startX + wallWidth * 5 + passageWidth * 4.5f, startZ + wallWidth * 1 + passageWidth * 0.5f, 4f);
        createDecor(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 3 + passageWidth * 2.5f, 3f);
        createDecor(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth * 5 + passageWidth * 4.5f, 2f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX + wallWidth * 5 + passageWidth * 4.5f);
        res.add(startZ + wallWidth * 2 + passageWidth * 1.5f);
        res.add(4f);
        return res;
    }

    /**
     * Creating Block6
     * @param startX - startX
     * @param startZ - startZ
     * @param node - node
     * @param roomsNumber - number of rooms
     * @param allTorches - array of all torches
     * @return - possible cross' coords
     */
    public ArrayList<Float> buildBlock6(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches) {
        currentNode = node;
        makeFloor(startX, startZ);
        makeCeiling(startX, startZ);

        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 1 + passageWidth * 0.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 1 + passageWidth * 0.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 5 + passageWidth * 4.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 1.5f + passageWidth * 1, wallHeight / 2, startZ + wallWidth * 5 + passageWidth * 4.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 5 + passageWidth * 4, wallHeight, wallWidth, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 4 + passageWidth * 3, wallHeight, wallWidth, startX + wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);

        loadTorch(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth * 2 + passageWidth * 2, 1, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 5 + passageWidth * 4.5f, startZ + wallWidth * 2 + passageWidth * 2, 1, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth * 5 + passageWidth * 5, 1, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 5 + passageWidth * 5, startZ + wallWidth * 4 + passageWidth * 3.5f, 4, roomsNumber, allTorches);

        makeTrap(startX + wallWidth * 3 + passageWidth * 2.5f, startZ + wallWidth * 2 + passageWidth * 1.5f, 5);
        makeTrap(startX + wallWidth * 3 + passageWidth * 2.5f, startZ + wallWidth * 1 + passageWidth * 0.5f, 5);
        makeTrap(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 4 + passageWidth * 3.5f, 5);

        createDecor(startX + wallWidth * 1 + passageWidth * 0.5f, startZ + wallWidth * 1 + passageWidth * 0.5f, 2f);
        createDecor(startX + wallWidth * 5 + passageWidth * 4.5f, startZ + wallWidth * 1 + passageWidth * 0.5f, 3f);
        createDecor(startX + wallWidth * 5 + passageWidth * 4.5f, startZ + wallWidth * 5 + passageWidth * 4.5f, 1f);
        createDecor(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth * 3 + passageWidth * 2.5f, 4f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX + wallWidth * 3 + passageWidth * 2.5f);
        res.add(startZ + wallWidth * 4 + passageWidth * 3.5f);
        res.add(2f);
        return res;
    }

    /**
     * Creating Block7
     * @param startX - startX
     * @param startZ - startZ
     * @param node - node
     * @param roomsNumber - number of rooms
     * @param allTorches - array of all torches
     * @return - possible cross' coords
     */
    public ArrayList<Float> buildBlock7(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches) {
        currentNode = node;
        makeFloor(startX, startZ);
        makeCeiling(startX, startZ);

        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 5 + passageWidth * 4.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 5 + passageWidth * 4.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 1.5f + passageWidth * 1, wallHeight / 2, startZ + wallWidth * 4 + passageWidth * 3.5f);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 4 + passageWidth * 3, wallHeight, wallWidth, startX + wallWidth * 3 + passageWidth * 2.5f, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);

        loadTorch(startX + wallWidth * 5 + passageWidth * 5, startZ + wallWidth * 1 + passageWidth * 0.5f, 4, roomsNumber, allTorches);
        loadTorch(startX + wallWidth, startZ + wallWidth * 4 + passageWidth * 3.5f, 2, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 3 + passageWidth * 2, startZ + wallWidth * 3 + passageWidth * 2.5f, 2, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 5 + passageWidth * 5, startZ + wallWidth * 4 + passageWidth * 3.5f, 4, roomsNumber, allTorches);

        makeTrap(startX + wallWidth * 1 + passageWidth * 0.5f, startZ + wallWidth * 2 + passageWidth * 1.5f, 5);
        makeTrap(startX + wallWidth * 3 + passageWidth * 2.5f, startZ + wallWidth * 5 + passageWidth * 4.5f, 5);

        createDecor(startX + wallWidth * 5 + passageWidth * 4.5f, startZ + wallWidth * 2 + passageWidth * 1.5f, 1f);
        createDecor(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 3 + passageWidth * 2.5f, 2f);
        createDecor(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth * 4 + passageWidth * 3.5f, 2f);


        ArrayList<Float> res = new ArrayList<>();
        res.add(startX + wallWidth * 4 + passageWidth * 3.5f);
        res.add(startZ + wallWidth * 2 + passageWidth * 1.5f);
        res.add(4f);
        return res;
    }

    /**
     * Creating Block8
     * @param startX - startX
     * @param startZ - startZ
     * @param node - node
     * @param roomsNumber - number of rooms
     * @param allTorches - array of all torches
     * @return - possible cross' coords
     */
    public ArrayList<Float> buildBlock8(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches) {
        currentNode = node;
        makeFloor(startX, startZ);
        makeCeiling(startX, startZ);

        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 1.5f + passageWidth * 1, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 1 + passageWidth * 0.5f);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 2 + passageWidth * 1.5f, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 4 + passageWidth * 3, wallHeight, wallWidth, startX + wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);

        loadTorch(startX + wallWidth + passageWidth * 0.5f, startZ + wallWidth, 3, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth, 3, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 3 + passageWidth * 2, startZ + wallWidth * 4 + passageWidth * 3.5f, 2, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth * 5 + passageWidth * 5, 1, roomsNumber, allTorches);

        makeTrap(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth * 4 + passageWidth * 3.5f, 1);
        makeTrap(startX + wallWidth * 3 + passageWidth * 2.5f, startZ + wallWidth * 2 + passageWidth * 1.5f, 5);

        createDecor(startX + wallWidth * 5 + passageWidth * 4.5f, startZ + wallWidth * 5 + passageWidth * 4.5f, 4f);
        createDecor(startX + wallWidth * 1 + passageWidth * 0.5f, startZ + wallWidth * 4 + passageWidth * 3.5f, 1f);
        createDecor(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 2 + passageWidth * 1.5f, 1f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX + wallWidth * 5 + passageWidth * 4.5f);
        res.add(startZ + wallWidth * 4 + passageWidth * 3.5f);
        res.add(4f);
        return res;
    }

    /**
     * Creating Block9
     * @param startX - startX
     * @param startZ - startZ
     * @param node - node
     * @param roomsNumber - number of rooms
     * @param allTorches - array of all torches
     * @return - possible cross' coords
     */
    public ArrayList<Float> buildBlock9(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches) {
        currentNode = node;
        makeFloor(startX, startZ);
        makeCeiling(startX, startZ);

        addWall(wallWidth, wallHeight, wallWidth * 4 + passageWidth * 3, startX + wallWidth * 1.5f + passageWidth * 1, wallHeight / 2, startZ + wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 5 + passageWidth * 4.5f);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 4 + passageWidth * 3, wallHeight, wallWidth, startX + wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);

        loadTorch(startX + wallWidth * 1 + passageWidth * 0.5f, startZ + wallWidth, 3, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 4 + passageWidth * 4, startZ + wallWidth * 2 + passageWidth * 1.5f, 4, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 5 + passageWidth * 4, startZ + wallWidth * 3 + passageWidth * 2.5f, 2, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 2 + passageWidth * 1, startZ + wallWidth * 4 + passageWidth * 3.5f, 2, roomsNumber, allTorches);

        makeTrap(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth * 3 + passageWidth * 2.5f, 1);
        makeTrap(startX + wallWidth * 3 + passageWidth * 2.5f, startZ + wallWidth * 4 + passageWidth * 3.5f, 5);

        createDecor(startX + wallWidth * 1 + passageWidth * 0.5f, startZ + wallWidth * 4 + passageWidth * 3.5f, 1f);
        createDecor(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 3 + passageWidth * 2.5f, 4f);
        createDecor(startX + wallWidth * 5 + passageWidth * 4.5f, startZ + wallWidth * 2 + passageWidth * 1.5f, 1f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX + wallWidth * 1 + passageWidth * 0.5f);
        res.add(startZ + wallWidth * 5 + passageWidth * 4.5f);
        res.add(2f);
        return res;
    }

    /**
     * Creating Block10
     * @param startX - startX
     * @param startZ - startZ
     * @param node - node
     * @param roomsNumber - number of rooms
     * @param allTorches - array of all torches
     * @return - possible cross' coords
     */
    public ArrayList<Float> buildBlock10(float startX, float startZ, Node node, int roomsNumber, ArrayList<TorchHolder> allTorches) {
        currentNode = node;
        makeFloor(startX, startZ);
        makeCeiling(startX, startZ);

        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth * 1.5f + passageWidth * 1, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth, wallHeight, wallWidth * 4 + passageWidth * 3, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 3 + passageWidth * 2.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + wallWidth * 5 + passageWidth * 4.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 3.5f + passageWidth * 3, wallHeight / 2, startZ + wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth, wallHeight, wallWidth * 2 + passageWidth * 1, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 2 + passageWidth * 1.5f);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX + wallWidth * 2.5f + passageWidth * 2, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 5 + passageWidth * 4.5f, wallHeight / 2, startZ + wallWidth * 1.5f + passageWidth * 1);
        addWall(wallWidth * 3 + passageWidth * 2, wallHeight, wallWidth, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth * 3.5f + passageWidth * 3);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 4 + passageWidth * 3.5f, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);
        addWall(wallWidth * 2 + passageWidth * 1, wallHeight, wallWidth, startX + wallWidth * 1 + passageWidth * 0.5f, wallHeight / 2, startZ + wallWidth * 2.5f + passageWidth * 2);

        loadTorch(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth, 3, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 1 + passageWidth * 0.5f, startZ + wallWidth * 5 + passageWidth * 5, 1, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 3 + passageWidth * 3, 1, roomsNumber, allTorches);
        loadTorch(startX + wallWidth * 5 + passageWidth * 5, startZ + wallWidth * 4 + passageWidth * 3.5f, 4, roomsNumber, allTorches);

        makeTrap(startX + wallWidth * 2 + passageWidth * 1.5f, startZ + wallWidth * 3 + passageWidth * 2.5f, 5);
        makeTrap(startX + wallWidth * 4 + passageWidth * 3.5f, startZ + wallWidth * 4 + passageWidth * 3.5f, 4);

        createDecor(startX + wallWidth * 5 + passageWidth * 4.5f, startZ + wallWidth * 1 + passageWidth * 0.5f, 4f);
        createDecor(startX + wallWidth * 3 + passageWidth * 2.5f, startZ + wallWidth * 2 + passageWidth * 1.5f, 3f);
        createDecor(startX + wallWidth * 5 + passageWidth * 4.5f, startZ + wallWidth * 2 + passageWidth * 1.5f, 3f);

        ArrayList<Float> res = new ArrayList<>();
        res.add(startX + wallWidth * 4 + passageWidth * 3.5f);
        res.add(startZ + wallWidth * 5 + passageWidth * 4.5f);
        res.add(2f);
        return res;
    }


    /**
     * Making long Z-Wall
     * @param startX - startX
     * @param startZ - startZ
     */
    public void makeZWall(float startX, float startZ) {
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX + (wallWidth * 3 + passageWidth * 2) / 2, wallHeight / 2, startZ + wallWidth / 2);
        addWall((wallWidth * 3 + passageWidth * 2), wallHeight, wallWidth, startX + wallWidth * 4.5f + passageWidth * 4, wallHeight / 2, startZ + wallWidth / 2);
    }

    /**
     * Making long X-Wall
     * @param startX - startX
     * @param startZ - startZ
     */
    public void makeXWall(float startX, float startZ) {
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth / 2, wallHeight / 2, startZ + (wallWidth * 3 + passageWidth * 2) / 2);
        addWall(wallWidth, wallHeight, wallWidth * 3 + passageWidth * 2, startX + wallWidth / 2, wallHeight / 2, startZ + wallWidth * 4.5f + passageWidth * 4);
    }

    /**
     * Making floor
     * @param startX - startX
     * @param startZ - startZ
     */
    public void makeFloor(float startX, float startZ) {
        floor = new Floor((wallWidth * 5 + passageWidth * 5) / 2, 0.1f, (wallWidth * 5 + passageWidth * 5) / 2, assetManager, currentNode, startX + ((wallWidth * 5 + passageWidth * 5) * 0.25f), -0.05f, startZ + ((wallWidth * 5 + passageWidth * 5) * 0.25f), bulletAppState);
        floor = new Floor((wallWidth * 5 + passageWidth * 5) / 2, 0.1f, (wallWidth * 5 + passageWidth * 5) / 2, assetManager, currentNode, startX + ((wallWidth * 5 + passageWidth * 5) * 0.75f), -0.05f, startZ + ((wallWidth * 5 + passageWidth * 5) * 0.75f), bulletAppState);
        floor = new Floor((wallWidth * 5 + passageWidth * 5) / 2, 0.1f, (wallWidth * 5 + passageWidth * 5) / 2, assetManager, currentNode, startX + ((wallWidth * 5 + passageWidth * 5) * 0.75f), -0.05f, startZ + ((wallWidth * 5 + passageWidth * 5) * 0.25f), bulletAppState);
        floor = new Floor((wallWidth * 5 + passageWidth * 5) / 2, 0.1f, (wallWidth * 5 + passageWidth * 5) / 2, assetManager, currentNode, startX + ((wallWidth * 5 + passageWidth * 5) * 0.25f), -0.05f, startZ + ((wallWidth * 5 + passageWidth * 5) * 0.75f), bulletAppState);
    }

    /**
     * Making ceiling
     * @param startX - startX
     * @param startZ - startZ
     */
    public void makeCeiling(float startX, float startZ) {
        ceiling = new Ceiling((wallWidth * 5 + passageWidth * 5) / 2, 0.1f, (wallWidth * 5 + passageWidth * 5) / 2, assetManager, currentNode, startX + ((wallWidth * 5 + passageWidth * 5) * 0.25f), wallHeight - 0.05f, startZ + ((wallWidth * 5 + passageWidth * 5) * 0.75f), bulletAppState);
        ceiling = new Ceiling((wallWidth * 5 + passageWidth * 5) / 2, 0.1f, (wallWidth * 5 + passageWidth * 5) / 2, assetManager, currentNode, startX + ((wallWidth * 5 + passageWidth * 5) * 0.75f), wallHeight - 0.05f, startZ + ((wallWidth * 5 + passageWidth * 5) * 0.75f), bulletAppState);
        ceiling = new Ceiling((wallWidth * 5 + passageWidth * 5) / 2, 0.1f, (wallWidth * 5 + passageWidth * 5) / 2, assetManager, currentNode, startX + ((wallWidth * 5 + passageWidth * 5) * 0.25f), wallHeight - 0.05f, startZ + ((wallWidth * 5 + passageWidth * 5) * 0.25f), bulletAppState);
        ceiling = new Ceiling((wallWidth * 5 + passageWidth * 5) / 2, 0.1f, (wallWidth * 5 + passageWidth * 5) / 2, assetManager, currentNode, startX + ((wallWidth * 5 + passageWidth * 5) * 0.75f), wallHeight - 0.05f, startZ + ((wallWidth * 5 + passageWidth * 5) * 0.25f), bulletAppState);
    }

    /**
     * Making trap
     * @param x - x
     * @param z - z
     * @param direction - face direction
     */
    public void makeTrap(float x, float z, int direction) {
        trap = new TrapMaster(application, assetManager, currentNode, x, -0.05f, z, direction);
    }

    /**
     * Creating decor
     * @param x - x
     * @param z - z
     * @param direction - face direction
     */
    public void createDecor(float x, float z, float direction) {
        decoration = new Decoration(direction, assetManager, currentNode, x, -0.05f, z, bulletAppState);
    }

    /**
     * Playing random creepy sound
     * @param characterNode - node to attach sound
     */
    public void playCreepySound(Node characterNode) {
        Random rand = new Random();
        if (rand.nextInt(250) == 0) {
            creepySound = new AudioNode(assetManager, "Sounds/LevelSounds/" + rand.nextInt(1, 6) + ".wav", AudioData.DataType.Buffer);
            creepySound.setPositional(false); // Use true for 3D sounds
            creepySound.setLooping(false); // Set to true if you want the sound to loop
            creepySound.setVolume(0.3f*4*settings.getFloat("Master Volume")*settings.getFloat("Sound Volume")); // Set the volume (1 is default, 0 is silent)
            characterNode.attachChild(creepySound);
            creepySound.play();
        }
    }

    /**
     * Playing mine sound
     * @param node - node to attach sound
     */
    public void playMineSound(Node node) {
        trapSound = new AudioNode(assetManager, "Sounds/countdown.wav", AudioData.DataType.Buffer);
        trapSound.setPositional(false); // Use true for 3D sounds
        trapSound.setLooping(false); // Set to true if you want the sound to loop
        trapSound.setVolume(3*4*settings.getFloat("Master Volume")*settings.getFloat("Sound Volume")); // Set the volume (1 is default, 0 is silent)
        node.attachChild(trapSound);
        trapSound.play();
    }

    /**
     * Playing mine explosion sound
     * @param node - node to attach sound
     */
    public void playMineExplodeSound(Node node) {
        trapSound = new AudioNode(assetManager, "Sounds/explosion.wav", AudioData.DataType.Buffer);
        trapSound.setPositional(false); // Use true for 3D sounds
        trapSound.setLooping(false); // Set to true if you want the sound to loop
        trapSound.setVolume(3*4*settings.getFloat("Master Volume")*settings.getFloat("Sound Volume")); // Set the volume (1 is default, 0 is silent)
        node.attachChild(trapSound);
        trapSound.play();
    }

    /**
     * Playing spike sound
     * @param node - node to attach sound
     */
    public void playSpikeSound(Node node) {
        trapSound = new AudioNode(assetManager, "Sounds/metal.wav", AudioData.DataType.Buffer);
        trapSound.setPositional(false); // Use true for 3D sounds
        trapSound.setLooping(false); // Set to true if you want the sound to loop
        trapSound.setVolume(3*4*settings.getFloat("Master Volume")*settings.getFloat("Sound Volume")); // Set the volume (1 is default, 0 is silent)
        node.attachChild(trapSound);
        trapSound.play();
    }

    /**
     * Playing death sound
     */
    public void playDeathSound() {
        AudioNode deathSound = new AudioNode(assetManager, "Sounds/player-death.wav", AudioData.DataType.Buffer);
        deathSound.setPositional(false); // Use true for 3D sounds
        deathSound.setLooping(false); // Set to true if you want the sound to loop
        deathSound.setVolume(10*4*settings.getFloat("Master Volume")*settings.getFloat("Sound Volume")); // Set the volume (1 is default, 0 is silent)
        rootNode.attachChild(deathSound);
        deathSound.play();
    }

    /**
     * Removing torches light
     */
    public void removeLight(){
        for(int i=0;i<labyrinthSizeX;i++){
            for(int j=0;j<labyrinthSizeZ;j++){
                for(TorchHolder torch: allTorches[i][j]){
                    torch.updateTorchStatus(false);
                }
            }
        }

    }


}