package labirinthan.props;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.Collidable;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;
import com.jme3.shadow.PointLightShadowFilter;
import com.jme3.shadow.PointLightShadowRenderer;
import labirinthan.Labirinthan;

public class TorchHolder extends Node {
    public final Spatial torchHolderMesh;
    private final Torch torch;
    public boolean torchEnabled;
    public final PointLight light;
    private final Node torchNode;
    private final Node rootNode;
    private final Node lightNode;
    Geometry inGeometry;
    private final BoundingBox interactionZone;
    private PointLightShadowRenderer plsr;
    private PointLightShadowFilter plsf;

    int SHADOW_MAP = 256;

    public TorchHolder(Labirinthan application, AssetManager assetManager, Node rootNode, boolean isFirst) {
        this.rootNode = rootNode;

        torchNode = new Node();
        this.torch = new Torch(assetManager, torchNode);
        this.attachChild(torchNode);

        this.torchHolderMesh = assetManager.loadModel("Models/TorchHolder/torchholder.fbx");
        torchHolderMesh.rotate(0, -Torch.TORCH_MESH_ROTATION_Y, 0);
        this.attachChild(torchHolderMesh);

        lightNode = new Node();
        this.attachChild(lightNode);
        lightNode.move(-0.5f, 1, 0);
        this.light = createLight(this.getWorldTranslation());
        this.rootNode.addLight(this.light);

        WireBox wireSphere = new WireBox(0.1f, 0.1f, 0.1f);
        inGeometry = new Geometry("InteractionZone", wireSphere);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        inGeometry.setMaterial(mat);
        lightNode.attachChild(inGeometry);
        updateLight();

        interactionZone = createInteractionBox(assetManager);

        if (isFirst){
            createShadowRenderer(application, assetManager, SHADOW_MAP);
        } else{
            createShadowFilter(application, assetManager, SHADOW_MAP);
        }
    }

    private void createShadowRenderer(Labirinthan labirinthan, AssetManager assetManager, int shadowMapSize){
        plsr = new PointLightShadowRenderer(assetManager, shadowMapSize);
        plsr.setLight(light);
        plsr.setShadowIntensity(0.3f);
        labirinthan.getViewPort().addProcessor(plsr);
    }

    private void createShadowFilter(Labirinthan labirinthan, AssetManager assetManager, int shadowMapSize){
        plsf = new PointLightShadowFilter(assetManager, shadowMapSize);
        plsf.setLight(light);
        plsf.setShadowIntensity(0.3f);
        plsf.setEnabled(true);
        labirinthan.filterPostProcessor.addFilter(plsf);
    }


    private void updateLight() {
        light.setPosition(lightNode.getWorldTranslation());
        System.out.println(light.getPosition());
    }

    private PointLight createLight(Vector3f lightSourcePosition) {
        PointLight torchLight = new PointLight();
        torchLight.setColor(ColorRGBA.Orange.mult(10f));
        torchLight.setRadius(15f);
        torchLight.setPosition(lightSourcePosition);
        return torchLight;
    }

    private BoundingBox createInteractionBox(AssetManager assetManager) {
        BoundingBox box = new BoundingBox(this.getLocalTranslation().addLocal(-0.5f, 0, 0), 0.5f, 1, 1);


        WireBox wireSphere = new WireBox(box.getXExtent(), box.getYExtent(), box.getZExtent());
        Geometry interactionZoneGeometry = new Geometry("InteractionZone", wireSphere);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        interactionZoneGeometry.setMaterial(mat);
        interactionZoneGeometry.setLocalTranslation(box.getCenter());
        this.attachChild(interactionZoneGeometry);
        return box;
    }

    public void rotateTorch(float x, float y, float z) {
        this.rotate(x, y, z);
        updateLight();
    }

    public void moveTorch(float x, float y, float z) {
        this.move(x, y, z);
        updateLight();
    }

    public void updateTorchStatus(boolean torchEnabled) {
        if (torchEnabled) {
            this.attachChild(torchNode);
            rootNode.addLight(light);
            this.torchEnabled = true;
        } else {
            this.detachChild(torchNode);
            rootNode.removeLight(light);
            this.torchEnabled = false;
        }
    }

    public boolean isCharacterInInteractionZone(Collidable character) {
        CollisionResults results = new CollisionResults();
        interactionZone.collideWith(character, results);
        return results.size() > 0;
    }
}