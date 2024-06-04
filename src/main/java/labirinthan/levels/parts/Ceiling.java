package labirinthan.levels.parts;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class Ceiling extends Box {

    public Ceiling(float x, float y, float z, AssetManager assetManager, Node localRootNode, float px, float py, float pz, BulletAppState bulletAppState) {
        super(x / 2, y / 2, z / 2);
        Geometry geom = new Geometry("Ceiling", this);

        // Use Lighting material instead of Unshaded
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

        // Load and set the diffuse texture (the main texture)
        Texture tex = assetManager.loadTexture("Textures/concrete.png");
        tex.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", tex);

        // Set material properties
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.DarkGray);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 1f);
        geom.setMaterial(mat);

        geom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        localRootNode.attachChild(geom);
        geom.setLocalTranslation(px, py, pz);

        RigidBodyControl ceilingPhysics = new RigidBodyControl(0.0f);
        geom.addControl(ceilingPhysics);
        bulletAppState.getPhysicsSpace().add(ceilingPhysics);
    }
}
