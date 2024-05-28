package labirinthan;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import com.jme3.scene.VertexBuffer;

import java.nio.FloatBuffer;

public class Wall extends Box {

    public Wall(float x, float y, float z, AssetManager assetManager, Node localRootNode, float px, float py, float pz, BulletAppState bulletAppState) {
        super(x / 2, y / 2, z / 2);
        Geometry geom = new Geometry("Wall", this);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetManager.loadTexture("Textures/bricks.png");
        tex.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);

        // Adjust the texture coordinates to stretch vertically and repeat horizontally
        this.scaleTextureCoordinates(x,y,z);

        localRootNode.attachChild(geom);
        geom.setLocalTranslation(px, py, pz);

        RigidBodyControl wallPhysics = new RigidBodyControl(0.0f);
        geom.addControl(wallPhysics);
        bulletAppState.getPhysicsSpace().add(wallPhysics);
    }

    private void scaleTextureCoordinates(float x, float y, float z) {
        FloatBuffer texCoords = BufferUtils.createFloatBuffer(
                // Front face
                0, y,
                x, y,
                x, 0,
                0, 0,
                // Right face
                0, y,
                z, y,
                z, 0,
                0, 0,
                //Back face
                0, y,
                x, y,
                x, 0,
                0, 0,
                // Left face
                0, y,
                z, y,
                z, 0,
                0, 0,
                // Top face
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                // Bottom face
                0, 0,
                0, 0,
                0, 0,
                0, 0);
        setBuffer(VertexBuffer.Type.TexCoord, 2, texCoords);
    }
}
