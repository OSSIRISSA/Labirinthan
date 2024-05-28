package labirinthan.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.*;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.event.MouseListener;
import labirinthan.Labirinthan;

public class SettingsMenu {

    private final Labirinthan app;
    private final Node guiNode;
    private final AppSettings settings;
    private final AssetManager assetManager;
    private final MainMenu mainMenu;

    private final BitmapFont mainFont;

    public SettingsMenu(Labirinthan mainApp, Node guiNode, AppSettings settings, AssetManager assetManager, MainMenu mainMenu, BitmapFont font){
        this.app = mainApp;
        this.guiNode = guiNode;
        this.settings = settings;
        this.assetManager = assetManager;
        this.mainMenu = mainMenu;
        this.mainFont = font;

        System.out.println("initialized");
    }

    public void createSettingsScreen() {
        // Create a title label
        Label titleLabel = new Label("Settings");
        titleLabel.setFont(mainFont);
        titleLabel.setFontSize(114f);
        titleLabel.setLocalTranslation((settings.getWidth()-titleLabel.getPreferredSize().x) / 2f, (settings.getHeight()-titleLabel.getPreferredSize().y) / 2f +2*titleLabel.getPreferredSize().y, 0);
        System.out.println(titleLabel);
        guiNode.attachChild(titleLabel);
        float yOffset = titleLabel.getLocalTranslation().y - titleLabel.getPreferredSize().y;

        // Create a "Toggle Fullscreen" checkbox
        Checkbox fullscreenCheckbox = new Checkbox("Fullscreen");
        fullscreenCheckbox.setFont(mainFont);
        fullscreenCheckbox.setFontSize(48f);
        fullscreenCheckbox.setChecked(settings.isFullscreen());
        fullscreenCheckbox.setLocalTranslation((settings.getWidth()-fullscreenCheckbox.getPreferredSize().x) / 2f, yOffset - fullscreenCheckbox.getPreferredSize().y, 1);
        fullscreenCheckbox.addClickCommands(source -> toggleFullscreen(fullscreenCheckbox.isChecked()));
        guiNode.attachChild(fullscreenCheckbox);
        yOffset = fullscreenCheckbox.getLocalTranslation().y - fullscreenCheckbox.getPreferredSize().y;

        // Create a "Volume" slider
        Label volumeLabel = new Label("Volume");
        volumeLabel.setFont(mainFont);
        volumeLabel.setFontSize(48f);
        volumeLabel.setLocalTranslation((settings.getWidth()-volumeLabel.getPreferredSize().x) / 2f, yOffset - volumeLabel.getPreferredSize().y, 1);
        guiNode.attachChild(volumeLabel);
        yOffset = volumeLabel.getLocalTranslation().y - volumeLabel.getPreferredSize().y;

        Slider volumeSlider = new Slider();
        volumeSlider.setPreferredSize(new Vector3f(300, 48, 0));
        volumeSlider.setLocalTranslation((settings.getWidth()-volumeSlider.getPreferredSize().x) / 2f, yOffset - volumeSlider.getPreferredSize().y, 1);
        volumeSlider.getModel().setValue(50); // Set initial value
        guiNode.attachChild(volumeSlider);

        // Add mouse listener to the slider
        MouseEventControl.addListenersToSpatial(volumeSlider, new MouseListener() {
            @Override
            public void mouseButtonEvent(MouseButtonEvent event, Spatial target, Spatial capture) {
                if (!event.isPressed()) { // Check if the mouse button is released
                    adjustVolume((float)volumeSlider.getModel().getValue());
                }
            }

            @Override
            public void mouseEntered(MouseMotionEvent event, Spatial target, Spatial capture) {}

            @Override
            public void mouseExited(MouseMotionEvent event, Spatial target, Spatial capture) {}

            @Override
            public void mouseMoved(MouseMotionEvent event, Spatial target, Spatial capture) {}
        });

        yOffset = volumeSlider.getLocalTranslation().y - volumeSlider.getPreferredSize().y;

        // Create a "Back" button
        Button backButton = new Button("Back");
        backButton.setFont(mainFont);
        backButton.setFontSize(48f);
        backButton.setTextHAlignment(HAlignment.Center);
        backButton.setPreferredSize(new Vector3f(titleLabel.getPreferredSize().x/1.5f, backButton.getPreferredSize().y, 0));
        backButton.setLocalTranslation((settings.getWidth()-backButton.getPreferredSize().x) / 2f, yOffset - backButton.getPreferredSize().y, 1);
        backButton.addClickCommands(source -> returnToMainMenu());
        guiNode.attachChild(backButton);
    }

    private void returnToMainMenu() {
        guiNode.detachAllChildren();
        mainMenu.createHomeScreen();
    }

    private void toggleFullscreen(boolean fullscreen) {
        settings.setFullscreen(fullscreen);
        app.restart();
    }

    private void adjustVolume(float volume) {
        // TODO: Implement volume adjustment
        System.out.println("Volume set to: " + volume);
    }
}
