package labirinthan;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class Floor extends Box {

    public Floor(float x, float y, float z, AssetManager assetManager, Node localRootNode, float px, float py, float pz) {
        super(x, y, z);
        Geometry geom = new Geometry("Floor", (Box) (this));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Gray);
        geom.setMaterial(mat);
        localRootNode.attachChild(geom);
        geom.setLocalTranslation(px, py, pz);
    }

}
