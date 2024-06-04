package labirinthan.levels.parts;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class Cross extends Box {

    public Cross(float x, float y, float z, AssetManager assetManager, Node localRootNode, float px, float py, float pz, BulletAppState bulletAppState) {
        super(x/2, y/2, z/2);
        Geometry geom = new Geometry("Floor", this);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        geom.setMaterial(mat);
        localRootNode.attachChild(geom);
        geom.setLocalTranslation(px, py+0.01f, pz);

        RigidBodyControl floorPhysics = new RigidBodyControl(0.0f);
        geom.addControl(floorPhysics);
        bulletAppState.getPhysicsSpace().add(floorPhysics);
    }
}