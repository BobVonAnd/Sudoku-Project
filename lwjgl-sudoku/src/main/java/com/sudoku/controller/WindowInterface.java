package com.sudoku.controller;

public interface WindowInterface {
    public void create();
    public void step();

    default void keyCallback(int key, int scancode, int action, int mods) {}
    default void resizeCallback(int width, int height) {}
    default void mouseButtonCallback(int button, int action, int mods) {}
    default void cursorPosCallback(double x, double y) {}
}
