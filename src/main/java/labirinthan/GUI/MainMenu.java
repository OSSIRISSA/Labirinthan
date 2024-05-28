package labirinthan.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;
import labirinthan.Labirinthan;


public class MainMenu {

    private final Labirinthan app;
    private final Node guiNode;
    private final AppSettings settings;
    private final AssetManager assetManager;

    private final BitmapFont mainFont;

    public MainMenu(Labirinthan mainApp, Node guiNode, AppSettings settings, AssetManager assetManager){
        this.app = mainApp;
        this.guiNode = guiNode;
        this.settings = settings;
        this.assetManager = assetManager;

        GuiGlobals.initialize(mainApp);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        mainFont = this.assetManager.loadFont("Interface/demi.fnt");
    }

    public void createHomeScreen() {
        // Create a title label
        Label titleLabel = new Label("Labirinthan");
        titleLabel.setFont(mainFont);
        titleLabel.setFontSize(114f);
        titleLabel.setLocalTranslation((settings.getWidth()-titleLabel.getPreferredSize().x) / 2f, (settings.getHeight()-titleLabel.getPreferredSize().y) / 2f +2*titleLabel.getPreferredSize().y, 0);
        guiNode.attachChild(titleLabel);

        // Create a "Play" button
        Button playButton = new Button("Play");
        playButton.setFont(mainFont);
        playButton.setFontSize(48f);
        playButton.setTextHAlignment(HAlignment.Center);
        playButton.setPreferredSize(new Vector3f(titleLabel.getPreferredSize().x/1.5f, playButton.getPreferredSize().y, 0));
        playButton.setLocalTranslation((settings.getWidth()-playButton.getPreferredSize().x) / 2f, (settings.getHeight()-playButton.getPreferredSize().y) / 2f + playButton.getPreferredSize().y, 1);
        playButton.addClickCommands(source -> app.startGame());
        guiNode.attachChild(playButton);
        float yOffset = playButton.getLocalTranslation().y - playButton.getPreferredSize().y/2f;

        // Create a "Settings" button
        Button settingsButton = createDefaultButton(new Button("Settings"), yOffset, titleLabel);
        settingsButton.addClickCommands(source -> openSettingsMenu());
        guiNode.attachChild(settingsButton);
        yOffset = settingsButton.getLocalTranslation().y - settingsButton.getPreferredSize().y/2f;

        // Create a "Credits" button
        Button creditsButton = createDefaultButton(new Button("Credits"), yOffset, titleLabel);
        creditsButton.addClickCommands(source -> System.out.println("Credits"));
        guiNode.attachChild(creditsButton);
        yOffset = creditsButton.getLocalTranslation().y - creditsButton.getPreferredSize().y/2f;

        // Create a "Quit" button
        Button quitButton = createDefaultButton(new Button("Quit"), yOffset, titleLabel);
        quitButton.addClickCommands(source -> app.stop());
        guiNode.attachChild(quitButton);
    }

    private void openSettingsMenu() {
        guiNode.detachAllChildren();
        new SettingsMenu(app, guiNode, settings, assetManager, this, mainFont);
    }

    private Button createDefaultButton(Button button, float yOffset, Label lableToMatch) {
        button.setFont(mainFont);
        button.setFontSize(48f);
        button.setTextHAlignment(HAlignment.Center);
        button.setPreferredSize(new Vector3f(lableToMatch.getPreferredSize().x/1.5f, button.getPreferredSize().y, 0));
        button.setLocalTranslation((settings.getWidth()-button.getPreferredSize().x) / 2f, yOffset - button.getPreferredSize().y, 1);
        return button;
    }
}