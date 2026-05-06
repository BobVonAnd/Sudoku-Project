package com.sudoku.controller.windows;

import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.model.SudokuBoard;
import com.sudoku.controller.WindowManager;
import com.sudoku.model.Field;
import com.sudoku.view.CreateString;
import com.sudoku.view.Shader;
import com.sudoku.view.elements.FieldButton;
import com.sudoku.view.fonts.CreateFont;

public class textWindow extends Window implements WindowInterface {
    
    private WindowManager wm;
	private CreateFont font;
	private CreateString text;

    public textWindow(WindowManager wm) {
        super(wm);
        this.wm = wm;
		wm.setActiveWindow(this, super.getWindow());
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
        text.makeText("Sådan!!!", 200, 200, 1f, new float[]{1.0f,0.0f,0.0f});
		text.flush();
    }

}

