package labirinthan.character;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.control.AbstractControl;
import labirinthan.GUI.MainHUD;
import labirinthan.Labirinthan;

public class CameraControl extends AbstractControl {

    private final Labirinthan game;
    private final Camera camera;
    private final Vector3f targetPosition;
    private final Quaternion targetRotation;
    private final float duration;
    private final MainHUD hud;
    private float elapsedTime = 0f;

    public CameraControl(Labirinthan game, Camera camera, Vector3f targetPosition, Quaternion targetRotation, float duration, MainHUD hud) {
        this.game = game;
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

            if (progress<0.4f) hud.updateLidOverlay(progress);
            if (progress>0.8f){
                MainCharacter.isDead = false;
                spatial.removeControl(this);
                game.stop();

            }
        }
    }

    @Override
    protected void controlRender(com.jme3.renderer.RenderManager rm, com.jme3.renderer.ViewPort vp) {
    }
}
