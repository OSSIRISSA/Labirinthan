package labirinthan.props;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class TorchHandler extends Node{
    public final Spatial torchHolderMesh;
    private final Torch torch;
    private boolean torchEnabled;
    private final AssetManager assetManager;
    private final PointLight light;
    private final ParticleEmitter fire;
    private final Node torchNode;
    private final Node rootNode;

    //CONSTANTS
    private final Vector3f TORCH_MESH_LOCATION = new Vector3f(-0.11f, 0.1f, 0);
    private final float TORCH_MESH_ROTATION_X = (-FastMath.PI/2)/90*73;
    private final float TORCH_MESH_ROTATION_Y = -FastMath.PI/2;
    private final Vector3f FIRE_LOCATION = new Vector3f(-0.25f, 0.7f, 0);

    public TorchHandler(AssetManager assetManager, Node rootNode) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.torch = new Torch(assetManager);
        this.torchHolderMesh = assetManager.loadModel("Models/TorchHolder/torchholder.fbx");
        torchHolderMesh.rotate(0, -TORCH_MESH_ROTATION_Y, 0);

        torch.torchMesh.setLocalTranslation(TORCH_MESH_LOCATION);
        torch.torchMesh.rotate(TORCH_MESH_ROTATION_X, TORCH_MESH_ROTATION_Y, 0);

        this.attachChild(torchHolderMesh);

        torchNode = new Node("TorchNode");
        torchNode.attachChild(torch.torchMesh);

        light = createLight();
        rootNode.addLight(light);

        fire = createFire();
        torchNode.attachChild(fire);
        fire.setLocalTranslation(FIRE_LOCATION);

        this.attachChild(torchNode);

        updateLightPosition();
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

    private void updateLightPosition(){
        light.setPosition(fire.getWorldTranslation());
    }

    private PointLight createLight(){
        PointLight torchLight = new PointLight();
        torchLight.setColor(ColorRGBA.Orange.mult(4f));
        torchLight.setRadius(15f);
        return torchLight;
    }

    public void rotateTorch(float x, float y, float z) {
        this.rotate(x, y, z);
        updateLightPosition();
    }

    public void moveTorch(float x, float y, float z) {
        this.move(x, y, z);
        updateLightPosition();
    }

    public void updateTorchStatus(boolean torchEnabled){
        if (torchEnabled){
            this.attachChild(torchNode);
            rootNode.addLight(light);
            this.torchEnabled = true;
        } else{
            this.detachChild(torchNode);
            rootNode.removeLight(light);
            this.torchEnabled = false;
        }
    }
}
