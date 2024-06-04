package labirinthan;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import labirinthan.levels.Level;
import labirinthan.GUI.MainHUD;

public class MainCharacter extends AbstractAppState implements ActionListener {

    private Labirinthan app;
    private BulletAppState bulletAppState;
    private BetterCharacterControl characterControl;
    public static Node characterNode;
    private final MainHUD hud;

    private final Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, forward = false, backward = false;

    private boolean isPuzzleFound = false;

    private final float CHARACTER_SPEED = 10f;
    private final float JUMP_FORCE = 400f; // Adjust this value to make the jump appropriate

    private float health = 1.0f; // full health

    public MainCharacter(MainHUD hud) {
        this.hud = hud;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Labirinthan) app;
        bulletAppState = this.app.getStateManager().getState(BulletAppState.class);

        characterNode = new Node("Character");
        characterNode.setLocalTranslation(Labirinthan.X, Labirinthan.Y, Labirinthan.Z);
        characterControl = new BetterCharacterControl(1.4f, 3.0f, 80f);
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
        if (!Labirinthan.isFlying) this.app.getCamera().setLocation(characterNode.getLocalTranslation().add(0, 1.8f, 0));

        crossCheck();
    }

    private void crossCheck() {
        if ((characterNode.getLocalTranslation().x <= Labirinthan.level.blocksInfo.get(Labirinthan.level.chooseCross).get(0) + Level.passageWidth / 2)
                && (characterNode.getLocalTranslation().x >= Labirinthan.level.blocksInfo.get(Labirinthan.level.chooseCross).get(0) - Level.passageWidth / 2)
                && (characterNode.getLocalTranslation().z <= Labirinthan.level.blocksInfo.get(Labirinthan.level.chooseCross).get(1) + Level.passageWidth / 2)
                && (characterNode.getLocalTranslation().z >= Labirinthan.level.blocksInfo.get(Labirinthan.level.chooseCross).get(1) - Level.passageWidth / 2)
                && !isPuzzleFound) {
            this.app.getRootNode().detachChild(characterNode);
            app.stopLevel();
            Labirinthan.level.startPuzzle();
            isPuzzleFound = true;
        }
    }

    public void takeDamage(float damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        hud.updateHealth(health);
    }

    public void heal(float amount) {
        health += amount;
        if (health > 1.0f) {
            health = 1.0f;
        }
        hud.updateHealth(health);
    }

    public void checkForInteraction() {
        // Logic to check if interaction is possible
        boolean canInteract = true; // Replace with actual logic
        hud.showInteractionSign(canInteract);
    }
}