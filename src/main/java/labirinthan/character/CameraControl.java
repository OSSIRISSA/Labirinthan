package labirinthan.character;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.control.AbstractControl;
import labirinthan.GUI.MainHUD;

public class CameraControl extends AbstractControl {

    private final Camera camera;
    private final Vector3f targetPosition;
    private final Quaternion targetRotation;
    private final float duration;
    private final MainHUD hud;
    private float elapsedTime = 0f;

    public CameraControl(Camera camera, Vector3f targetPosition, Quaternion targetRotation, float duration, MainHUD hud) {
        this.camera = camera;
        this.targetPosition = targetPosition;
        this.targetRotation = targetRotation;
        this.duration = duration;
        this.hud = hud;

        hud.detachAllExceptLids();
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (elapsedTime < duration) {
            elapsedTime += tpf;
            float progress = elapsedTime / duration;

            // Interpolate position
            Vector3f newPosition = camera.getLocation().interpolateLocal(targetPosition, progress);
            camera.setLocation(newPosition);

            // Interpolate rotation
            Quaternion newRotation = new Quaternion().slerp(camera.getRotation(), targetRotation, progress);
            camera.setRotation(newRotation);

            if (progress<0.5) hud.updateLidOverlay(progress);
        } else {
            spatial.removeControl(this);
        }
    }

    @Override
    protected void controlRender(com.jme3.renderer.RenderManager rm, com.jme3.renderer.ViewPort vp) {
        // No rendering needed
    }
}
