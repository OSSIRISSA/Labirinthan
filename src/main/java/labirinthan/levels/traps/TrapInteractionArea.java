/**
 * Task: Game
 * File: TrapInteractionArea.java
 *
 *  @author Max Mormil
 */
package labirinthan.levels.traps;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.objects.PhysicsGhostObject;
import com.jme3.math.Vector3f;

public class TrapInteractionArea extends PhysicsGhostObject {
    private final TrapMaster parent;

    /**
     * TrapInteractionArea constructor
     * @param parent - parent
     * @param dimensions - dimensions vector
     */
    public TrapInteractionArea(TrapMaster parent, Vector3f dimensions) {
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
    public TrapMaster getParent() {
        return parent;
    }
}