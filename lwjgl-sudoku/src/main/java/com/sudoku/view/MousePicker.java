package com.sudoku.view;
import java.awt.event.MouseEvent;

public class MousePicker {
    
    
    
    
    
    
    
    
    
    public float[] getCurrentMouse(MouseEvent e){
        float[] currentMouse = new float[2];
        currentMouse[0] = e.getX();
        currentMouse[1] = e.getY();
        
        return currentMouse;
    }
}
