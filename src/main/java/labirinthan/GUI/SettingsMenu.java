/**
 * Task: Game
 * File: SettingsMenu.java
 *
 *  @author Max Mormil
*/
package labirinthan.GUI;

import com.jme3.font.BitmapFont;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.SpringGridLayout;
import labirinthan.Labirinthan;

public class SettingsMenu {

    private final Labirinthan app;
    private final Node guiNode;
    private final AppSettings settings;
    private final MainMenu mainMenu;

    private final BitmapFont mainFont;
    private TextField widthField;
    private TextField heightField;
    private Checkbox fullscreenCheckbox;
    private Checkbox vsyncCheckbox;
    private Slider volumeSlider;
    private Slider musicSlider;
    private Slider soundSlider;

    /**
     * SettingsMenu constructor
     * @param mainApp - mainApp
     * @param guiNode - gui node
     * @param settings - settings
     * @param mainMenu - main menu
     * @param font - font
     */
    public SettingsMenu(Labirinthan mainApp, Node guiNode, AppSettings settings, MainMenu mainMenu, BitmapFont font){
        this.app = mainApp;
        this.guiNode = guiNode;
        this.settings = settings;
        this.mainMenu = mainMenu;
        this.mainFont = font;
        createSettingsScreen();
    }

    /**
     * Creating settings screen
     */
    public void createSettingsScreen() {
        guiNode.detachAllChildren();

        // Create a title label
        Label titleLabel = new Label("Settings");
        titleLabel.setFont(mainFont);
        titleLabel.setFontSize(114f);
        titleLabel.setLocalTranslation((settings.getWidth() - titleLabel.getPreferredSize().x) / 2f, settings.getHeight(), 0);
        guiNode.attachChild(titleLabel);

        // Back button
        Button backButton = new Button("Back");
        backButton.setFont(mainFont);
        backButton.setFontSize(48f);
        backButton.setTextHAlignment(HAlignment.Center);
        backButton.setPreferredSize(new Vector3f(titleLabel.getPreferredSize().x/2.5f, backButton.getPreferredSize().y, 0));
        backButton.setLocalTranslation((settings.getWidth() - backButton.getPreferredSize().x)/2f - backButton.getPreferredSize().x*2, backButton.getPreferredSize().y*1.5f, 0);
        backButton.addClickCommands(source -> returnToMainMenu());
        backButton.addClickCommands(source -> MainMenu.playClickSound());
        backButton.addCommands(Button.ButtonAction.HighlightOn, source -> MainMenu.hover.play());
        guiNode.attachChild(backButton);

        // Apply button
        Button applyButton = new Button("Apply");
        applyButton.setFont(mainFont);
        applyButton.setFontSize(48f);
        applyButton.setTextHAlignment(HAlignment.Center);
        applyButton.setPreferredSize(new Vector3f(backButton.getPreferredSize().x, applyButton.getPreferredSize().y, 0));
        applyButton.setLocalTranslation((settings.getWidth() - applyButton.getPreferredSize().x) / 2f + applyButton.getPreferredSize().x*2f, applyButton.getPreferredSize().y*1.5f, 0);
        applyButton.addClickCommands(source -> applySettings());
        applyButton.addClickCommands(source -> MainMenu.playClickSound());
        applyButton.addCommands(Button.ButtonAction.HighlightOn, source -> MainMenu.hover.play());
        guiNode.attachChild(applyButton);

        // Create a Tabs component
        Container tabsContainer = new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.Last, FillMode.First));
        tabsContainer.setPreferredSize(new Vector3f(settings.getWidth() / 1.2f, settings.getHeight() - titleLabel.getPreferredSize().y-backButton.getPreferredSize().y*2, 0));
        tabsContainer.setLocalTranslation((settings.getWidth() - tabsContainer.getPreferredSize().x) / 2f, (settings.getHeight() - titleLabel.getPreferredSize().y), 0);
        guiNode.attachChild(tabsContainer);

        TabbedPanel tabs = new TabbedPanel();
        tabsContainer.addChild(tabs);

        // Graphics Tab
        Container graphicsTab = new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.None, FillMode.Even));
        createGraphicsSettings(graphicsTab);
        tabs.addTab("Graphics", graphicsTab);

        // Audio Tab
        Container audioTab = new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.None, FillMode.Even));
        createAudioSettings(audioTab);
        tabs.addTab("Audio", audioTab);

        // Controls Tab
        Container controlsTab = new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.None, FillMode.Even));
        createControlsSettings(controlsTab);
        tabs.addTab("Controls", controlsTab);
    }

    /**
     * Creating graphics settings screen
     * @param graphicsTab - graphics tab
     */
    private void createGraphicsSettings(Container graphicsTab) {
        Label fullscreenLabel = new Label("Fullscreen");
        fullscreenLabel.setFontSize(48f);
        fullscreenLabel.setFont(mainFont);
        graphicsTab.addChild(fullscreenLabel,0,0);
        fullscreenCheckbox = graphicsTab.addChild(new Checkbox(""),0,1);
        fullscreenCheckbox.addClickCommands(source -> MainMenu.playClickSound());
        fullscreenCheckbox.setFont(mainFont);
        fullscreenCheckbox.setFontSize(48f);
        fullscreenCheckbox.setChecked(settings.isFullscreen());

        Label widthLabel = new Label("Resolution Width");
        widthLabel.setFontSize(48f);
        widthLabel.setFont(mainFont);
        graphicsTab.addChild(widthLabel,1,0);
        widthField = graphicsTab.addChild(new TextField(""),1,1);
        widthField.setFontSize(48f);
        widthField.setText(String.valueOf(settings.getWidth()));

        Label heightLabel = new Label("Resolution Height");
        heightLabel.setFontSize(48f);
        heightLabel.setFont(mainFont);
        graphicsTab.addChild(heightLabel,2,0);
        heightField = graphicsTab.addChild(new TextField(""),2,1);
        heightField.setFontSize(48f);
        heightField.setText(String.valueOf(settings.getHeight()));

        Label vsyncLabel = new Label("Vertical Sync");
        vsyncLabel.setFontSize(48f);
        vsyncLabel.setFont(mainFont);
        graphicsTab.addChild(vsyncLabel,3,0);
        vsyncCheckbox = graphicsTab.addChild(new Checkbox(""),3,1);
        vsyncCheckbox.addClickCommands(source -> MainMenu.playClickSound());
        vsyncCheckbox.setFont(mainFont);
        vsyncCheckbox.setFontSize(48f);
        vsyncCheckbox.setChecked(settings.isVSync());
    }

    /**
     * Creating audio settings screen
     * @param audioTab - audio tab
     */
    private void createAudioSettings(Container audioTab) {
        Label volumeLabel = new Label("Master Volume");
        volumeLabel.setFontSize(48f);
        volumeLabel.setFont(mainFont);
        audioTab.addChild(volumeLabel,0,0);
        volumeSlider = audioTab.addChild(new Slider(new DefaultRangedValueModel(0, 100, settings.getFloat("Master Volume")*100)),0,1);
        volumeSlider.setPreferredSize(new Vector3f(300, 48, 0));

        Label musicLabel = new Label("Music Volume");
        musicLabel.setFontSize(48f);
        musicLabel.setFont(mainFont);
        audioTab.addChild(musicLabel,1,0);
        musicSlider = audioTab.addChild(new Slider(new DefaultRangedValueModel(0, 100, settings.getFloat("Music Volume")*100)),1,1);
        musicSlider.setPreferredSize(new Vector3f(300, 48, 0));

        Label soundLabel = new Label("Sound Volume");
        soundLabel.setFontSize(48f);
        soundLabel.setFont(mainFont);
        audioTab.addChild(soundLabel,2,0);
        soundSlider = audioTab.addChild(new Slider(new DefaultRangedValueModel(0, 100, settings.getFloat("Sound Volume")*100)),2,1);
        soundSlider.setPreferredSize(new Vector3f(300, 48, 0));
    }

    /**
     * Creating controls settings screen
     * @param controlsTab - controls tab
     */
    private void createControlsSettings(Container controlsTab) {
        controlsTab.addChild(new Label("Control Settings Coming Soon")).setFontSize(48f);
    }

    /**
     * Returning to main menu
     */
    private void returnToMainMenu() {
        guiNode.detachAllChildren();
        mainMenu.createHomeScreen();
    }

    /**
     * Applying chosen settings
     */
    private void applySettings() {
        settings.setFullscreen(fullscreenCheckbox.isChecked());
        settings.setVSync(vsyncCheckbox.isChecked());
        try {
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());
            settings.setWidth(width);
            settings.setHeight(height);
        } catch (NumberFormatException ignored) {
        }
        settings.putFloat("Master Volume", (float) (volumeSlider.getModel().getValue()/volumeSlider.getModel().getMaximum()));
        settings.putFloat("Music Volume", (float) (musicSlider.getModel().getValue()/musicSlider.getModel().getMaximum()));
        settings.putFloat("Sound Volume", (float) (soundSlider.getModel().getValue()/soundSlider.getModel().getMaximum()));
        app.restart();
        createSettingsScreen();
    }
}
