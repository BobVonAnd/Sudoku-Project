package com.sudoku.controller;
import java.util.ArrayList;

import com.sudoku.controller.Window;
import com.sudoku.view.elements.Element;


public abstract class Window {

    private ArrayList<Element> Elements = new ArrayList<>();
    private WindowManager wm;
    private long window;

    public Window(WindowManager wm) {
		this.wm = wm;
	}

    public void addElement(Element e) {
        Elements.add(e);
    }

    public boolean removeElement(Element e) {
        if (Elements.contains(e)) {
            Elements.remove(e);
            e = null;
            return true;
        }
        return false;
        
    }

    public void draw() {
      Elements.forEach(e -> e.draw());
    }

    public long getWindow() {
        return window;
    }
}
