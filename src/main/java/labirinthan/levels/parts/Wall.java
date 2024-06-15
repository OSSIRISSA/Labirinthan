/**
 * Task: Game
 * File: Wall.java
 *
 *  @author Iryna Hryshchenko
 */
package labirinthan.levels.parts;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
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

public class Wall extends Box {

    /**
     * Wall constructor
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
    public Wall(float x, float y, float z, AssetManager assetManager, Node localRootNode, float px, float py, float pz, BulletAppState bulletAppState) {
        super(x / 2, y / 2, z / 2);
        Geometry geom = new Geometry("Wall", this);

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

        Texture diffuseTex = assetManager.loadTexture("Textures/bricks.png");
        diffuseTex.setWrap(Texture.WrapMode.Repeat);

        mat.setTexture("DiffuseMap", diffuseTex);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.DarkGray);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 0f);
        geom.setMaterial(mat);

        geom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        this.scaleTextureCoordinates(x, y, z);

        localRootNode.attachChild(geom);
        geom.setLocalTranslation(px, py, pz);

        // Create a BoxCollisionShape and PhysicsRigidBody for the wall
        BoxCollisionShape wallShape = new BoxCollisionShape(new Vector3f(x / 2, y / 2, z / 2));
        PhysicsRigidBody wallPhysics = new PhysicsRigidBody(wallShape, 0.0f);

        // Set the position of the rigid body to match the geometry
        wallPhysics.setPhysicsLocation(geom.getWorldTranslation());

        // Add the rigid body to the physics space
        bulletAppState.getPhysicsSpace().add(wallPhysics);
    }

    /**
     * Scaling texture
     * @param x - x
     * @param y - y
     * @param z - z
     */
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
