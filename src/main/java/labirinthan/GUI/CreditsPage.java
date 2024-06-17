/**
 * Task: Game
 * File: CreditsPage.java
 *
 *  @author Max Mormil
 */
package labirinthan.GUI;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;

public class CreditsPage {
    private final Node guiNode;
    private final Node creditsNode;
    private final AppSettings settings;
    private final MainMenu mainMenu;
    private final BitmapFont mainFont;

    /**
     * Credits page construction
     * @param guiNode - gui node
     * @param settings - settings
     * @param mainMenu - main menu
     * @param mainFont - main font
     */
    public CreditsPage(Node guiNode, AppSettings settings, MainMenu mainMenu, BitmapFont mainFont){
        this.guiNode = guiNode;
        this.settings = settings;
        this.mainMenu = mainMenu;
        this.mainFont = mainFont;
        creditsNode = new Node("Credits");

        createCreditsPage();
    }

    /**
     * Creating credits page screen
     */
    private void createCreditsPage() {
        guiNode.detachAllChildren();

        // Create a "Created by" label
        Label createdByLabel = new Label("Created by");
        createdByLabel.setFont(mainFont);
        createdByLabel.setFontSize(72f);
        createdByLabel.setLocalTranslation(settings.getWidth()/2f - createdByLabel.getPreferredSize().x/2, settings.getHeight()/2f+createdByLabel.getPreferredSize().y*2, 0f);
        creditsNode.attachChild(createdByLabel);

        // Create first label
        Label firstLabel = new Label("Ira Hryshchenko");
        firstLabel.setFont(mainFont);
        firstLabel.setFontSize(60f);
        firstLabel.setColor(new ColorRGBA(153/255f, 102/255f, 255/255f, 1));
        firstLabel.setLocalTranslation(settings.getWidth()/2f - firstLabel.getPreferredSize().x/2, settings.getHeight()/2f-firstLabel.getPreferredSize().y*0.2f, 0f);
        creditsNode.attachChild(firstLabel);

        // Create second label
        Label secondLabel = new Label("Max Mormil");
        secondLabel.setFont(mainFont);
        secondLabel.setFontSize(60f);
        secondLabel.setColor(ColorRGBA.Red.mult(0.5f));
        secondLabel.setLocalTranslation(settings.getWidth()/2f - secondLabel.getPreferredSize().x/2, settings.getHeight()/2f-secondLabel.getPreferredSize().y*2.2f, 0f);
        creditsNode.attachChild(secondLabel);

        // Create a "Back" button
        Button backButton = new Button("Return");
        backButton.setFont(mainFont);
        backButton.setFontSize(48f);
        backButton.setTextHAlignment(HAlignment.Center);
        backButton.setPreferredSize(new Vector3f(createdByLabel.getPreferredSize().x/2.5f, backButton.getPreferredSize().y, 0));
        backButton.setLocalTranslation((settings.getWidth() - backButton.getPreferredSize().x)/2f, backButton.getPreferredSize().y*1.5f, 0);
        backButton.addClickCommands(source -> mainMenu.createHomeScreen());
        backButton.addClickCommands(source -> MainMenu.playClickSound());
        backButton.addCommands(Button.ButtonAction.HighlightOn, source -> MainMenu.hover.play());
        creditsNode.attachChild(backButton);

        guiNode.attachChild(creditsNode);
    }
}
