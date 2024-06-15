/**
 * Task: Game
 * File: Floor.java
 *
 *  @author Iryna Hryshchenko
 */
package labirinthan.levels.parts;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

import java.nio.FloatBuffer;

public class Floor extends Box {

    /**
     * Floor constructor
     * @param x - size x
     * @param y - size y
     * @param z - size z
     * @param assetManager - asset manager
     * @param localRootNode - localRootNode
     * @param px - x
     * @param py - y
     * @param pz - z
     * @param bulletAppState - bulletAppState
     */
    public Floor(float x, float y, float z, AssetManager assetManager, Node localRootNode, float px, float py, float pz, BulletAppState bulletAppState) {
        super(x / 2, y / 2, z / 2);
        Geometry geom = new Geometry("Floor", this);

        // Use Lighting material instead of Unshaded
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

        // Load and set the diffuse texture (the main texture)
        Texture tex = assetManager.loadTexture("Textures/grass-floor.jpg");
        tex.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", tex);

        geom.setShadowMode(RenderQueue.ShadowMode.Receive);

        // Set material properties
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.Gray);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 0f);

        geom.setMaterial(mat);

        this.scaleTextureCoordinates(x, z);

        localRootNode.attachChild(geom);
        geom.setLocalTranslation(px, py, pz);

        // Create a BoxCollisionShape and PhysicsRigidBody for the floor
        BoxCollisionShape floorShape = new BoxCollisionShape(new Vector3f(x / 2, y / 2, z / 2));
        PhysicsRigidBody floorPhysics = new PhysicsRigidBody(floorShape, 0.0f); // Mass = 0.0f for static objects

        // Set the position of the rigid body to match the geometry
        floorPhysics.setPhysicsLocation(geom.getWorldTranslation());

        floorPhysics.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        floorPhysics.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01);

        // Add the rigid body to the physics space
        bulletAppState.getPhysicsSpace().add(floorPhysics);
    }

    /**
     * Scaling texture
     * @param x - x
     * @param z - z
     */
    private void scaleTextureCoordinates(float x, float z) {
        FloatBuffer texCoords = BufferUtils.createFloatBuffer(
                // Front face
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                // Right face
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                // Back face
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                // Left face
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                // Top face
                0, x,
                x, z,
                z, 0,
                0, 0,
                // Bottom face
                0, x,
                x, z,
                z, 0,
                0, 0);
        setBuffer(VertexBuffer.Type.TexCoord, 2, texCoords);
    }
}
