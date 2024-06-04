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
        hpBar.setPreferredSize(new Vector3f(200, 30, 0));  // Make the bar smaller
        hpBar.setProgressPercent(1f);  // Full health
        mainHUDNode.attachChild(hpBar);

        // Position the HP bar in the bottom left corner
        hpBar.setLocalTranslation(10, 500, 0);

        // Interaction Sign
        interactionSign = new Label("Press 'E' to interact");
        interactionSign.setFontSize(48);
        interactionSign.setFont(mainFont);
        interactionSign.setColor(ColorRGBA.Red);
        interactionSign.setCullHint(Spatial.CullHint.Always); // Hide initially
        mainHUDNode.attachChild(interactionSign);

        // Position the interaction sign at the bottom middle
        interactionSign.setLocalTranslation(
                (settings.getWidth() / 2f) - (interactionSign.getPreferredSize().x / 2),
                interactionSign.getPreferredSize().y*4f, // Adjust as needed
                0
        );

        guiNode.attachChild(mainHUDNode);

        System.out.println(guiNode.getChildren());
        System.out.println(mainHUDNode.getChildren());
    }

    public void updateHealth(float healthPercent) {
        hpBar.setProgressValue(healthPercent);
    }

    public void showInteractionSign(boolean show) {
        if (show) {
            interactionSign.setCullHint(Spatial.CullHint.Never);
        } else {
            interactionSign.setCullHint(Spatial.CullHint.Always);
        }
    }
}