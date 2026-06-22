package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.controller.WindowManager;
import com.sudoku.model.Gamepad;
import com.sudoku.model.SudokuBoard;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.elements.MenuButton;
import com.sudoku.view.elements.Sudoku;
import com.sudoku.view.font.CreateFont;

public class EndScreenWindow extends Window implements WindowInterface {
    
    private WindowManager wm;
    private Gamepad gpad;
    private SudokuBoard sudokuBoard;
    private Sudoku sudokuUI;
    private MenuButton returnButton;

    private int width;
    private int height;

    private CreateFont font;
    private CreateString text;
    private Shader fontShader;

    private String endCondition;

  

    public EndScreenWindow(WindowManager wm, SudokuBoard sb, int width, int height, String endCondition) {
        super(wm);
        this.wm = wm;
        this.sudokuBoard = sb;
        this.width = width;
        this.height = height;
        this.endCondition = endCondition;
        wm.setActiveWindow(this);
    }

    public void create() {
        // This code runs once
        font = wm.getFont();
        fontShader = wm.getFontShader();
        text = new CreateString(fontShader, font, width, height);
        gpad = new Gamepad();

        returnButton = new MenuButton(-0.7, 0.75, 0.2, text, fontShader, "Return");
        addElement(returnButton, 0);

        sudokuUI = new Sudoku(width, height, 1.6, 0,0, sudokuBoard, font, fontShader, this);
        addElement(sudokuUI, 0);
    }

    public void step() {
        // This code runs every frame
        gpad.step();


        switch (endCondition) {
            case "win":
                text.makeText("You Finished The Sudoku!", -0.3f, 0.83f, 0.5f, new float[]{0f,0f,0f});
                break;
            case "lost":
                text.makeText("You Lost The Sudoku", -0.28f, 0.83f, 0.5f, new float[]{1f,0f,0f});
                break;
            //add cases like timeconstrain if needed :)
            default:
                throw new AssertionError();
        }
        text.flush();
        

        holdOver(returnButton);
    }

    private void holdOver(MenuButton button) {
        double mouseXt = mouseX / (width / 2) - 1;
        double mouseYt = -mouseY / (height / 2) + 1;
        if (button.getPos()[0] - returnButton.getSize() / 2 < mouseXt &
                button.getPos()[0] + returnButton.getSize() / 2 > mouseXt &

                button.getPos()[1] - button.getSize() / 2 < mouseYt &
                button.getPos()[1] + button.getSize() / 2 > mouseYt) {
            button.heldOver(true);
        } else {
            button.heldOver(false);
        }
    }

    @Override // If you don't need a key callback, just delete this
    public void keyCallback(int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            System.out.println("Space pressed!");
        }
    }
    
    @Override // If you don't need a resize callback, just delete this
    public void resizeCallback(int width, int height) {
        System.out.println("New size: " + width + "x" + height);
    }

    @Override // If you don't need a mouse button callback, just delete this
    public void mouseButtonCallback(int button, int action, int mods) {

        if (button == GLFW_MOUSE_BUTTON_LEFT &&
            action == GLFW_PRESS) {

            if(returnButton.isHeldOver()){
                new mainMenuWindow(wm, width, height);
            }
        }

        if (button == GLFW_MOUSE_BUTTON_RIGHT &&
            action == GLFW_PRESS) {

            System.out.println("Right click!");
        }
    }

    private double mouseX;
    private double mouseY;

    @Override
    public void cursorPosCallback(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

}
