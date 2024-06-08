package labirinthan.props;

import com.jme3.asset.AssetManager;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.PointLightShadowFilter;
import com.jme3.shadow.PointLightShadowRenderer;
import labirinthan.Labirinthan;

import java.util.ArrayList;

public class TorchHolder extends Node {
    public final Spatial torchHolderMesh;
    private final Labirinthan app;
    public boolean torchEnabled;
    public final PointLight light;
    private final Node torchNode;
    private final Node rootNode;
    private final Node lightNode;
    private final Node interactionAreaNode;
    private PointLightShadowFilter plsf;
    private PointLightShadowRenderer plsr;
    private final TorchInteractionArea interactionArea;
    public boolean isFirst;

    int SHADOW_MAP = 256;

    public TorchHolder(Labirinthan application, AssetManager assetManager, Node rootNode, boolean isFirst, int roomsNumber, ArrayList<TorchHolder> allTorches) {
        this.app = application;
        this.rootNode = rootNode;
        this.isFirst = isFirst;

        torchNode = new Node("TorchNode");
        new Torch(assetManager, torchNode);
        this.attachChild(torchNode);

        this.torchHolderMesh = assetManager.loadModel("Models/TorchHolder/torchholder.fbx");
        torchHolderMesh.rotate(0, FastMath.PI/2, 0);
        this.attachChild(torchHolderMesh);

        lightNode = new Node("LightNode");
        this.attachChild(lightNode);
        lightNode.move(-0.5f, 1, 0);
        this.light = createLight(this.getWorldTranslation(), assetManager);

        updateLight();

        interactionAreaNode = new Node("InteractionArea");
        this.attachChild(interactionAreaNode);
        interactionAreaNode.move(-0.65f, 0, 0);
        interactionArea = new TorchInteractionArea(this, new Vector3f(0.6f, 1f, 1f));
    }

    private void createShadowRenderer(AssetManager assetManager, int shadowMapSize) {
        plsr = new PointLightShadowRenderer(assetManager, shadowMapSize);
        plsr.setLight(light);
        plsr.setShadowIntensity(0.1f/9);
        app.getViewPort().addProcessor(plsr);
    }

    private void createShadowFilter(AssetManager assetManager, int shadowMapSize) {
        plsf = new PointLightShadowFilter(assetManager, shadowMapSize);
        plsf.setLight(light);
        plsf.setShadowIntensity(0.1f/9);
        plsf.setEnabled(true);
        app.filterPostProcessor.addFilter(plsf);
    }

    private void updateLight() {
        light.setPosition(lightNode.getWorldTranslation());
        if (interactionArea != null && interactionAreaNode != null) {
            interactionArea.setPosition(interactionAreaNode.getWorldTranslation());
        }
    }

    public PointLight createLight(Vector3f lightSourcePosition, AssetManager assetManager) {
        PointLight torchLight = new PointLight();
        torchLight.setColor(ColorRGBA.Orange.mult(10f));
        torchLight.setRadius(15f);
        torchLight.setPosition(lightSourcePosition);
        if (isFirst) {
            createShadowRenderer(assetManager, SHADOW_MAP);
        } else {
            createShadowFilter(assetManager, SHADOW_MAP);
        }
        return torchLight;
    }

    public void rotateTorch(float x, float y, float z) {
        this.rotate(x, y, z);
        updateLight();
        interactionArea.setPhysicsRotation(new Quaternion().fromAngleAxis(y, Vector3f.UNIT_Y));
    }

    public void moveTorch(float x, float y, float z) {
        this.move(x, y, z);
        updateLight();
    }

    public void updateTorchStatus(boolean status) {
        if (status && !this.torchEnabled) {
            this.attachChild(torchNode);
            rootNode.addLight(light);
            interactionArea.addToPhysicsSpace(app.bulletAppState.getPhysicsSpace());
            refreshShadows();
            this.torchEnabled = true;
        } else if (!status && this.torchEnabled) {
            this.detachChild(torchNode);
            rootNode.removeLight(light);
            interactionArea.removeFromPhysicsSpace(app.bulletAppState.getPhysicsSpace());
            cleanupShadows();
            this.torchEnabled = false;
        }
    }

    private void refreshShadows() {
        if (plsr != null) {
            app.getViewPort().removeProcessor(plsr);
            createShadowRenderer(app.getAssetManager(), SHADOW_MAP);
        }
        if (plsf != null) {
            app.filterPostProcessor.removeFilter(plsf);
            createShadowFilter(app.getAssetManager(), SHADOW_MAP);
        }
    }

    public void cleanupShadows() {
        if (plsr != null) {
            app.getViewPort().removeProcessor(plsr);
            plsr = null;
        }
        if (plsf != null) {
            app.filterPostProcessor.removeFilter(plsf);
            plsf = null;
        }
    }
}
