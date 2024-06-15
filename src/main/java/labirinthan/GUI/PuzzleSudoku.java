/**
 * Task: Game
 * File: PuzzleSudoku.java
 *
 *  @author Iryna Hryshchenko
 */
package labirinthan.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.style.BaseStyles;
import labirinthan.Labirinthan;

import java.util.ArrayList;


public class PuzzleSudoku implements ActionListener {

    private final Labirinthan app;
    private final Node guiNode;
    private final AppSettings settings;
    private final AssetManager assetManager;
    public ArrayList<Button> sudokuSquares = new ArrayList<>();
    public ArrayList<Button> codeSquares = new ArrayList<>();
    public Button chosenSquare;

    /**
     * PuzzleSudoku constructor
     * @param application - app
     * @param guiNode - gui node
     * @param settings - settings
     * @param assetManager - asset manager
     */
    public PuzzleSudoku(Labirinthan application, Node guiNode, AppSettings settings, AssetManager assetManager) {
        this.app = application;
        this.guiNode = guiNode;
        this.settings = settings;
        this.assetManager = assetManager;

        GuiGlobals.initialize(application);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
    }

    /**
     * Creating puzzle screen
     */
    public void createScreen() {

        float startPosX = settings.getWidth() / 2f;
        float startPosY = settings.getHeight() * 0.75f;

        Button square = new Button(" ");


        Panel line = new Panel(5,190*3);
        line.setLocalTranslation(startPosX+190, startPosY, 1);
        line.setBackground(new QuadBackgroundComponent(ColorRGBA.fromRGBA255(186, 0, 7, 255)));
        guiNode.attachChild(line);
        line = new Panel(5,190*3);
        line.setLocalTranslation(startPosX+190*2+4, startPosY, 1);
        line.setBackground(new QuadBackgroundComponent(ColorRGBA.fromRGBA255(186, 0, 7, 255)));
        guiNode.attachChild(line);
        line = new Panel(190*3,5);
        line.setLocalTranslation(startPosX, startPosY-190, 1);
        line.setBackground(new QuadBackgroundComponent(ColorRGBA.fromRGBA255(186, 0, 7, 255)));
        guiNode.attachChild(line);
        line = new Panel(190*3,5);
        line.setLocalTranslation(startPosX, startPosY-190*2-4, 1);
        line.setBackground(new QuadBackgroundComponent(ColorRGBA.fromRGBA255(186, 0, 7, 255)));
        guiNode.attachChild(line);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (((i == 0) && (j == 0)) || ((i == 4) && (j == 3)) || ((i == 5) && (j == 8))) {
                    square = new Button("5");
                    square.setColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setHighlightColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setFocusColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                } else if (((i == 0) && (j == 1)) || ((i == 1) && (j == 3)) || ((i == 2) && (j == 7)) || ((i == 6) && (j == 0))) {
                    square = new Button("2");
                    square.setColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setHighlightColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setFocusColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                } else if (((i == 0) && (j == 5)) || ((i == 3) && (j == 0)) || ((i == 5) && (j == 4)) || ((i == 6) && (j == 2)) || ((i == 8) && (j == 3))) {
                    square = new Button("7");
                    square.setColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setHighlightColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setFocusColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                } else if (((i == 0) && (j == 6)) || ((i == 1) && (j == 5)) || ((i == 4) && (j == 1)) || ((i == 7) && (j == 0)) || ((i == 8) && (j == 8))) {
                    square = new Button("9");
                    square.setColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setHighlightColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setFocusColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                } else if (((i == 0) && (j == 7)) || ((i == 1) && (j == 4)) || ((i == 7) && (j == 3)) || ((i == 8) && (j == 2))) {
                    square = new Button("6");
                    square.setColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setHighlightColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setFocusColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                } else if (((i == 0) && (j == 8)) || ((i == 2) && (j == 3)) || ((i == 6) && (j == 5)) || ((i == 8) && (j == 0))) {
                    square = new Button("1");
                    square.setColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setHighlightColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setFocusColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                } else if (((i == 1) && (j == 8)) || ((i == 4) && (j == 7)) || ((i == 7) && (j == 5)) || ((i == 8) && (j == 1))) {
                    square = new Button("3");
                    square.setColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setHighlightColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setFocusColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                } else if (((i == 2) && (j == 8)) || ((i == 4) && (j == 5)) || ((i == 7) && (j == 4)) || ((i == 8) && (j == 7))) {
                    square = new Button("8");
                    square.setColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setHighlightColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setFocusColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                }
                else if ((i == 3) && (j == 4)) {
                    square = new Button("4");
                    square.setColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setHighlightColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                    square.setFocusColor(ColorRGBA.fromRGBA255(186, 0, 7, 255));
                }
                else {
                    square = new Button(" ");
                    square.setColor(ColorRGBA.Black);
                    square.addClickCommands(this::choose);
                    square.setHighlightColor(ColorRGBA.DarkGray);
                    square.setFocusColor(ColorRGBA.DarkGray);
                    if((i == 4) && (j == 4)){
                        square.setShadowColor(ColorRGBA.Blue);
                    }
                    if((i == 1) && (j == 7)){
                        square.setShadowColor(ColorRGBA.Yellow);
                    }
                    if((i == 5) && (j == 5)){
                        square.setShadowColor(ColorRGBA.Green);
                    }
                    if((i == 5) && (j == 7)){
                        square.setShadowColor(ColorRGBA.Magenta);
                    }
                }
                sudokuSquares.add(square);
                square.setBackground(new QuadBackgroundComponent(ColorRGBA.fromRGBA255(126, 117, 108, 255)));
                square.setFontSize(48f);
                square.setTextHAlignment(HAlignment.Center);
                square.setPreferredSize(new Vector3f(square.getPreferredSize().y, square.getPreferredSize().y, 0));
                square.setLocalTranslation(startPosX, startPosY, 1);
                guiNode.attachChild(square);
                startPosX += square.getPreferredSize().x;
                if ((i == 1) && (j == 0)) {
                    chosenSquare = square;
                    chosenSquare.setBackground(new QuadBackgroundComponent(ColorRGBA.fromRGBA255(186, 0, 7, 255)));
                }
            }
            startPosY -= square.getPreferredSize().y;
            startPosX -= 9 * square.getPreferredSize().x;
        }


        for(int i=0;i<4;i++){
            square = new Button("?");
            square.setColor(ColorRGBA.Black);
            square.addClickCommands(this::choose);
            square.setBackground(new QuadBackgroundComponent(ColorRGBA.Black));
            square.setFontSize(48f);
            square.setTextHAlignment(HAlignment.Center);
            square.setPreferredSize(new Vector3f(square.getPreferredSize().y, square.getPreferredSize().y, 0));
            square.setLocalTranslation(190+i*square.getPreferredSize().y, 190*3, 1);
            switch (i){
                case 0 -> square.setShadowColor(ColorRGBA.Blue);
                case 1 -> square.setShadowColor(ColorRGBA.Yellow);
                case 2 -> square.setShadowColor(ColorRGBA.Green);
                case 3 -> square.setShadowColor(ColorRGBA.Magenta);
            }
            square.setHighlightColor(ColorRGBA.White);
            square.setFocusColor(ColorRGBA.White);
            codeSquares.add(square);
            guiNode.attachChild(square);
        }


        initKeys();
    }

    /**
     * Number keys initialization
     */
    private void initKeys() {
        this.app.getInputManager().addMapping("1", new KeyTrigger(KeyInput.KEY_1));
        this.app.getInputManager().addMapping("2", new KeyTrigger(KeyInput.KEY_2));
        this.app.getInputManager().addMapping("3", new KeyTrigger(KeyInput.KEY_3));
        this.app.getInputManager().addMapping("4", new KeyTrigger(KeyInput.KEY_4));
        this.app.getInputManager().addMapping("5", new KeyTrigger(KeyInput.KEY_5));
        this.app.getInputManager().addMapping("6", new KeyTrigger(KeyInput.KEY_6));
        this.app.getInputManager().addMapping("7", new KeyTrigger(KeyInput.KEY_7));
        this.app.getInputManager().addMapping("8", new KeyTrigger(KeyInput.KEY_8));
        this.app.getInputManager().addMapping("9", new KeyTrigger(KeyInput.KEY_9));

        this.app.getInputManager().addListener(this, "1", "2", "3", "4", "5", "6", "7", "8", "9");
    }


    @Override
    public void onAction(String number, boolean isPressed, float tpf) {
        chosenSquare.setText(number);
        if(codeSquares.contains(chosenSquare)){
            if((codeSquares.get(0).getText().equals("1"))&&(codeSquares.get(1).getText().equals("4"))&&(codeSquares.get(2).getText().equals("2"))&&(codeSquares.get(3).getText().equals("9"))){
                guiNode.detachAllChildren();
                app.gameFinal();
            }
        }
    }

    /**
     * Choosing button
     * @param source - source button
     */
    private void choose(Button source) {
        if(sudokuSquares.contains(chosenSquare)){
            chosenSquare.setBackground(new QuadBackgroundComponent(ColorRGBA.fromRGBA255(126, 117, 108, 255)));
        }
        else {
            chosenSquare.setBackground(new QuadBackgroundComponent(ColorRGBA.Black));
        }
        chosenSquare = source;
        if(sudokuSquares.contains(chosenSquare)){
            chosenSquare.setBackground(new QuadBackgroundComponent(ColorRGBA.fromRGBA255(186, 0, 7, 255)));
        }
        else {
            chosenSquare.setBackground(new QuadBackgroundComponent(ColorRGBA.DarkGray));
        }
    }


}
