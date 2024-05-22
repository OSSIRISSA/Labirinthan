package labirinthan;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * This is the Main Class of your Game. It should boot up your game and do initial initialisation
 * Move your Logic into AppStates or Controls or other java classes
 */
public class Labirinthan extends SimpleApplication {

    public static void main(String[] args) {
        Labirinthan app = new Labirinthan();
        app.setShowSettings(true); //Settings dialog not supported on mac
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //Spatial m = assetManager.loadModel("src/1.obj");
        /*Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);*/

        assetManager.registerLocator("assets/", FileLocator.class);
        Spatial model = assetManager.loadModel("Models/1.obj");
        model.setLocalScale(0.5F, 0.5F, 0.5F);
        rootNode.attachChild(model);
        flyCam.setMoveSpeed(100);
        model.setLocalTranslation(0,0,0);

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(10f));
        rootNode.addLight(al);
        startLevel0();

        //rootNode.attachChild(geom);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //this method will be called every game tick and can be used to make updates
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //add render code here (if any)
    }

    public void startLevel0(){
        stateManager.attach(new Level0(this));
    }
}
