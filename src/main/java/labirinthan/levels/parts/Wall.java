package labirinthan.levels.parts;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

import java.nio.FloatBuffer;

public class Wall extends Box {

    public Wall(float x, float y, float z, AssetManager assetManager, Node localRootNode, float px, float py, float pz, BulletAppState bulletAppState) {
        super(x / 2, y / 2, z / 2);
        Geometry geom = new Geometry("Wall", this);

        //коли розставиш факели - це комітиш
        //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //це розкомітиш
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

        Texture diffuseTex = assetManager.loadTexture("Textures/bricks.png");
        diffuseTex.setWrap(Texture.WrapMode.Repeat);

        //це комітиш
        //mat.setTexture("ColorMap", diffuseTex);

        //а це все розкомітиш --------------------------- щоб бачити де накладання, зі світлом ясно буде
        mat.setTexture("DiffuseMap", diffuseTex);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.DarkGray);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 0f);
        geom.setMaterial(mat);

        geom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
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
