package labirinthan.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.font.BitmapFont;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
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
    public static AudioNode click;
    public static AudioNode hover;

    public MainMenu(Labirinthan mainApp, Node guiNode, AppSettings settings, AssetManager assetManager){
        this.app = mainApp;
        this.guiNode = guiNode;
        this.settings = settings;
        this.assetManager = assetManager;

        GuiGlobals.initialize(mainApp);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        this.settings.putFloat("Master Volume", 0.5f);
        this.settings.putFloat("Music Volume", 0.5f);
        this.settings.putFloat("Sound Volume", 0.5f);

        mainFont = this.assetManager.loadFont("Interface/demi.fnt");

        click = new AudioNode(assetManager, "Sounds/button-click.wav", AudioData.DataType.Buffer);
        click.setPositional(false);
        click.setLooping(false);
        click.setVolume(8*settings.getFloat("Master Volume")*settings.getFloat("Sound Volume"));
        guiNode.attachChild(click);

        hover = new AudioNode(assetManager, "Sounds/button-hover.wav", AudioData.DataType.Buffer);
        hover.setPositional(false);
        hover.setLooping(false);
        hover.setVolume(8*settings.getFloat("Master Volume")*settings.getFloat("Sound Volume"));
        guiNode.attachChild(hover);
    }

    public void createHomeScreen() {
        guiNode.detachAllChildren();

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
        playButton.addClickCommands(source -> playClickSound());
        playButton.addCommands(Button.ButtonAction.HighlightOn, source -> hover.play());
        playButton.addClickCommands(source -> {
            guiNode.detachAllChildren();
            app.startGame();
        });
        guiNode.attachChild(playButton);
        float yOffset = playButton.getLocalTranslation().y - playButton.getPreferredSize().y/2f;

        // Create a "Settings" button
        Button settingsButton = createDefaultButton(new Button("Settings"), yOffset, titleLabel);
        settingsButton.addClickCommands(source -> openSettingsMenu());
        settingsButton.addClickCommands(source -> playClickSound());
        settingsButton.addCommands(Button.ButtonAction.HighlightOn, source -> hover.play());
        guiNode.attachChild(settingsButton);
        yOffset = settingsButton.getLocalTranslation().y - settingsButton.getPreferredSize().y/2f;

        // Create a "Credits" button
        Button creditsButton = createDefaultButton(new Button("Credits"), yOffset, titleLabel);
        creditsButton.addClickCommands(source -> new CreditsPage(guiNode, settings, this, mainFont));
        creditsButton.addClickCommands(source -> playClickSound());
        creditsButton.addCommands(Button.ButtonAction.HighlightOn, source -> hover.play());
        guiNode.attachChild(creditsButton);
        yOffset = creditsButton.getLocalTranslation().y - creditsButton.getPreferredSize().y/2f;

        // Create a "Quit" button
        Button quitButton = createDefaultButton(new Button("Quit"), yOffset, titleLabel);
        quitButton.addClickCommands(source -> playClickSound());
        quitButton.addClickCommands(source -> app.stop());
        quitButton.addCommands(Button.ButtonAction.HighlightOn, source -> hover.play());
        guiNode.attachChild(quitButton);
    }

    static void playClickSound(){
        click.stop();
        click.play();
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

    public static class LoadingScreen {
        private static Label loadingLabel;
        private static Geometry background;

        public static void show(Node guiNode, AssetManager assetManager, AppSettings settings) {
            // Create black background quad if not already created
            if (background == null) {
                Quad quad = new Quad(settings.getWidth(), settings.getHeight());
                background = new Geometry("Background", quad);
                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                mat.setColor("Color", ColorRGBA.Black);
                background.setMaterial(mat);
                background.setLocalTranslation(0, 0, 2); // Ensure it is rendered behind the loading label
            }
            guiNode.attachChild(background);

            // Create loading label if not already created
            if (loadingLabel == null) {
                BitmapFont font = assetManager.loadFont("Interface/demi.fnt");
                loadingLabel = new Label("Loading...");
                loadingLabel.setFont(font);
                loadingLabel.setFontSize(72f);
                loadingLabel.setLocalTranslation(
                        (settings.getWidth() - loadingLabel.getPreferredSize().x) / 2f,
                        (settings.getHeight() + loadingLabel.getPreferredSize().y) / 2f,
                        3); // Ensure it is rendered in front of the background
            }
            guiNode.attachChild(loadingLabel);
        }

        public static void hide(Node guiNode) {
            if (loadingLabel != null) {
                guiNode.detachChild(loadingLabel);
                loadingLabel = null;
            }
            if (background != null) {
                guiNode.detachChild(background);
                background = null;
            }
        }
    }
}