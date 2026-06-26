package com.sudoku.controller;

public interface WindowInterface {
    public void create(); // create gets run once
    public void step(); // step gets run every frame

    default void keyCallback(int key, int scancode, int action, int mods) {} // callback, needs to be overridden
    default void resizeCallback(int width, int height) {} // callback, needs to be overridden
    default void mouseButtonCallback(int button, int action, int mods) {} // callback, needs to be overridden
    default void cursorPosCallback(double x, double y) {} // callback, needs to be overridden
}
