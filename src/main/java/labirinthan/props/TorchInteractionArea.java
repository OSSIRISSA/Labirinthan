/**
 * Task: Game
 * File: TorchInteractionArea.java
 *
 *  @author Max Mormil
 */
package labirinthan.props;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.objects.PhysicsGhostObject;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class TorchInteractionArea extends PhysicsGhostObject {
    private Vector3f boxDimensions;
    private final TorchHolder parent;

    /**
     * TorchInteractionArea constructor
     * @param parent - parent
     * @param dimensions - dimensions vector
     */
    public TorchInteractionArea(TorchHolder parent, Vector3f dimensions) {
        super(parent, new BoxCollisionShape(dimensions));
        this.parent = parent;
        this.boxDimensions = dimensions;
    }

    /**
     * Setting position
     * @param position - position vector
     */
    public void setPosition(Vector3f position) {
        this.setPhysicsLocation(position);
    }

    public void setRotation(Quaternion rotation) {
        this.setPhysicsRotation(rotation);
    }

    /**
     * Adding to physics space
     * @param physicsSpace - physics space
     */
    public void addToPhysicsSpace(PhysicsSpace physicsSpace) {
        physicsSpace.add(this);
    }

    /**
     * Parent getter
     * @return - parent
     */
    public TorchHolder getParent() {
        return parent;
    }

    /**
     * Removing from physics space
     * @param physicsSpace - physics space
     */
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
