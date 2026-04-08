package com.sudoku;

import org.junit.jupiter.api.Test;

import com.sudoku.model.Field;
//import com.sudoku.view.Button;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class FieldTest {
    private Field field;

    @BeforeEach
    void setup() {
        field = new Field(0, 0, 1, 1);
    }

    @Test
    void setFieldValueTest() {
        field.setValue(2);
        assertEquals(2,field.getValue());
    }

    @Test
    void fieldValueConstraintTest() {
        assertTrue(field.getValue() >= 0 ? true : field.getValue() <= field.getBoardSize() ? true : false);
        Field impossibleField = new Field(0, 0, 69, 1);
        assertTrue(impossibleField.getValue() >= 0 ? true : impossibleField.getValue() <= impossibleField.getBoardSize() ? true : false);
    }

    @Test
    void addEdgeTest() {
        Field sisterField = new Field(1,0,2,1); 
        field.addEdge(sisterField);
        assertTrue(field.getEdges().contains(sisterField));
    }

    @Test
    void removeEdgeTest() {
        Field sisterField = new Field(1,0,2,1); 
        field.addEdge(sisterField);
        field.removeEdge(sisterField);
        assertTrue(field.getEdges().contains(sisterField) == false);
    }

    @Test
    void notcontainsEdgeTest() {
        Field sisterField = new Field(1,0,2,1); 
        assertTrue(field.notcontainsEdge(sisterField));
    }
    
    @Test
    void clearLeTest() {
        assertEquals(1, field.getLeSize());
        field.clearLe();
        assertEquals(0, field.getLeSize());
    }

    @Test
    void removeLETest() {
        field.removeLE(1);

        assertFalse(field.getLegalEntries().contains(1));
    }

    //@Test
    //void fieldButtonTest() {
    //    assertTrue(field.getButton() instanceof Button);
    //}

    @Test
    void removeValueFromNeighboursTest() {
        Field neighbor = new Field(1,0,1,3);

        field.setValue(2);
        field.addEdge(neighbor);

        field.removeValueFromLegalEntriesOfNeighbours();

        assertFalse(neighbor.getLegalEntries().contains(2));
    }

    @Test
    void getCoordinatesTest() {
        assertArrayEquals(new int[]{0,0}, field.getCoordinates());
    }

    @Test
    void getPositionTest() {
        Field f = new Field(4, 5, 1, 9);

        assertArrayEquals(new int[]{1,2}, f.getPosition());
    }
    
}