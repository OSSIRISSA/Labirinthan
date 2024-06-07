package labirinthan.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.*;
import com.simsilica.lemur.style.BaseStyles;
import labirinthan.Labirinthan;

public class MainHUD {
    private final Labirinthan app;
    private final Node guiNode;
    private final AppSettings settings;
    private final AssetManager assetManager;
    private final BitmapFont mainFont;
    private ProgressBar hpBar;
    private Label interactionSign;
    private final Node mainHUDNode = new Node("Main HUD");

    private static final String DEFAULT_INTERACTION_TEXT = "Press 'E' to ";
    public static final String TORCH_INTERACTION_TEXT = "take a torch";

    public MainHUD(Labirinthan mainApp, Node guiNode, AppSettings settings, AssetManager assetManager){
        this.app = mainApp;
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
        hpBar.setLocalTranslation(10, 500, 0);
        hpBar.setProgressPercent(1f);
        mainHUDNode.attachChild(hpBar);

        // Interaction Sign
        interactionSign = new Label(DEFAULT_INTERACTION_TEXT);
        interactionSign.setFontSize(48);
        interactionSign.setFont(mainFont);
        interactionSign.setColor(ColorRGBA.Red);
        interactionSign.setCullHint(Spatial.CullHint.Always); // Hide initially
        mainHUDNode.attachChild(interactionSign);

        // Position the interaction sign at the bottom middle
        interactionSign.setLocalTranslation(
                (settings.getWidth() / 2f) - (interactionSign.getPreferredSize().x / 2),
                interactionSign.getPreferredSize().y*4f,
                0
        );

        guiNode.attachChild(mainHUDNode);
    }

    public void updateHealth(float healthPercent) {
        hpBar.setProgressValue(healthPercent);
    }

    public void showInteractionSign(boolean show, String text) {
        if (show) {
            interactionSign.setCullHint(Spatial.CullHint.Never);
            interactionSign.setText(DEFAULT_INTERACTION_TEXT+text);
        } else {
            interactionSign.setCullHint(Spatial.CullHint.Always);
        }
        interactionSign.setLocalTranslation(
                (settings.getWidth() / 2f) - (interactionSign.getPreferredSize().x / 2),
                interactionSign.getPreferredSize().y*4f,
                0
        );
    }
}