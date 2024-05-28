package labirinthan;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class MainCharacter extends AbstractAppState implements ActionListener {

    private SimpleApplication app;
    private BulletAppState bulletAppState;
    private BetterCharacterControl characterControl;
    private Node characterNode;

    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, forward = false, backward = false;

    private final float CHARACTER_SPEED = 10f;
    private final float JUMP_FORCE = 400f; // Adjust this value to make the jump appropriate

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        bulletAppState = this.app.getStateManager().getState(BulletAppState.class);

        characterNode = new Node("Character");
        characterNode.setLocalTranslation(Labirinthan.X,Labirinthan.Y,Labirinthan.Z);
        characterControl = new BetterCharacterControl(1.2f, 2.5f, 80f);
        characterControl.setJumpForce(new Vector3f(0, JUMP_FORCE, 0));
        characterControl.setGravity(new Vector3f(0, -9.81f, 0)); // Ensure gravity is set correctly
        characterNode.addControl(characterControl);
        this.app.getRootNode().attachChild(characterNode);
        bulletAppState.getPhysicsSpace().add(characterControl);

        initKeys();
    }

    private void initKeys() {
        this.app.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        this.app.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        this.app.getInputManager().addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        this.app.getInputManager().addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        this.app.getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));

        this.app.getInputManager().addListener(this, "Left", "Right", "Forward", "Backward", "Jump");
    }

    @Override
    public void onAction(String binding, boolean isPressed, float tpf) {
        switch (binding) {
            case "Left":
                left = isPressed;
                break;
            case "Right":
                right = isPressed;
                break;
            case "Forward":
                forward = isPressed;
                break;
            case "Backward":
                backward = isPressed;
                break;
            case "Jump":
                if (isPressed && characterControl.isOnGround()) { // Ensure jump is only triggered when on the ground
                    characterControl.jump();
                }
                break;
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        Vector3f camDir = this.app.getCamera().getDirection().clone().setY(0).normalizeLocal().multLocal(CHARACTER_SPEED);
        Vector3f camLeft = this.app.getCamera().getLeft().clone().setY(0).normalizeLocal().multLocal(CHARACTER_SPEED);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (forward) {
            walkDirection.addLocal(camDir);
        }
        if (backward) {
            walkDirection.addLocal(camDir.negate());
        }
        characterControl.setWalkDirection(walkDirection);
        this.app.getCamera().setLocation(characterNode.getLocalTranslation().add(0, 1.8f, 0));
    }
}
