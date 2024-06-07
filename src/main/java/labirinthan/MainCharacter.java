package labirinthan;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import labirinthan.GUI.MainHUD;
import labirinthan.levels.Level;
import labirinthan.props.Torch;

public class MainCharacter extends AbstractAppState implements ActionListener, PhysicsCollisionListener {

    private Labirinthan app;
    private BetterCharacterControl characterControl;
    private Object interactionObject;
    public static Node characterNode;
    private Node cameraNode; // Node for the camera
    private final MainHUD hud;

    private final Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, forward = false, backward = false;
    public boolean isPuzzleFound = false;
    public boolean isCarryingTorch = false;

    private InteractionType interactType = InteractionType.NONE;

    final float CHARACTER_SPEED = 10f;
    final float JUMP_FORCE = 400f;

    private float health = 1.0f;

    private Node torchNode; // Reference to the torch node

    public MainCharacter(MainHUD hud) {
        this.hud = hud;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Labirinthan) app;
        characterNode = new Node("Character");
        characterNode.setLocalTranslation(Labirinthan.X, Labirinthan.Y, Labirinthan.Z);
        characterControl = new BetterCharacterControl(1.4f, 3.0f, 80f);
        characterControl.setJumpForce(new Vector3f(0, JUMP_FORCE, 0));
        characterNode.addControl(characterControl);
        this.app.getRootNode().attachChild(characterNode);

        // Create and attach the camera node
        cameraNode = new Node("Camera Node");
        this.app.getRootNode().attachChild(cameraNode);
        cameraNode.setLocalTranslation(this.app.getCamera().getLocation());

        this.app.bulletAppState.getPhysicsSpace().add(characterControl);

        this.app.getCamera().setFrustumNear(0.1f);
        float aspect = (float) this.app.getCamera().getWidth() / this.app.getCamera().getHeight();
        this.app.getCamera().setFrustumPerspective(45f, aspect, 0.1f, 1000f);

        addCollisionListener();

        initKeys();
    }

    private void initKeys() {
        this.app.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        this.app.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        this.app.getInputManager().addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        this.app.getInputManager().addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        this.app.getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        this.app.getInputManager().addMapping("Interact", new KeyTrigger(KeyInput.KEY_E)); // Add interaction key

        this.app.getInputManager().addListener(this, "Left", "Right", "Forward", "Backward", "Jump", "Interact");
    }

    private void addCollisionListener() {
        this.app.bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        //System.out.println("Collision detected: " + event.getObjectA() + " with " + event.getObjectB());
        if ((event.getObjectA() instanceof TorchInteractionArea || event.getObjectB() instanceof TorchInteractionArea) && !isCarryingTorch) {
            if (event.getObjectA() instanceof TorchInteractionArea) {
                interactionObject = event.getObjectA();
            } else interactionObject = event.getObjectB();
            interactType = InteractionType.TORCH;
            setInteractionText(true, MainHUD.TORCH_INTERACTION_TEXT);
        } else {
            interactType = InteractionType.NONE;
            setInteractionText(false);
        }
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
                if (isPressed && characterControl.isOnGround()) {
                    characterControl.jump();
                }
                break;
            case "Interact":
                if (isPressed && (interactType != InteractionType.NONE)) {
                    interact();
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
        if (!Labirinthan.isFlying) {
            this.app.getCamera().setLocation(characterNode.getLocalTranslation().add(0, 1.8f, 0));
            cameraNode.setLocalTranslation(this.app.getCamera().getLocation()); // Update camera node position
        }

        float[] angles = this.app.getCamera().getRotation().toAngles(null);
        float pitch = FastMath.clamp(angles[0], -FastMath.PI / 4, FastMath.PI / 4); // Clamp between -45 and 45 degrees
        Quaternion clampedRotation = new Quaternion().fromAngles(pitch, angles[1], angles[2]);
        this.app.getCamera().setRotation(clampedRotation);
        cameraNode.setLocalRotation(clampedRotation);

        if (torchNode != null) {
            Vector3f torchPosition = this.app.getCamera().getLocation()
                    .add(this.app.getCamera().getDirection().mult(0.5f))
                    .add(camLeft.mult(-0.02f))
                    .add(0, -0.5f, 0);
            torchNode.setLocalTranslation(torchPosition);

            Quaternion horizontalRotation = new Quaternion().fromAngleAxis(clampedRotation.toAngles(null)[1], Vector3f.UNIT_Y);
            torchNode.setLocalRotation(horizontalRotation.mult(new Quaternion().fromAngleAxis(FastMath.PI / 2, Vector3f.UNIT_Y)));
        }

        crossCheck();
        Labirinthan.level.updateBlocks(characterNode.getLocalTranslation().x,characterNode.getLocalTranslation().z);
    }

    private void carryTorch() {
        if (torchNode == null) {
            torchNode = new Node("Torch Node");
            new Torch(app.getAssetManager(), torchNode);
            this.app.getRootNode().attachChild(torchNode); // Attach torch node to the root node
        }
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

    private void setInteractionText(boolean status, String... text) {
        hud.showInteractionSign(status, text.length > 0 ? text[0] : "");
    }

    private void interact() {
        switch (interactType) {
            case TORCH:
                ((TorchInteractionArea) interactionObject).getParent().updateTorchStatus(false);
                isCarryingTorch = true;
                carryTorch();
                break;
            case PUZZLE:
                System.out.println("Interacted with puzzle");
                break;
            case BOOK:
                System.out.println("Interacted with book");
                break;
            default:
                System.out.println("No interaction available");
                break;
        }
    }
}
