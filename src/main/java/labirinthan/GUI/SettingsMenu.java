package labirinthan.GUI;

import com.jme3.asset.AssetManager;
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
    private final AssetManager assetManager;
    private final MainMenu mainMenu;

    private final BitmapFont mainFont;
    private TextField widthField;
    private TextField heightField;
    private Checkbox fullscreenCheckbox;

    public SettingsMenu(Labirinthan mainApp, Node guiNode, AppSettings settings, AssetManager assetManager, MainMenu mainMenu, BitmapFont font){
        this.app = mainApp;
        this.guiNode = guiNode;
        this.settings = settings;
        this.assetManager = assetManager;
        this.mainMenu = mainMenu;
        this.mainFont = font;

        createSettingsScreen();
    }

    public void createSettingsScreen() {
        guiNode.detachAllChildren(); // Clear previous elements

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
        backButton.addClickCommands(source -> MainMenu.click.play());
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
        applyButton.addClickCommands(source -> MainMenu.click.play());
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

    private void createGraphicsSettings(Container graphicsTab) {
        Label fullscreenLabel = new Label("Fullscreen");
        fullscreenLabel.setFontSize(48f);
        fullscreenLabel.setFont(mainFont);
        graphicsTab.addChild(fullscreenLabel,0,0);
        fullscreenCheckbox = graphicsTab.addChild(new Checkbox(""),0,1);
        fullscreenCheckbox.addClickCommands(source -> MainMenu.click.play());
        fullscreenCheckbox.setFont(mainFont);
        fullscreenCheckbox.setFontSize(48f);
        fullscreenCheckbox.setChecked(settings.isFullscreen());
        fullscreenCheckbox.addClickCommands(source -> toggleFullscreen(fullscreenCheckbox.isChecked()));

        Label widthLabel = new Label("Resolution Width");
        widthLabel.setFontSize(48f);
        widthLabel.setFont(mainFont);
        graphicsTab.addChild(widthLabel,1,0);
        widthField = graphicsTab.addChild(new TextField(""),1,1);
        //widthField.setFont(mainFont);
        widthField.setFontSize(48f);
        widthField.setText(String.valueOf(settings.getWidth()));

        Label heightLabel = new Label("Resolution Height");
        heightLabel.setFontSize(48f);
        heightLabel.setFont(mainFont);
        graphicsTab.addChild(heightLabel,2,0);
        heightField = graphicsTab.addChild(new TextField(""),2,1);
        //heightField.setFont(mainFont);
        heightField.setFontSize(48f);
        heightField.setText(String.valueOf(settings.getHeight()));

        Label vsyncLabel = new Label("Vertical Sync");
        vsyncLabel.setFontSize(48f);
        vsyncLabel.setFont(mainFont);
        graphicsTab.addChild(vsyncLabel,3,0);
        Checkbox vsyncCheckbox = graphicsTab.addChild(new Checkbox(""),3,1);
        vsyncCheckbox.addClickCommands(source -> MainMenu.click.play());
        vsyncCheckbox.setFont(mainFont);
        vsyncCheckbox.setFontSize(48f);
        vsyncCheckbox.setChecked(settings.isVSync());
    }

    private void createAudioSettings(Container audioTab) {
        Label volumeLabel = new Label("Master Volume");
        volumeLabel.setFontSize(48f);
        volumeLabel.setFont(mainFont);
        audioTab.addChild(volumeLabel,0,0);
        Slider volumeSlider = audioTab.addChild(new Slider(new DefaultRangedValueModel(0, 100, 50)),0,1);
        volumeSlider.setPreferredSize(new Vector3f(300, 48, 0));
        //volumeSlider.getModel().setValue(50); // Set initial value

        Label musicLabel = new Label("Music Volume");
        musicLabel.setFontSize(48f);
        musicLabel.setFont(mainFont);
        audioTab.addChild(musicLabel,1,0);
        Slider musicSlider = audioTab.addChild(new Slider(new DefaultRangedValueModel(0, 100, 50)),1,1);
        musicSlider.setPreferredSize(new Vector3f(300, 48, 0));
        //musicSlider.getModel().setValue(50);

        Label soundLabel = new Label("Sound Volume");
        soundLabel.setFontSize(48f);
        soundLabel.setFont(mainFont);
        audioTab.addChild(soundLabel,2,0);
        Slider soundSlider = audioTab.addChild(new Slider(new DefaultRangedValueModel(0, 100, 50)),2,1);
        soundSlider.setPreferredSize(new Vector3f(300, 48, 0));
        //soundSlider.getModel().setValue(50);
    }

    private void createControlsSettings(Container controlsTab) {
        // Add control settings here
        controlsTab.addChild(new Label("Control Settings Coming Soon")).setFontSize(48f);
    }

    private void toggleFullscreen(boolean fullscreen) {
        settings.setFullscreen(fullscreen);
        app.restart();
    }

    private void returnToMainMenu() {
        guiNode.detachAllChildren();
        mainMenu.createHomeScreen();
    }

    private void applySettings() {
        // TODO: Implement logic
        System.out.println("Settings are applied!");
    }
}