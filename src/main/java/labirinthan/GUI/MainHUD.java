/**
 * Task: Game
 * File: MainHUD.java
 *
 *  @author Max Mormil
 */
package labirinthan.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.component.QuadBackgroundComponent;

public class MainHUD {
    private final Node guiNode;
    private final AppSettings settings;
    private final AssetManager assetManager;
    private final BitmapFont mainFont;
    private ProgressBar hpBar;
    private ProgressBar torchBar;
    private Label interactionSign;
    private Panel interactionPanel;
    private Geometry topLid;
    private Geometry bottomLid;
    private Label deathText;
    private final Node mainHUDNode = new Node("Main HUD");
    private boolean instructionsShowing = false;

    private static final String DEFAULT_INTERACTION_TEXT = "  Press 'E' to ";
    public static final String TORCH_INTERACTION_TEXT = "take a torch  ";
    public static final String PUZZLE_INTERACTION_TEXT = "solve a mystery  ";
    public static final String INSTRUCTION_TEXT = "\n\n\n  Your fate is endless puzzle solving  \n\n  Find puzzle books in my labyrinth  \n  Use 'WASD' to move  \n  Press 'SPACE' to jump  \n  Press 'E' to interact with objects  \n\n  Good Luck, Cursed, try not to die too soon...  \n\n\n";

    /**
     * Main HUD constructor
     * @param guiNode - gui node
     * @param settings - settings
     * @param assetManager - asset manager
     */
    public MainHUD(Node guiNode, AppSettings settings, AssetManager assetManager) {
        this.guiNode = guiNode;
        this.settings = settings;
        this.assetManager = assetManager;

        GuiGlobals.getInstance().setCursorEventsEnabled(false);

        mainFont = this.assetManager.loadFont("Interface/demi.fnt");
    }

    /**
     * Creating main HUD
     */
    public void createMainHUD() {
        //guiNode.detachAllChildren();

        // HP Bar
        hpBar = new ProgressBar();
        hpBar.setPreferredSize(new Vector3f(300, 40, 0));
        hpBar.setLocalTranslation(10, hpBar.getPreferredSize().y + 10, 0);
        hpBar.setProgressPercent(1f);
        hpBar.setBackground(new QuadBackgroundComponent(ColorRGBA.DarkGray));
        mainHUDNode.attachChild(hpBar);

        // Torch Bar
        torchBar = new ProgressBar();
        torchBar.setPreferredSize(new Vector3f(300, 40, 0));
        torchBar.rotate(0, 0, FastMath.PI);
        torchBar.setLocalTranslation(settings.getWidth() - 10, 10, 0);
        torchBar.setProgressPercent(1f);
        torchBar.setCullHint(Spatial.CullHint.Always);
        torchBar.setBackground(new QuadBackgroundComponent(ColorRGBA.DarkGray));
        torchBar.getValueIndicator().setBackground(new QuadBackgroundComponent(ColorRGBA.Orange));
        mainHUDNode.attachChild(torchBar);

        // Interaction Panel
        interactionPanel = new Panel();
        interactionPanel.setBackground(new QuadBackgroundComponent(assetManager.loadTexture("Textures/scroll.png")));
        interactionPanel.setCullHint(Spatial.CullHint.Always);
        mainHUDNode.attachChild(interactionPanel);

        // Interaction Sign
        interactionSign = new Label("");
        interactionSign.setFontSize(48);
        interactionSign.setFont(mainFont);
        interactionSign.setColor(ColorRGBA.Red);
        interactionSign.setCullHint(Spatial.CullHint.Always);
        interactionPanel.attachChild(interactionSign);


        // Eyelid overlays
        topLid = createLidOverlay();
        bottomLid = createLidOverlay();
        bottomLid.rotate(0, 0, FastMath.PI);
        topLid.setLocalTranslation(0, settings.getHeight(), 3);
        bottomLid.setLocalTranslation(settings.getWidth(), 0, 3);
        mainHUDNode.attachChild(topLid);
        mainHUDNode.attachChild(bottomLid);

        deathText = new Label("Letum non? omnia finiT");
        deathText.setFont(mainFont);
        deathText.setFontSize(72);
        deathText.setColor(ColorRGBA.Red);
        deathText.setAlpha(0);
        deathText.setLocalTranslation((settings.getWidth() - deathText.getPreferredSize().x) / 2f, (settings.getHeight() + deathText.getPreferredSize().y) / 2f, 4);
        mainHUDNode.attachChild(deathText);

        guiNode.attachChild(mainHUDNode);
    }

    /**
     * Creating LidOverlay
     * @return - LidOverlay
     */
    private Geometry createLidOverlay() {
        Quad quad = new Quad(settings.getWidth(), settings.getHeight());
        Geometry geom = new Geometry("LidOverlay", quad);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetManager.loadTexture("Textures/lid.png");
        mat.setTexture("ColorMap", tex);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        geom.setMaterial(mat);
        return geom;
    }

    /**
     * Updating LidOverlay
     * @param progress - LidOverlay progress
     */
    public void updateLidOverlay(float progress) {
        float lidPosition = settings.getHeight() * (1 - 2.5f * progress);
        topLid.setLocalTranslation(0, lidPosition, 3);
        bottomLid.setLocalTranslation(settings.getWidth(), settings.getHeight() - lidPosition, 3);
        deathText.setAlpha(1.25f * progress);
    }

    /**
     * Detaching everything except lids
     */
    public void detachAllExceptLids() {
        guiNode.detachChild(interactionPanel);
        guiNode.detachChild(torchBar);
        guiNode.detachChild(hpBar);
    }

    /**
     * Update health percentage
     * @param healthPercent - new health percentage
     */
    public void updateHealthPercent(float healthPercent) {
        hpBar.setProgressPercent(healthPercent);
    }

    /**
     * Update torch percentage
     * @param progress - torch lighting progress
     */
    public void updateTorchPercent(float progress) {
        torchBar.setProgressPercent(progress);
    }

    /**
     * Showing torch progress
     * @param show - if showing is needed
     */
    public void showTorchProgress(boolean show) {
        if (show) {
            torchBar.setCullHint(Spatial.CullHint.Never);
        } else {
            torchBar.setCullHint(Spatial.CullHint.Always);
        }
    }

    /**
     * Show interaction sign
     * @param show - if showing is needed
     * @param text - text
     */
    public void showInteractionSign(boolean show, String text) {
        if(!instructionsShowing) {
            if (show) {
                interactionSign.setCullHint(Spatial.CullHint.Never);
                interactionPanel.setCullHint(Spatial.CullHint.Never);
                interactionSign.setText(DEFAULT_INTERACTION_TEXT + text);
            } else {
                interactionSign.setCullHint(Spatial.CullHint.Always);
                interactionPanel.setCullHint(Spatial.CullHint.Always);
            }
            // Update the size of the background panel to match the text
            interactionPanel.setPreferredSize(new Vector3f(interactionSign.getPreferredSize().x + 20, interactionSign.getPreferredSize().y + 20, 0));
            interactionSign.setLocalTranslation(
                    (interactionPanel.getPreferredSize().x / 2f) - (interactionSign.getPreferredSize().x / 2),
                    (interactionPanel.getPreferredSize().y / 2f) - (interactionSign.getPreferredSize().y / 2) - 20,
                    1
            );
            interactionPanel.setLocalTranslation(
                    (settings.getWidth() / 2f) - (interactionPanel.getPreferredSize().x / 2),
                    interactionPanel.getPreferredSize().y * 2f,
                    0
            );
        }
    }

    /**
     * Show instructions
     * @param show - if showing is needed
     */
    public void showInstructionSign(boolean show) {
        if (show) {
            instructionsShowing = true;
            interactionSign.setCullHint(Spatial.CullHint.Never);
            interactionPanel.setCullHint(Spatial.CullHint.Never);
            interactionSign.setText(INSTRUCTION_TEXT);
        } else {
            instructionsShowing = false;
            interactionSign.setCullHint(Spatial.CullHint.Always);
            interactionPanel.setCullHint(Spatial.CullHint.Always);
        }
        // Update the size of the background panel to match the text
        interactionPanel.setPreferredSize(new Vector3f(interactionSign.getPreferredSize().x + 20, interactionSign.getPreferredSize().y + 20, 0));
        interactionSign.setLocalTranslation(
                (interactionPanel.getPreferredSize().x / 2f) - (interactionSign.getPreferredSize().x / 2),
                (interactionPanel.getPreferredSize().y / 2f) - (interactionSign.getPreferredSize().y / 2) - 20,
                1
        );
        interactionPanel.setLocalTranslation(
                (settings.getWidth() / 2f) - (interactionPanel.getPreferredSize().x / 2),
                interactionPanel.getPreferredSize().y * 1.1f,
                0
        );
    }
}
