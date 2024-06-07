package labirinthan;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.objects.PhysicsGhostObject;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import labirinthan.props.TorchHolder;

public class TorchInteractionArea extends PhysicsGhostObject {
    private Vector3f boxDimensions;
    private TorchHolder parent;

    public TorchInteractionArea(TorchHolder parent, Vector3f dimensions) {
        super(parent, new BoxCollisionShape(dimensions));
        this.parent = parent;
        this.boxDimensions = dimensions;
    }

    public void setPosition(Vector3f position) {
        this.setPhysicsLocation(position);
    }

    public void setRotation(Quaternion rotation) {
        this.setPhysicsRotation(rotation);
    }

    public void addToPhysicsSpace(PhysicsSpace physicsSpace) {
        physicsSpace.add(this);
    }

    public TorchHolder getParent() {
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
