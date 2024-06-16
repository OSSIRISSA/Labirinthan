/**
 * Task: Game
 * File: MainCharacter.java
 *
 *  @author Max Mormil
 */
package labirinthan.character;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.PointLight;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import labirinthan.GUI.MainHUD;
import labirinthan.GUI.PuzzlePyramid;
import labirinthan.GUI.PuzzleSquareEncryption;
import labirinthan.GUI.PuzzleSudoku;
import labirinthan.Labirinthan;
import labirinthan.levels.puzzles.PuzzleCabinet;
import labirinthan.levels.puzzles.PuzzleInteractionArea;
import labirinthan.levels.traps.TrapInteractionArea;
import labirinthan.levels.traps.TrapMaster;
import labirinthan.props.Torch;
import labirinthan.props.TorchHolder;
import labirinthan.props.TorchInteractionArea;

public class MainCharacter extends AbstractAppState implements ActionListener, PhysicsCollisionListener {

    private final AppSettings settings;
    private Labirinthan app;
    private BetterCharacterControl characterControl;
    private Object interactionObject;
    public static Node characterNode;
    public Node cameraNode;
    public final MainHUD hud;
    private PointLight light;

    private final Vector3f walkDirection = new Vector3f();
    private boolean left, right, forward, backward;
    public boolean isCarryingTorch = false;
    public boolean isPuzzleFound = false;
    public boolean isDead = false;

    private InteractionType interactType = InteractionType.NONE;

    final float CHARACTER_SPEED = 10f;
    final float JUMP_FORCE = 400f;

    public float health = 1.0f;

    private Node torchNode;
    private Node lightNode;
    private TorchHolder torchHolder;

    private float torchTimer = 0f;
    private final float TORCH_DURATION = 30f;

    /**
     * Main Character constructor
     * @param hud - current hud
     * @param settings - settings
     */
    public MainCharacter(MainHUD hud, AppSettings settings) {
        this.hud = hud;
        this.settings = settings;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Labirinthan) app;
        characterNode = new Node("Character");
        characterNode.setLocalTranslation(Labirinthan.X, Labirinthan.Y, Labirinthan.Z);
        characterControl = new BetterCharacterControl(1f, 3.0f, 80f);
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
        this.app.getCamera().setRotation(new Quaternion(0,1,0,0));
        isDead = false;

        addCollisionListener();

        if (!Labirinthan.isKeyInitialized) {
            initKeys();
            Labirinthan.isKeyInitialized = true;
        }
    }

    /**
     * Game keys initialization
     */
    private void initKeys() {
        this.app.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        this.app.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        this.app.getInputManager().addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        this.app.getInputManager().addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        this.app.getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        this.app.getInputManager().addMapping("Interact", new KeyTrigger(KeyInput.KEY_E));
        this.app.getInputManager().addMapping("DropTorch", new KeyTrigger(KeyInput.KEY_Q));

        this.app.getInputManager().addListener(this, "Left", "Right", "Forward", "Backward", "Jump", "Interact", "DropTorch");
    }

    /**
     * Adding collision listener
     */
    private void addCollisionListener() {
        this.app.bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        boolean interactWithTorch = (event.getObjectA() instanceof TorchInteractionArea || event.getObjectB() instanceof TorchInteractionArea) && !isCarryingTorch;
        boolean interactWithPuzzle = event.getObjectA() instanceof PuzzleInteractionArea || event.getObjectB() instanceof PuzzleInteractionArea;
        boolean interactWithTrap = event.getObjectA() instanceof TrapInteractionArea || event.getObjectB() instanceof TrapInteractionArea;
        if (interactWithTorch) {
            if (event.getObjectA() instanceof TorchInteractionArea) {
                interactionObject = event.getObjectA();
            } else interactionObject = event.getObjectB();
            interactType = InteractionType.TORCH;
            setInteractionText(true, MainHUD.TORCH_INTERACTION_TEXT);
        } else if (interactWithPuzzle) {
            if (event.getObjectA() instanceof PuzzleInteractionArea) {
                interactionObject = event.getObjectA();
            } else interactionObject = event.getObjectB();
            interactType = InteractionType.PUZZLE;
            setInteractionText(true, MainHUD.PUZZLE_INTERACTION_TEXT);
        } else if (interactWithTrap) {
            if (event.getObjectA() instanceof TrapInteractionArea) {
                interactionObject = event.getObjectA();
            } else interactionObject = event.getObjectB();
            trapAction();
        } else {
            interactType = InteractionType.NONE;
            setInteractionText(false);
        }
    }

    /**
     * Starting different traps actions
     */
    private void trapAction() {
        TrapMaster trapMaster = ((TrapInteractionArea) interactionObject).getParent();
        switch (trapMaster.trapType) {
            case MINE -> trapMaster.startMineExplode();
            case SPIKE -> trapMaster.startDropAnimation();
        }
    }

    @Override
    public void onAction(String binding, boolean isPressed, float tpf) {
        if (!isPuzzleFound && !isDead) {
            switch (binding) {
                case "Left":
                    this.left = isPressed;
                    break;
                case "Right":
                    this.right = isPressed;
                    break;
                case "Forward":
                    this.forward = isPressed;
                    break;
                case "Backward":
                    this.backward = isPressed;
                    break;
                case "Jump":
                    if (isPressed && this.characterControl.isOnGround())
                        this.characterControl.jump();
                    break;
                case "Interact":
                    if (isPressed && (this.interactType != InteractionType.NONE))
                        interact();
                    break;
                case "DropTorch":
                    if (isPressed && this.isCarryingTorch)
                        dequipTorch();
                    break;
            }
        }
    }

    @Override
    public void update(float tpf) {
        Vector3f camDir = this.app.getCamera().getDirection().clone().setY(0).normalizeLocal().multLocal(CHARACTER_SPEED);
        Vector3f camLeft = this.app.getCamera().getLeft().clone().setY(0).normalizeLocal().multLocal(CHARACTER_SPEED);
        walkDirection.set(0, 0, 0);
        if(this.left||this.right||this.forward||this.backward){
            hud.showInstructionSign(false);
        }
        if (this.left)
            walkDirection.addLocal(camLeft);
        if (this.right){
            walkDirection.addLocal(camLeft.negate());
        }
        if (this.forward)
            walkDirection.addLocal(camDir);
        if (this.backward)
            walkDirection.addLocal(camDir.negate());
        characterControl.setWalkDirection(walkDirection);

        if (!Labirinthan.isFlying && !isDead) {
            this.app.getCamera().setLocation(characterNode.getLocalTranslation().add(0, 1.8f, 0));
            cameraNode.setLocalTranslation(this.app.getCamera().getLocation());
        }

        if (!isDead) {
            float[] angles = this.app.getCamera().getRotation().toAngles(null);
            float pitch = FastMath.clamp(angles[0], -FastMath.PI / 4, FastMath.PI / 4);
            Quaternion clampedRotation = new Quaternion().fromAngles(pitch, angles[1], angles[2]);
            this.app.getCamera().setRotation(clampedRotation);
            cameraNode.setLocalRotation(clampedRotation);

            if ((torchNode != null) && (light != null)) {
                Vector3f torchPosition = this.app.getCamera().getLocation()
                        .add(this.app.getCamera().getDirection().mult(0.5f))
                        .add(camLeft.mult(-0.02f))
                        .add(0, -0.5f, 0);
                torchNode.setLocalTranslation(torchPosition);

                Quaternion horizontalRotation = new Quaternion().fromAngleAxis(clampedRotation.toAngles(null)[1], Vector3f.UNIT_Y);
                torchNode.setLocalRotation(horizontalRotation.mult(new Quaternion().fromAngleAxis(FastMath.PI / 2, Vector3f.UNIT_Y)));
                light.setPosition(lightNode.getWorldTranslation());
            }

            if (isCarryingTorch) {
                torchTimer -= tpf;
                if (torchTimer <= 0) {
                    dequipTorch();
                } else {
                    hud.updateTorchPercent(torchTimer / TORCH_DURATION);
                }
            }
        }

        Labirinthan.level.playCreepySound(characterNode);
    }

    /**
     * HP control
     * @param amount - hp amount
     */
    public void hpActions(float amount) {
        health += amount;
        if (health < 0) {
            Labirinthan.level.playDeathSound();
            health = 0;
            death();
        }
        if (health > 1.0f) {
            health = 1.0f;
        }
        hud.updateHealthPercent(health);
    }

    /**
     * HP change at the start of level
     */
    public void newLevelHPActions() {
        hpActions(health / 2f);
    }

    /**
     * Death actions
     */
    private void death() {
        app.getFlyByCamera().setEnabled(false);
        dequipTorch();
        isDead = true;

        CameraControl cameraControl = new CameraControl(app, this, this.app.getCamera(), characterNode.getLocalTranslation().add(0, 0.2f, 0), new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X).fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_Z), 15.0f, hud);
        cameraNode.addControl(cameraControl);

        app.getInputManager().setCursorVisible(false);
    }

    /**
     * Setting interaction text
     * @param status - if interaction text needed
     * @param text - text
     */
    private void setInteractionText(boolean status, String... text) {
        hud.showInteractionSign(status, text.length > 0 ? text[0] : "");
    }

    /**
     * Character interaction with objects
     */
    private void interact() {
        switch (interactType) {
            case TORCH:
                torchHolder = ((TorchInteractionArea) interactionObject).getParent();
                torchHolder.updateTorchStatus(false);
                isCarryingTorch = true;
                carryTorch();
                break;
            case PUZZLE:
                isPuzzleFound = true;
                startPuzzle(((PuzzleInteractionArea) interactionObject).getParent());
                break;
            case BOOK:
                System.out.println("Interacted with book");
                break;
            default:
                System.out.println("No interaction available");
                break;
        }
    }

    /**
     * Starting a puzzle
     * @param puzzleCab - puzzle cabinet
     */
    private void startPuzzle(PuzzleCabinet puzzleCab) {
        puzzleCab.removeInteractionZone();
        this.app.getRootNode().detachChild(characterNode);
        app.levelPreparation(true);
        switch (puzzleCab.puzzleType) {
            case SUDOKU ->
                    new PuzzleSudoku(app, Labirinthan.level.localPuzzleNode, settings, app.getAssetManager()).createScreen();
            case PYRAMID ->
                    new PuzzlePyramid(app, Labirinthan.level.localPuzzleNode, settings, app.getAssetManager()).createScreen();
            case ENCRYPTION ->
                    new PuzzleSquareEncryption(app, Labirinthan.level.localPuzzleNode, settings, app.getAssetManager()).createScreen();
        }
    }

    /**
     * Carrying a torch
     */
    private void carryTorch() {
        if (torchNode == null) {
            torchNode = new Node("Torch Node");
            new Torch(app.getAssetManager(), torchNode);
            this.app.getRootNode().attachChild(torchNode);
            lightNode = new Node("Light Node");
            torchNode.attachChild(lightNode);
            lightNode.move(-0.5f, 1, 0);
            light = torchHolder.createLight(lightNode.getWorldTranslation(), app.getAssetManager());
            this.app.getRootNode().addLight(light);
            torchTimer = TORCH_DURATION;
            hud.showTorchProgress(true);
        }
    }

    /**
     * Loosing a torch
     */
    private void dequipTorch() {
        if (torchNode != null) {
            torchNode.removeFromParent();
            torchNode = null;
            lightNode = null;
            this.app.getRootNode().removeLight(light);
            torchHolder.cleanupShadows();
            light = null;
            isCarryingTorch = false;
            hud.updateTorchPercent(0);
            hud.showTorchProgress(false);
        }
    }
}
