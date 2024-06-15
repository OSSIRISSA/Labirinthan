/**
 * Task: Game
 * File: PuzzleCabinet.java
 *
 *  @author Max Mormil
 */
package labirinthan.levels.puzzles;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import labirinthan.Labirinthan;

public class PuzzleCabinet extends Node {

    private final Spatial lecternMesh;
    private Spatial bookMesh;
    private final Labirinthan app;
    private final Node rootNode;
    private final Node interactionAreaNode;
    private final PuzzleInteractionArea interactionArea;
    public PuzzleType puzzleType;

    private float animationTime = 0;
    private boolean movingUp = true;

    /**
     * PuzzleCabinet constructor
     * @param application - Labirinthan app
     * @param assetManager - assetManager
     * @param localRootNode - localRootNode
     * @param px - x
     * @param py - y
     * @param pz - z
     * @param puzzleType - type of puzzle
     * @param direction - facing direction
     */
    public PuzzleCabinet(Labirinthan application, AssetManager assetManager, Node localRootNode, float px, float py, float pz, PuzzleType puzzleType, float direction) {
        this.app = application;
        this.rootNode = localRootNode;
        this.puzzleType = puzzleType;

        localRootNode.attachChild(this);
        this.setLocalTranslation(px, py + 1.2f, pz);

        this.lecternMesh = assetManager.loadModel("Models/Books/lectern.glb");
        lecternMesh.scale(0.03f);
        lecternMesh.move(0, -1.2f, 0);
        this.attachChild(lecternMesh);

        switch (puzzleType) {
            case SUDOKU -> {
                this.bookMesh = assetManager.loadModel("Models/Books/book.glb");
                bookMesh.scale(0.005f);
                bookMesh.move(0, 0.6f, 0);
                bookMesh.rotate(FastMath.PI / 4, FastMath.PI / 2, 0);
            }
            case PYRAMID -> {
                this.bookMesh = assetManager.loadModel("Models/Books/medieval_open_book_1.glb");
                bookMesh.scale(0.3f);
                bookMesh.move(0, 0.6f, 0);
                bookMesh.rotate(-FastMath.PI / 4, FastMath.PI / 2, 0);
            }
            case ENCRYPTION -> {
                this.bookMesh = assetManager.loadModel("Models/Books/fire_book.glb");
                bookMesh.scale(0.3f);
                bookMesh.move(0, 0.6f, 0);
                bookMesh.rotate(-FastMath.PI / 4, FastMath.PI / 2, 0);
            }
        }
        this.attachChild(bookMesh);

        switch ((int) direction) {
            case 1 -> this.rotate(0, -FastMath.HALF_PI, 0);
            case 4 -> this.rotate(0, FastMath.PI, 0);
            case 3 -> this.rotate(0, FastMath.HALF_PI, 0);
        }

        interactionAreaNode = new Node("InteractionArea");
        this.attachChild(interactionAreaNode);
        interactionArea = new PuzzleInteractionArea(this, new Vector3f(2f, 1.2f, 2f));
        interactionArea.setPosition(this.getWorldTranslation());
        app.bulletAppState.getPhysicsSpace().add(interactionArea);
    }

    /**
     * Removing interaction zone
     */
    public void removeInteractionZone(){
        app.bulletAppState.getPhysicsSpace().remove(interactionArea);
    }

    @Override
    public void updateLogicalState(float tpf) {
        super.updateLogicalState(tpf);
        animateBook(tpf);
    }

    /**
     * Puzzle book animation
     * @param tpf - tpf
     */
    private void animateBook(float tpf) {
        float amplitude = 0.1f; // Height of the movement
        float speed = 1f; // Speed of the movement

        animationTime += tpf * speed;
        float offset = FastMath.sin(animationTime) * amplitude;

        bookMesh.setLocalTranslation(0, 0.6f + offset, 0);
    }
}
