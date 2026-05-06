package com.sudoku.controller;
import com.sudoku.view.elements.Element;

import java.util.ArrayList;

public class WindowManager {
    private ArrayList<Element> Elements = new ArrayList<>();
    
    public WindowManager() {

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

    // Den skal kunne
    /// Holde styr på alt hvad der sker i hvert vindue
    /// kunne draw hvad der er i det CURRENT vindue
    /// tilføje og remove i det current vindue
}
