package labirinthan;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class Wall extends Box {

    public Wall(float x, float y, float z, AssetManager assetManager, Node localRootNode, float px, float py, float pz) {
        super(x/2, y/2, z/2);
        Geometry geom = new Geometry("Wall", (Box) (this));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetManager.loadTexture("Textures/br.png");
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);
        localRootNode.attachChild(geom);
        geom.setLocalTranslation(px, py, pz);
    }

    public Wall(float x, float y, float z, AssetManager assetManager, Node localRootNode, float px, float py, float pz,ColorRGBA color) {
        super(x/2, y/2, z/2);
        Geometry geom = new Geometry("Wall", (Box) (this));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetManager.loadTexture("Textures/br.png");
        mat.setTexture("ColorMap", tex);
        //mat.getTextureParam("ColorMap").getTextureValue().setWrap(Texture.WrapMode.Repeat);
        mat.setColor("Color", color);
        geom.setMaterial(mat);
        localRootNode.attachChild(geom);
        geom.setLocalTranslation(px, py, pz);
    }
}
