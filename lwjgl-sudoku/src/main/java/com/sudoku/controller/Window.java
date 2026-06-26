package com.sudoku.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.sudoku.controller.Window;
import com.sudoku.view.elements.Element;


public abstract class Window {
    private Map<Double, ArrayList<Element>> dictionary = new TreeMap<>();
    private WindowManager wm;

    public Window(WindowManager wm) {
		this.wm = wm;
	}

    // add element to the window, so it can be drawn later on
    public void addElement(Element e, double depth) {
        ArrayList<Element> list = dictionary.get(depth);
        if (list != null) {
            list.add(e);
        } else {
            ArrayList<Element> l = new ArrayList<Element>();
            l.add(e);
            dictionary.put(depth, l);
        }
    }

    // checks if the element is part of the map
    public boolean elementExists(Element e) {
        for (ArrayList<Element> list : dictionary.values()) {
            if (list.contains(e)) {
                return true;
            }
        }
        return false;
    }

    // removes element from map
    public boolean removeElement(Element e) {
        for (Map.Entry<Double, ArrayList<Element>> entry : dictionary.entrySet()) {
            ArrayList<Element> list = entry.getValue();

            if (list.remove(e)) {
                if (list.isEmpty()) {
                    dictionary.remove(entry.getKey());
                }
                return true;
            }
        }
        return false;
    }

    // changes the depth of the element
    public void changeElementDepth(Element e, double depth) {
        removeElement(e);
        addElement(e, depth);
    }

    // gets run every frame, and draws every active element
    public void draw() {
        for (Map.Entry<Double, ArrayList<Element>> entry : dictionary.entrySet()) {
            ArrayList<Element> list = entry.getValue();
            for (int i = 0 ; i < list.size() ; i++) {
                list.get(i).draw();
            }
        }
    }
}
