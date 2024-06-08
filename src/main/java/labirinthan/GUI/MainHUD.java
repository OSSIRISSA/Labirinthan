package labirinthan.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.*;
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
    private final Node mainHUDNode = new Node("Main HUD");

    private static final String DEFAULT_INTERACTION_TEXT = "  Press 'E' to ";
    public static final String TORCH_INTERACTION_TEXT = "take a torch  ";
    public static final String PUZZLE_INTERACTION_TEXT = "solve a mystery  ";

    public MainHUD(Node guiNode, AppSettings settings, AssetManager assetManager) {
        this.guiNode = guiNode;
        this.settings = settings;
        this.assetManager = assetManager;

        GuiGlobals.getInstance().setCursorEventsEnabled(false);

        mainFont = this.assetManager.loadFont("Interface/demi.fnt");
    }

    public void createMainHUD() {
        guiNode.detachAllChildren();

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

        guiNode.attachChild(mainHUDNode);
    }

    public void updateHealthPercent(float healthPercent) {
        hpBar.setProgressPercent(healthPercent);
    }

    public void updateTorchPercent(float progress) {
        torchBar.setProgressPercent(progress);
    }

    public void showTorchProgress(boolean show) {
        if (show) {
            torchBar.setCullHint(Spatial.CullHint.Never);
        } else {
            torchBar.setCullHint(Spatial.CullHint.Always);
        }
    }

    public void showInteractionSign(boolean show, String text) {
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