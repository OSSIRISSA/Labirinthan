/**
 * Task: Game
 * File: FinalScreen.java
 *
 *  @author Iryna Hryshchenko
 */
package labirinthan.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;
import labirinthan.Labirinthan;

public class FinalScreen {

    private final Labirinthan app;
    private final Node guiNode;
    private final AppSettings settings;
    private final AssetManager assetManager;
    private final BitmapFont mainFont;
    public static AudioNode click;
    public static AudioNode hover;

    /**
     * Final screen constructor
     * @param mainApp - Labirinthan app
     * @param guiNode - gui node
     * @param settings - settings
     * @param assetManager - asset manager
     */
    public FinalScreen(Labirinthan mainApp, Node guiNode, AppSettings settings, AssetManager assetManager){
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

    /**
     * Creating final screen
     */
    public void createHomeScreen() {
        guiNode.detachAllChildren();

        // Create a title label
        Label titleLabel = new Label("May this be the enD?");
        titleLabel.setFont(mainFont);
        titleLabel.setFontSize(114f);
        titleLabel.setLocalTranslation((settings.getWidth()-titleLabel.getPreferredSize().x) / 2f, (settings.getHeight()-titleLabel.getPreferredSize().y) / 2f +2*titleLabel.getPreferredSize().y, 0);
        titleLabel.setColor(ColorRGBA.Red);
        guiNode.attachChild(titleLabel);

        Button playButton = new Button("Main menu");
        playButton.setFont(mainFont);
        playButton.setFontSize(48f);
        playButton.setTextHAlignment(HAlignment.Center);
        playButton.setPreferredSize(new Vector3f(titleLabel.getPreferredSize().x/1.5f, playButton.getPreferredSize().y, 0));
        playButton.setLocalTranslation((settings.getWidth()-playButton.getPreferredSize().x) / 2f, (settings.getHeight()-playButton.getPreferredSize().y) / 2f + playButton.getPreferredSize().y, 1);
        playButton.addClickCommands(source -> playClickSound());
        playButton.addCommands(Button.ButtonAction.HighlightOn, source -> hover.play());
        playButton.addClickCommands(source -> {
            guiNode.detachAllChildren();
            app.mainMenu.createHomeScreen();
        });
        guiNode.attachChild(playButton);

    }

    /**
     * Playing sound on click
     */
    static void playClickSound(){
        click.stop();
        click.play();
    }

    /**
     * Playing death sound
     */
    public void playDeathSound() {
        AudioNode deathSound = new AudioNode(assetManager, "Sounds/player-death.wav", AudioData.DataType.Buffer);
        deathSound.setPositional(false); // Use true for 3D sounds
        deathSound.setLooping(false); // Set to true if you want the sound to loop
        deathSound.setVolume(10*4*settings.getFloat("Master Volume")*settings.getFloat("Sound Volume")); // Set the volume (1 is default, 0 is silent)
        guiNode.attachChild(deathSound);
        deathSound.play();
    }
}