/**
 * Task: Game
 * File: PuzzleInteractionArea.java
 *
 *  @author Max Mormil
 */
package labirinthan.levels.puzzles;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.objects.PhysicsGhostObject;
import com.jme3.math.Vector3f;

public class PuzzleInteractionArea extends PhysicsGhostObject {
    private final PuzzleCabinet parent;

    /**
     * PuzzleInteractionArea constructor
     * @param parent - parent
     * @param dimensions - dimensions vector
     */
    public PuzzleInteractionArea(PuzzleCabinet parent, Vector3f dimensions) {
        super(parent, new BoxCollisionShape(dimensions));
        this.parent = parent;
    }

    /**
     * Setting position of area
     * @param position - needed position
     */
    public void setPosition(Vector3f position) {
        this.setPhysicsLocation(position);
    }

    /**
     * Parent getter
     * @return - parent
     */
    public PuzzleCabinet getParent() {
        return parent;
    }
}