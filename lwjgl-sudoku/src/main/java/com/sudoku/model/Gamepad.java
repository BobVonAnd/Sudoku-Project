package com.sudoku.model;
import org.lwjgl.glfw.GLFWGamepadState;

import com.sudoku.view.elements.Element;

import static org.lwjgl.glfw.GLFW.*;

import java.util.TreeMap;

public class Gamepad {
    enum X_Input {
        LEFT,
        RIGHT,
        NONE
    }

    enum Y_Input {
        UP,
        DOWN,
        NONE
    }

    private X_Input x = X_Input.NONE;
    private Y_Input y = Y_Input.NONE;

    // position cause we want to compare euclidian distance
    record Position(int x, int y) implements Comparable<Position> { 
        @Override
        public int compareTo(Position other) {
            long d1 = (long)x * x + (long)y * y;
            long d2 = (long)other.x * other.x + (long)other.y * other.y;

            int cmp = Long.compare(d1, d2);
            if (cmp != 0) return cmp;

            cmp = Integer.compare(x, other.x);
            if (cmp != 0) return cmp;

            return Integer.compare(y, other.y);
        }
    }

    // tree map for the buttons
    private TreeMap<Position, Element> buttonMap = new TreeMap<>();
    private Position position = new Position(0,0);

    private long buffer = 200; // in ms
    private long buffer_timestamp = System.currentTimeMillis();
    private long aTimestamp = System.currentTimeMillis();
    private long aBuffer = 200; // in ms
    private boolean connected = false;
    private double deadzone = 0.45;
    private boolean entered = false;
    private boolean moveLocked = false;

    public boolean isSelected(Element e) {
        if (connected) {
            return e.equals(buttonMap.get(position));
        }
        return false;
    }

    public boolean isMoveLocked() {
        return moveLocked;
    }

    public void setMoveLocked(boolean moveLocked) {
        this.moveLocked = moveLocked;
    }

    public void setPosition(int x, int y) {
        position = new Position(x, y);
    }
    
    public void addElement(Element e, int x, int y) {
        if (buttonMap.isEmpty()) {
            setPosition(x, y);
        }
        Position pos = new Position(x, y);

        if (buttonMap.containsKey(pos)) {
            return;
        }

        removeElement(e);
        buttonMap.put(pos, e);
    }

    public void removeElement(Element e) {
        Position toRemove = null;

        for (var entry : buttonMap.entrySet()) {
            if (entry.getValue() == e) {
                toRemove = entry.getKey();
                break;
            }
        }

        if (toRemove != null) {
            buttonMap.remove(toRemove);
        }
    }

    public void updateElement(Element e, int x, int y) {
        removeElement(e);
        addElement(e, x, y);
    }

    public int getXDir() {
        switch (x) {
            case LEFT:
                return -1;
            
            case RIGHT:
                return 1;

            default:
                return 0;
        }
    }

    public int getYDir() {
        switch (y) {
            case UP:
                return -1;
            
            case DOWN:
                return 1;

            default:
                return 0;
        }
    }

    public boolean isEntered() {
        boolean temp = entered;
        entered = false;
        return temp;
    }

    public boolean isConnected() {
        return connected;
    }

    private Position findNearest(X_Input xDir, Y_Input yDir) {
        entered = false;
        Position current = new Position(position.x(), position.y());

        Position best = null;
        double bestScore = Double.MAX_VALUE;

        for (Position pos : buttonMap.keySet()) {
            int dx = pos.x() - current.x();
            int dy = pos.y() - current.y();

            // Filter by direction
            if (xDir == X_Input.LEFT  && dx >= 0) continue;
            if (xDir == X_Input.RIGHT && dx <= 0) continue;

            if (yDir == Y_Input.UP    && dy >= 0) continue;
            if (yDir == Y_Input.DOWN  && dy <= 0) continue;

            double score;

            if (xDir != X_Input.NONE) {
                // Horizontal movement prioritizes X
                score = dx * dx + dy * dy * 4;
            } else {
                // Vertical movement prioritizes Y
                score = dy * dy + dx * dx * 4;
            }

            if (score < bestScore) {
                bestScore = score;
                best = pos;
            }
        }

        return best;
    }

    public void step() {
        GLFWGamepadState state = GLFWGamepadState.create();

        if (glfwGetGamepadState(GLFW_JOYSTICK_1, state)) {
            connected = true;
            // get state
            float leftX = state.axes(GLFW_GAMEPAD_AXIS_LEFT_X);
            float leftY = state.axes(GLFW_GAMEPAD_AXIS_LEFT_Y);

            // should we move?
            if (Math.abs(leftX) < 0.02 + deadzone) {
                x = X_Input.NONE;
            } else if (leftX < 0) {
                x = X_Input.LEFT;
            } else {
                x = X_Input.RIGHT;
            }

            if (Math.abs(leftY) < 0.02 + deadzone) {
                y = Y_Input.NONE;
            } else if (leftY < 0) {
                y = Y_Input.UP;
            } else {
                y = Y_Input.DOWN;
            }

            // movement
            
            long now = System.currentTimeMillis();

            boolean canMove =
                (buffer_timestamp == -1 ||
                now - buffer_timestamp >= buffer) &&
                !moveLocked;

            if (canMove) {
                Position best = null;

                if (y != Y_Input.NONE) {
                    best = findNearest(X_Input.NONE, y);
                } else if (x != X_Input.NONE) {
                    best = findNearest(x, Y_Input.NONE);
                }

                if (best != null) {
                    position = best;
                    buffer_timestamp = now;
                }
            }

            boolean aPressed = state.buttons(GLFW_GAMEPAD_BUTTON_A) == GLFW_PRESS;
            if (aPressed && now - aTimestamp >= aBuffer) {
                entered = true;
                aTimestamp = now;
            }
        } else {
            connected = false;
        }
    }
}
