package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_5;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_6;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_7;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.controller.WindowManager;
import com.sudoku.model.SudokuBoard;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.elements.MenuButton;
import com.sudoku.view.elements.Sudoku;
import com.sudoku.view.fonts.CreateFont;

public class playSudokuWindow extends Window implements WindowInterface {
    
    private WindowManager wm;
    private SudokuBoard sudokuBoard;
    private SudokuBoard solvedSudokuBoard;
    private int width, height;
    private double mouseX, mouseY;
    private CreateFont font;
	private CreateString text;
    private Shader fontShader;
    private int[] selectedField = new int[2];
    private Sudoku sudokuFront;
    private MenuButton returnButton;

    private MenuButton solveButton;
    private MenuButton hintButton;

    //private EndScreenWindow = endScreen;

    public playSudokuWindow(WindowManager wm, int width, int height, SudokuBoard sb) {
        super(wm);
        this.wm = wm;
        this.width = width;
        this.height = height;
        sudokuBoard = sb;
        wm.setActiveWindow(this);
    }

    public void create() {
        // This code runs once
        font = wm.getFont();
		//creates a shader and a class that can display strings
		fontShader = wm.getFontShader();
		text = new CreateString(fontShader, font); 
        
        sudokuBoard.populate();

        sudokuFront = new Sudoku(width, height, sudokuBoard, font, fontShader, this);
        addElement(sudokuFront, 0);

        //return to last window
        returnButton = new MenuButton(-0.7, 0.75, 0.2, text, fontShader, "Back");
        addElement(returnButton, 0);

        solveButton = new MenuButton(0.7, 0.75, 0.2, text, fontShader, "Solve");
        addElement(solveButton, 0);

        hintButton = new MenuButton(0.7, 0.50, 0.2, text, fontShader, "Hint");
        addElement(hintButton, 0);


        

        solvedSudokuBoard = new SudokuBoard(sudokuBoard.getSize());
        int[][] integerBoard = SudokuBoard.readOutOffBoard(sudokuBoard);
        solvedSudokuBoard.readIntoBoard(integerBoard);
        solvedSudokuBoard.solve();

    }

    public void step() {
        // This code runs every frame
        
        holdOver(returnButton);
        holdOver(solveButton);
        holdOver(hintButton);
        
    }

    //inout a MenuButton and it will track if the mouse if hovering the button
    private void holdOver(MenuButton button){
        double mouseXt = mouseX/(width/2)-1;
        double mouseYt = -mouseY/(height/2)+1;
        if (button.getPos()[0] - returnButton.getSize()/2 < mouseXt & 
                button.getPos()[0] + returnButton.getSize()/2 > mouseXt &

                button.getPos()[1] - button.getSize()/2 < mouseYt & 
                button.getPos()[1] + button.getSize()/2 > mouseYt) {
                button.heldOver(true);
            } else {
                button.heldOver(false);
            }
    }

    @Override // If you don't need a key callback, just delete this
    public void keyCallback(int key, int scancode, int action, int mods){
        if (key == GLFW_KEY_1 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],1);
                    System.out.println("1 pressed!");
        }else if (key == GLFW_KEY_2 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],2);
                    System.out.println("2 pressed!");
        }else if (key == GLFW_KEY_3 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],3);
                    System.out.println("3 pressed!");
        }else if (key == GLFW_KEY_4 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],4);
                    System.out.println("4 pressed!");
        }else if (key == GLFW_KEY_5 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],5);
                    System.out.println("5 pressed!");
        }else if (key == GLFW_KEY_6 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],6);
                    System.out.println("6 pressed!");
        }else if (key == GLFW_KEY_7 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],7);
                    System.out.println("7 pressed!");
        }else if (key == GLFW_KEY_8 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],8);
                    System.out.println("8 pressed!");
        }else if (key == GLFW_KEY_9 && action == GLFW_PRESS){
                    sudokuBoard.changeField(selectedField[0],selectedField[1],9);
                    System.out.println("9 pressed!");
        }
        
        if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            System.out.println("Space pressed!");

        }
    }
    
    @Override // If you don't need a resize callback, just delete this
    public void resizeCallback(int width, int height) {
        this.width = width;
        this.height = height;
        sudokuFront.resize(width, height);
    }

    @Override // If you don't need a mouse button callback, just delete this
    public void mouseButtonCallback(int button, int action, int mods) {
        
        if (button == GLFW_MOUSE_BUTTON_LEFT &&
            action == GLFW_PRESS) {
            selectedField = sudokuFront.leftClick(mouseX, mouseY);


            //return button
            if (returnButton.isHeldOver() && elementExists(returnButton)) {
                new PlaySudokuSettingsWindow(wm, width, height);
            }else if (solveButton.isHeldOver() && elementExists(solveButton)) {
                new SolvedWindow(wm, width, height, sudokuBoard, solvedSudokuBoard);
            }else if (hintButton.isHeldOver() && elementExists(hintButton)) {

            }
        }

        if (button == GLFW_MOUSE_BUTTON_RIGHT &&
            action == GLFW_PRESS) {

            System.out.println("Right click!");
        }
    }

    @Override
    public void cursorPosCallback(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }

}
