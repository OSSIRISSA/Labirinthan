package labirinthan.props;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

public class Torch {

    public final Spatial torchMesh;

    protected Torch(AssetManager assetManager){
        this.torchMesh = assetManager.loadModel("Models/Torch/torch.fbx");
    }
}
