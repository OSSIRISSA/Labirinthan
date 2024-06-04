package labirinthan.props;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Torch {

    public final Spatial torchMesh;
    private final AssetManager assetManager;
    public final ParticleEmitter fire;

    final Vector3f TORCH_MESH_LOCATION = new Vector3f(-0.11f, 0.1f, 0);
    final float TORCH_MESH_ROTATION_X = (-FastMath.PI/2)/90*73;
    final static float TORCH_MESH_ROTATION_Y = -FastMath.PI/2;
    final Vector3f FIRE_LOCATION = new Vector3f(-0.25f, 0.7f, 0);

    protected Torch(AssetManager assetManager, Node torchNode){
        this.assetManager = assetManager;
        this.torchMesh = assetManager.loadModel("Models/Torch/torch.fbx");
        torchMesh.setShadowMode(RenderQueue.ShadowMode.Receive);

        this.torchMesh.setLocalTranslation(TORCH_MESH_LOCATION);
        this.torchMesh.rotate(TORCH_MESH_ROTATION_X, TORCH_MESH_ROTATION_Y, 0);

        torchNode.attachChild(this.torchMesh);

        fire = createFire();
        torchNode.attachChild(fire);
        fire.setLocalTranslation(FIRE_LOCATION);
    }

    private ParticleEmitter createFire(){
        ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 13);
        Material matRed = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        matRed.setTexture("Texture", assetManager.loadTexture("Textures/flame.png"));
        fire.setMaterial(matRed);
        fire.setImagesX(1);
        fire.setImagesY(1);
        fire.setEndColor(new ColorRGBA(ColorRGBA.Red));
        fire.setStartColor(new ColorRGBA(ColorRGBA.Yellow));
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0.5f, 0));
        fire.setStartSize(0.25f);
        fire.setEndSize(0.01f);
        fire.setGravity(0, 0.1f, 0);
        fire.setLowLife(1f);
        fire.setHighLife(3f);
        fire.getParticleInfluencer().setVelocityVariation(0.1f);

        return fire;
    }
}
