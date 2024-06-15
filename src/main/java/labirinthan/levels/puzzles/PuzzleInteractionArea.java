/**
 * Task: Game
 * File: PuzzleInteractionArea.java
 *
 *  @author Max Mormil
 */
package labirinthan.levels.puzzles;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.objects.PhysicsGhostObject;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class PuzzleInteractionArea extends PhysicsGhostObject {
    private Vector3f boxDimensions;
    private final PuzzleCabinet parent;

    /**
     * PuzzleInteractionArea constructor
     * @param parent - parent
     * @param dimensions - dimensions vector
     */
    public PuzzleInteractionArea(PuzzleCabinet parent, Vector3f dimensions) {
        super(parent, new BoxCollisionShape(dimensions));
        this.parent = parent;
        this.boxDimensions = dimensions;
    }

    /**
     * Setting position of area
     * @param position - needed position
     */
    public void setPosition(Vector3f position) {
        this.setPhysicsLocation(position);
    }

    public void setRotation(Quaternion rotation) {
        this.setPhysicsRotation(rotation);
    }

    public void addToPhysicsSpace(PhysicsSpace physicsSpace) {
        physicsSpace.add(this);
    }

    /**
     * Parent getter
     * @return - parent
     */
    public PuzzleCabinet getParent() {
        return parent;
    }

    public void removeFromPhysicsSpace(PhysicsSpace physicsSpace) {
        physicsSpace.remove(this);
    }

    public Vector3f getBoxDimensions() {
        return boxDimensions;
    }

    public void setBoxDimensions(Vector3f boxDimensions) {
        this.boxDimensions = boxDimensions;
        this.setCollisionShape(new BoxCollisionShape(boxDimensions));
    }
}
