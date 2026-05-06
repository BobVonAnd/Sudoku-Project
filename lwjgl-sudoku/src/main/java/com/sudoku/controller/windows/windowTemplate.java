package com.sudoku.controller.windows;

import com.sudoku.controller.Window;
import com.sudoku.controller.WindowInterface;
import com.sudoku.controller.WindowManager;

/// THIS IS PURELY FOR THE DEVELOPERS TO BE ABLE TO MAKE A WINDOW
public class windowTemplate extends Window implements WindowInterface {
    
    private WindowManager wm;

    public windowTemplate(WindowManager wm) {
        super(wm);
        this.wm = wm;
        wm.setActiveWindow(this, super.getWindow());
    }

    public void create() {
        // This code runs once
    }

    public void step() {
        // This code runs every frame
    }

}
