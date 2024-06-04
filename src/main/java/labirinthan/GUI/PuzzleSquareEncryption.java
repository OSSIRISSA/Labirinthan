package labirinthan.GUI;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.style.BaseStyles;
import labirinthan.Labirinthan;

import java.util.ArrayList;


public class PuzzleSquareEncryption implements ActionListener {

    private final Labirinthan app;
    private final Node guiNode;
    private final AppSettings settings;
    private final AssetManager assetManager;
    private final BitmapFont mainFont;
    public ArrayList<Button> sudokuSquares = new ArrayList<>();
    public ArrayList<Button> codeSquares = new ArrayList<>();
    public Button chosenSquare;
    public ColorRGBA chooseColor = ColorRGBA.fromRGBA255(186, 0, 7, 255);
    public ColorRGBA defaultColor = ColorRGBA.fromRGBA255(126, 117, 108, 255);
    public float size = 50;
    private float startPosX;
    private float startPosY;

    public PuzzleSquareEncryption(Labirinthan application, Node guiNode, AppSettings settings, AssetManager assetManager) {
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

        startPosX = settings.getWidth() * 0.5f - 150;
        startPosY = settings.getHeight() * 0.9f;

        Button square = new Button(" ");


        Panel line = new Panel(5, 100 * 3);
        line.setLocalTranslation(startPosX + 100, startPosY, 1);
        line.setBackground(new QuadBackgroundComponent(chooseColor));
        guiNode.attachChild(line);
        line = new Panel(5, 100 * 3);
        line.setLocalTranslation(startPosX + 100 * 2 + 4, startPosY, 1);
        line.setBackground(new QuadBackgroundComponent(chooseColor));
        guiNode.attachChild(line);
        line = new Panel(100 * 3, 5);
        line.setLocalTranslation(startPosX, startPosY - 100, 1);
        line.setBackground(new QuadBackgroundComponent(chooseColor));
        guiNode.attachChild(line);
        line = new Panel(100 * 3, 5);
        line.setLocalTranslation(startPosX, startPosY - 100 * 2 - 4, 1);
        line.setBackground(new QuadBackgroundComponent(chooseColor));
        guiNode.attachChild(line);

        Label alphabet = new Label("abc def ghi");
        alphabet.setFontSize(35);
        alphabet.setFont(mainFont);
        alphabet.setColor(chooseColor);
        alphabet.setLocalTranslation(startPosX, startPosY - 15, 1);
        guiNode.attachChild(alphabet);
        alphabet = new Label(" jkl mno pqr");
        alphabet.setFontSize(35);
        alphabet.setFont(mainFont);
        alphabet.setColor(chooseColor);
        alphabet.setLocalTranslation(startPosX, startPosY - 100 - 5 - 15, 1);
        guiNode.attachChild(alphabet);
        alphabet = new Label("stu vwx yz");
        alphabet.setFontSize(35);
        alphabet.setFont(mainFont);
        alphabet.setColor(chooseColor);
        alphabet.setLocalTranslation(startPosX, startPosY - 200 - 10 - 15, 1);
        guiNode.attachChild(alphabet);


        for (int i = 0; i < 3; i++) {
            square = new Button("?");
            square.setColor(ColorRGBA.Black);
            square.addClickCommands(this::choose);
            square.setBackground(new QuadBackgroundComponent(defaultColor));
            square.setFontSize(40f);
            square.setTextHAlignment(HAlignment.Center);
            square.setPreferredSize(new Vector3f(size + 10, size + 10, 0));
            square.setLocalTranslation(settings.getWidth() * 0.75f+size, settings.getHeight() * 0.5f - i * (size + 10) * 2, 1);
            square.setShadowColor(ColorRGBA.Black);
            square.setHighlightColor(ColorRGBA.White);
            square.setFocusColor(ColorRGBA.White);
            codeSquares.add(square);
            guiNode.attachChild(square);
        }
        initKeys();

        startPosX = settings.getWidth() * 0.25f - size*2;
        startPosY = settings.getHeight() * 0.5f;

        //four
        bottomLine();
        leftLine();
        rightLine();
        dots();
        next();

        topLine();
        bottomLine();
        leftLine();
        rightLine();
        dots();
        next();

        topLine();
        rightLine();
        dots();
        next();

        topLine();
        bottomLine();
        leftLine();
        dots();
        space();

        //divided
        bottomLine();
        leftLine();
        rightLine();
        next();

        bottomLine();
        leftLine();
        dots();
        next();

        topLine();
        leftLine();
        rightLine();
        next();

        bottomLine();
        leftLine();
        dots();
        next();

        bottomLine();
        leftLine();
        rightLine();
        next();

        bottomLine();
        leftLine();
        rightLine();
        dot();
        next();

        bottomLine();
        leftLine();
        rightLine();
        space();

        //by
        bottomLine();
        rightLine();
        dot();
        next();

        topLine();
        leftLine();
        space();

        //two
        topLine();
        rightLine();
        dot();
        next();

        topLine();
        rightLine();
        leftLine();
        dot();
        next();

        topLine();
        bottomLine();
        leftLine();
        rightLine();
        dots();
        enter();

        //seven
        topLine();
        rightLine();
        next();

        bottomLine();
        leftLine();
        rightLine();
        dot();
        next();

        topLine();
        leftLine();
        rightLine();
        next();

        bottomLine();
        leftLine();
        rightLine();
        dot();
        next();

        topLine();
        bottomLine();
        rightLine();
        leftLine();
        dot();
        space();

        //plus
        topLine();
        bottomLine();
        leftLine();
        next();

        topLine();
        bottomLine();
        rightLine();
        dots();
        next();

        topLine();
        rightLine();
        dots();
        next();

        topLine();
        rightLine();
        space();

        //one
        topLine();
        bottomLine();
        leftLine();
        rightLine();
        dots();
        next();

        topLine();
        bottomLine();
        rightLine();
        leftLine();
        dot();
        next();

        bottomLine();
        rightLine();
        leftLine();
        dot();
        enter();

        //your
        topLine();
        leftLine();
        next();

        topLine();
        bottomLine();
        leftLine();
        rightLine();
        dots();
        next();

        topLine();
        rightLine();
        dots();
        next();

        topLine();
        bottomLine();
        leftLine();
        dots();
        space();

        //toes
        topLine();
        rightLine();
        dot();
        next();

        topLine();
        bottomLine();
        leftLine();
        rightLine();
        dots();
        next();

        bottomLine();
        leftLine();
        rightLine();
        dot();
        next();

        topLine();
        rightLine();
        space();

        //minus
        topLine();
        bottomLine();
        leftLine();
        rightLine();
        next();

        leftLine();
        bottomLine();
        dots();
        next();

        topLine();
        bottomLine();
        leftLine();
        rightLine();
        dot();
        next();

        topLine();
        rightLine();
        dots();
        next();

        topLine();
        rightLine();
        space();

        //three
        topLine();
        rightLine();
        dot();
        next();

        bottomLine();
        leftLine();
        dot();
        next();

        topLine();
        bottomLine();
        leftLine();
        dots();
        next();

        bottomLine();
        leftLine();
        rightLine();
        dot();
        next();

        bottomLine();
        leftLine();
        rightLine();
        dot();
        next();
    }

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
        if (codeSquares.contains(chosenSquare)) {
            if ((codeSquares.get(0).getText().equals("2")) && (codeSquares.get(1).getText().equals("8")) && (codeSquares.get(2).getText().equals("7"))) {
                guiNode.detachAllChildren();
                app.startLevel1();
            }
        }
    }

    private void choose(Button source) {
        if (chosenSquare != null) {
            chosenSquare.setBackground(new QuadBackgroundComponent(defaultColor));
        }
        chosenSquare = source;
        chosenSquare.setBackground(new QuadBackgroundComponent(chooseColor));
    }

    private void topLine() {
        Panel line = new Panel(size, 5);
        line.setLocalTranslation(startPosX, startPosY, 1);
        line.setBackground(new QuadBackgroundComponent(defaultColor));
        guiNode.attachChild(line);
    }

    private void bottomLine() {
        Panel line = new Panel(size, 5);
        line.setLocalTranslation(startPosX, startPosY - size + 5, 1);
        line.setBackground(new QuadBackgroundComponent(defaultColor));
        guiNode.attachChild(line);
    }

    private void leftLine() {
        Panel line = new Panel(5, size);
        line.setLocalTranslation(startPosX, startPosY, 1);
        line.setBackground(new QuadBackgroundComponent(defaultColor));
        guiNode.attachChild(line);
    }

    private void rightLine() {
        Panel line = new Panel(5, size);
        line.setLocalTranslation(startPosX + size - 5, startPosY, 1);
        line.setBackground(new QuadBackgroundComponent(defaultColor));
        guiNode.attachChild(line);
    }

    private void dot() {
        Panel line = new Panel(5, 5);
        line.setLocalTranslation(startPosX + size / 2 - 2.5f, startPosY - size / 2 + 2.5f, 1);
        line.setBackground(new QuadBackgroundComponent(defaultColor));
        guiNode.attachChild(line);
    }

    private void dots() {
        Panel line = new Panel(5, 5);
        line.setLocalTranslation(startPosX + size / 2 - 2.5f - (5), startPosY - size / 2 + 2.5f, 1);
        line.setBackground(new QuadBackgroundComponent(defaultColor));
        guiNode.attachChild(line);
        line = new Panel(5, 5);
        line.setLocalTranslation(startPosX + size / 2 - 2.5f + (5), startPosY - size / 2 + 2.5f, 1);
        line.setBackground(new QuadBackgroundComponent(defaultColor));
        guiNode.attachChild(line);
    }

    private void next() {
        startPosX += size + 5;
    }

    private void space() {
        startPosX += size + 5 + size / 2;
    }

    private void enter() {
        startPosX = settings.getWidth() * 0.25f - size*2;
        startPosY -= (size + 10) * 2;
    }
}
