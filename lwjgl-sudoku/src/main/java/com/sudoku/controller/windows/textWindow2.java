package com.sudoku.controller.windows;

import static org.lwjgl.glfw.GLFW.*;
import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.model.SudokuBoard;
import com.sudoku.controller.WindowManager;
import com.sudoku.model.Field;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.elements.FieldButton;
import com.sudoku.view.fonts.CreateFont;

public class textWindow2 extends Window implements WindowInterface {
    
    private WindowManager wm;
	private CreateFont font;
	private CreateString text;

    public textWindow2(WindowManager wm) {
        super(wm);
        this.wm = wm;
		wm.setActiveWindow(this);
    }

    public void create() {
        // This code runs once
		font = new CreateFont("Sudoku-Project/lwjgl-sudoku/assets/fonts/ARIAL.TTF", 128);

		//creates a shader and a class that can display strings
		Shader fontShader = new Shader("lwjgl-sudoku/assets/fonts/fontShader.glsl");
		text = new CreateString(fontShader, font);
    }

    public void step() {
		// This code runs every frame
        text.makeText("Text2", 200, 200, 1f, new float[]{1.0f,0.0f,0.0f});
		text.flush();
    }

    @Override // If you don't need a key callback, just delete this
    public void keyCallback(int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            new textWindow(wm); // Switch to textwindow
        }
    }

}

