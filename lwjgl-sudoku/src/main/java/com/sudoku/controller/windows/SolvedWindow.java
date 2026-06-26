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

public class SolvedWindow extends Window implements WindowInterface {
    
    private WindowManager wm; 
    private Sudoku sudokuUI; // holds the sudoku ui element
    private SudokuBoard sudokuBoard;
    private SudokuBoard solvedSudokuBoard;
    private playSudokuWindow pw; // holds the play sudoku window
    private CreateMenuWindow cmw; // holds the create menu window
    private Gamepad gpad; // holds the gamepad

    private int width;
    private int height;
    private CreateFont font;
    private Shader fontShader; // font shader
    private CreateString text;

    // buffer for `a` button
    private long a_buffer_timestamp = System.currentTimeMillis();
    private long a_buffer_max = 200;

    private MenuButton returnButton;
    private MenuButton end; 

    public SolvedWindow(WindowManager wm, int width, int height, SudokuBoard sb, SudokuBoard solvedsb, playSudokuWindow pw) {
        super(wm); // init parent
        this.wm = wm;
        sudokuBoard = sb; // updated sudoku board
        solvedSudokuBoard = solvedsb;
        this.pw = pw;
        this.width = width;
        this.height = height;
        wm.setActiveWindow(this); // set this to active window
        
    }

     public SolvedWindow(WindowManager wm, int width, int height, SudokuBoard sb, SudokuBoard solvedsb, CreateMenuWindow cmw) {
        super(wm);
        this.wm = wm;
        sudokuBoard = sb; // updated sudoku board
        solvedSudokuBoard = solvedsb;
        this.cmw = cmw;
        this.width = width;
        this.height = height;
        wm.setActiveWindow(this); // set this to active window
        
    }

    public void update(WindowManager wm, int width, int height, SudokuBoard sb, SudokuBoard solvedsb, playSudokuWindow pw){
        this.wm = wm;
        sudokuBoard = sb;
        solvedSudokuBoard = solvedsb; // updated sudoku board
        this.pw = pw;
        this.width = width;
        this.height = height;
        wm.setActiveWindow(this);  // set this to active window
    }


    public void create() {
        // This code runs once
        gpad = new Gamepad();
        font = wm.getFont();
		fontShader = wm.getFontShader();
		text = new CreateString(fontShader, font, width, height);        

        // create return button
        returnButton = new MenuButton(-0.7, 0.75, 0.2, text, fontShader, "Back");
        addElement(returnButton, 0); // add return button to ui
        gpad.addElement(returnButton, 0, 0); // add return button to gamepad

        end = new MenuButton(-0.7, -0.75, 0.2, text, fontShader, "End");
        addElement(end, 0); // add end button to ui
        gpad.addElement(end, 0, 1);  // add end button to gamepad


        sudokuUI = new Sudoku(width, height, 1.6, solvedSudokuBoard, sudokuBoard, font, fontShader, this);
        addElement(sudokuUI, 0); // add sudoku to ui

    }

    

    public void step() {
        // This code runs every frame
        gpad.step(); // controller step
        holdOver(end); // is held over end?
        holdOver(returnButton); // is held over return?
        long now = System.currentTimeMillis();
        // if pressed a?
        boolean entered = gpad.isEntered() && (now - a_buffer_timestamp >= a_buffer_max);
        if (entered) {
            a_buffer_timestamp = now; // set new timestamp
            windowTransition(end); // go to end if selected
            windowTransition(returnButton); // go to return if selected
        }
    }

    // collision detection with mouse and button
     private void holdOver(MenuButton button) {
        double mouseXt = mouseX / (width / 2) - 1;
        double mouseYt = -mouseY / (height / 2) + 1;
        if ((button.getPos()[0] - returnButton.getSize() / 2 < mouseXt &
                button.getPos()[0] + returnButton.getSize() / 2 > mouseXt &

                button.getPos()[1] - button.getSize() / 2 < mouseYt &
                button.getPos()[1] + button.getSize() / 2 > mouseYt && !gpad.isConnected()) || gpad.isSelected(button)) {
            button.heldOver(true);
        } else {
            button.heldOver(false);
        }
    }
    
    @Override 
    public void resizeCallback(int width, int height) {
        this.width = width;
        this.height = height;
        sudokuUI.resize(width, height);
    }

    @Override
    public void mouseButtonCallback(int button, int action, int mods) {

        if (button == GLFW_MOUSE_BUTTON_LEFT &&
            action == GLFW_PRESS) {
            windowTransition(returnButton); // if click mouse return
            windowTransition(end); // if click mouse end
        }
    }

    // this is where it checks if we need to go to a specfic window, then goes to that window
    public void windowTransition(MenuButton e) {
        if(e == returnButton && returnButton.isHeldOver()){
            if(pw == null && cmw != null){
                wm.setActiveWindow(cmw);
            }else{
                wm.setActiveWindow(pw);
            }
            
        }else if(e == end && end.isHeldOver()){
            new mainMenuWindow(wm, width, height);
        }
    }

    private double mouseX;
    private double mouseY;

    @Override // updates the mouse position
    public void cursorPosCallback(double x, double y) {
        this.mouseX = x;
        this.mouseY = y;
    }


} // hi Karl! :)