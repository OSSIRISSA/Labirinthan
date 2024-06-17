/**
 * Task: Game
 * File: CameraControl.java
 *
 *  @author Max Mormil
 */
package labirinthan.character;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.control.AbstractControl;
import labirinthan.GUI.MainHUD;
import labirinthan.Labirinthan;

public class CameraControl extends AbstractControl {
    private final Labirinthan game;
    private final MainCharacter character;
    private final Camera camera;
    private final Vector3f targetPosition;
    private final Quaternion targetRotation;
    private final float duration;
    private final MainHUD hud;
    private float elapsedTime = 0f;

    private boolean stopped = false;

    /**
     * CameraControl init
     * @param game - Labirinthan app
     * @param camera - cam
     * @param targetPosition - demanded position
     * @param targetRotation - demanded rotation
     * @param duration - duration
     * @param hud - current hud
     */
    public CameraControl(Labirinthan game, MainCharacter character, Camera camera, Vector3f targetPosition, Quaternion targetRotation, float duration, MainHUD hud) {
        this.game = game;
        this.character = character;
        this.camera = camera;
        this.targetPosition = targetPosition;
        this.targetRotation = targetRotation;
        this.duration = duration;
        this.hud = hud;

        hud.detachAllExceptLids();
    }

    @Override
    protected void controlUpdate(float tpf) {
        if(stopped) return;
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
                character.isDead = false;
                game.startGame();
                stopped=true;
            }
        }
    }

    @Override
    protected void controlRender(com.jme3.renderer.RenderManager rm, com.jme3.renderer.ViewPort vp) {
    }
}
