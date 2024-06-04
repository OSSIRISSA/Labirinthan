package labirinthan.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.style.BaseStyles;
import labirinthan.Labirinthan;

import java.util.ArrayList;


public class PuzzlePyramid {

    private final Labirinthan app;
    private final Node guiNode;
    private final AppSettings settings;
    private final AssetManager assetManager;
    private final BitmapFont mainFont;
    public ArrayList<Button> squares = new ArrayList<>();
    public ArrayList<Button> chosenSquares = new ArrayList<>();
    public ArrayList<Button> answerSquares = new ArrayList<>();
    public Button chosenSquare;
    public float size = 30;

    public ColorRGBA chooseColor = ColorRGBA.fromRGBA255(186, 0, 7, 255);
    public ColorRGBA defaultColor = ColorRGBA.fromRGBA255(126, 117, 108, 255);
    public ColorRGBA drawColor = ColorRGBA.fromRGBA255(25, 72, 72, 255);


    public PuzzlePyramid(Labirinthan application, Node guiNode, AppSettings settings, AssetManager assetManager) {
        this.app = application;
        this.guiNode = guiNode;
        this.settings = settings;
        this.assetManager = assetManager;

        GuiGlobals.initialize(application);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        mainFont = this.assetManager.loadFont("Interface/demi.fnt");
    }

    public void createScreen() {

        float startPosX = settings.getWidth() / 2f - size * 11.5f;
        float startPosY = settings.getHeight() / 2f - size * 11.5f;

        Button square;

        for (int a = 0; a < 6; a++) {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    square = new Button(" ");
                    squares.add(square);
                    square.setBackground(new QuadBackgroundComponent(defaultColor));
                    switch (a) {
                        case 0 -> {
                            if ((i > 0) && (j == 3) && (i < 6)) {
                                square.setBackground(new QuadBackgroundComponent(drawColor));
                            }
                            if ((i > 2) && (j == 1) && (i < 6)) {
                                square.setBackground(new QuadBackgroundComponent(drawColor));
                            }
                            if ((i > 2) && (j == 5) && (i < 6)) {
                                square.setBackground(new QuadBackgroundComponent(drawColor));
                            }
                            if ((i == 3) && ((j == 2)||(j == 4))) {
                                square.setBackground(new QuadBackgroundComponent(drawColor));
                            }

                        }
                        case 1 -> {
                            if (((i ==5)||(i==3)||(i==1))&&(j>0)&&(j<6)) {
                                square.setBackground(new QuadBackgroundComponent(drawColor));
                            }
                            if (((j == 1)||(j==5)) && ((i == 2)||(i == 4))) {
                                square.setBackground(new QuadBackgroundComponent(drawColor));
                            }
                            if ((j == 3) && (i == 2)) {
                                square.setBackground(new QuadBackgroundComponent(drawColor));
                            }
                        }
                        case 2 -> {
                            square.addClickCommands(this::choose);
                            if (((i ==5)||(i==3)||(i==1))&&(j>0)&&(j<6)) {
                                answerSquares.add(square);
                            }
                            if ((i==4) && ((j == 3)||(j == 5)||(j == 1))) {
                                answerSquares.add(square);
                            }
                            if ((i==2) && (j == 3)) {
                                answerSquares.add(square);
                            }
                        }
                        case 3 -> {
                            if (((i ==5)||(i==3)||(i==1))&&(j>0)&&(j<6)) {
                                square.setBackground(new QuadBackgroundComponent(drawColor));
                            }
                            if ((i ==4) && (j == 3)) {
                                square.setBackground(new QuadBackgroundComponent(drawColor));
                            }
                            if ((i == 2) && ((j == 1)||(j == 5))) {
                                square.setBackground(new QuadBackgroundComponent(drawColor));
                            }
                        }
                        case 4 -> {
                            if (((i ==5)||(i==3)||(i==1))&&(j>0)&&(j<6)) {
                                square.setBackground(new QuadBackgroundComponent(drawColor));
                            }
                            if (((i == 2)||(i==4)) && (j == 3)) {
                                square.setBackground(new QuadBackgroundComponent(drawColor));
                            }
                        }
                        case 5 -> {
                            if ((j == 3) && (i >0)&&(i <6)) {
                                square.setBackground(new QuadBackgroundComponent(drawColor));
                            }
                        }
                    }
                    square.setTextHAlignment(HAlignment.Center);
                    square.setPreferredSize(new Vector3f(size, size, 0));
                    square.setLocalTranslation(startPosX, startPosY, 1);
                    guiNode.attachChild(square);
                    startPosX += size;
                }
                startPosY += size;
                startPosX -= 7 * size;
            }
            switch (a) {
                case 0, 1, 3 -> {
                    startPosY += size;
                    startPosX += 4 * size;
                }
                case 2 -> startPosY -= 23 * size;
                case 4 -> {
                    startPosY -= 15 * size;
                    startPosX += 4 * size;
                }
            }
        }



        /*for(int i=0;i<4;i++){
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
        }*/

    }

    public void check() {
        boolean win = true;
        for(Button s:chosenSquares){
            if (!answerSquares.contains(s)){
                win = false;
                break;
            }
        }
        for(Button s:answerSquares){
            if (!chosenSquares.contains(s)){
                win = false;
                break;
            }
        }
        if(win){
            guiNode.detachAllChildren();
            app.startLevel1();
        }
    }

    private void choose(Button source) {
        if (chosenSquares.contains(source)) {
            source.setBackground(new QuadBackgroundComponent(defaultColor));
            chosenSquares.remove(source);
        } else {
            source.setBackground(new QuadBackgroundComponent(chooseColor));
            chosenSquares.add(source);
        }
        check();
    }

}