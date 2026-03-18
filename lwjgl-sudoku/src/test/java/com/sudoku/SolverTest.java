package com.sudoku;

import org.junit.jupiter.api.Test;

import com.sudoku.model.Field;
import com.sudoku.model.Solver;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class SolverTest {
    Solver solver = new Solver();

    private Field createField(int x, int y, int value) {
        Field f = new Field(x, y, value, 9);
        f.clearLe();
        return f;
    }

    private void connect(Field a, Field b) {
        a.addEdge(b);
        b.addEdge(a);
    }

    @Test
    void isRowEdgeTest() {
        Field f1 = createField(0, 0, 0);
        Field f2 = createField(1, 0, 0); 
        Field f3 = createField(0, 1, 0); 

        assertTrue(solver.isRowEdge(f1, f2));
        assertFalse(solver.isRowEdge(f1, f3));
        assertFalse(solver.isRowEdge(f1, f1));
    }

    @Test
    void isColumnEdgeTest() {
        Field f1 = createField(0, 0, 0);
        Field f2 = createField(0, 1, 0); 
        Field f3 = createField(1, 0, 0); 

        assertTrue(solver.isColumnEdge(f1, f2));
        assertFalse(solver.isColumnEdge(f1, f3));
    }

    @Test
    void lookAtNeighboursUniqueCandidateTest() {
        Field target = createField(0, 0, 0);
        target.getLegalEntries().addAll(Arrays.asList(  1, 3, 2));

        Field other1 = createField(1, 0, 0);
        other1.getLegalEntries().addAll(Arrays.asList(1, 3));

        Field other2 = createField(2, 0, 0);
        other2.getLegalEntries().addAll(Arrays.asList(1, 3));

        connect(target, other1);
        connect(target, other2);

        int result = solver.lookAtNeighbours(target);

        assertEquals(2, result);
    }

    @Test
    void lookAtNeighboursNoUniqueCandidateTest() {
        Field target = createField(0, 0, 0);
        target.getLegalEntries().addAll(Arrays.asList(1, 2));

        Field other1 = createField(1, 0, 0);
        other1.getLegalEntries().addAll(Arrays.asList(1, 2));

        Field other2 = createField(2, 0, 0);
        other2.getLegalEntries().addAll(Arrays.asList(1, 2));

        connect(target, other1);
        connect(target, other2);

        int result = solver.lookAtNeighbours(target);

        assertEquals(0, result);
    }

    @Test
    void lookAtNeighboursFilledFieldTest() {
        Field target = createField(0, 0, 5);

        int result = solver.lookAtNeighbours(target);

        assertEquals(0, result);
    }

    @Test
    void nakedPairRowTest() {
        Field f1 = createField(0, 0, 0);
        f1.getLegalEntries().addAll(Arrays.asList(2, 5));

        Field f2 = createField(1, 0, 0);
        f2.getLegalEntries().addAll(Arrays.asList(2, 5));

        Field other = createField(2, 0, 0);
        other.getLegalEntries().addAll(Arrays.asList(2, 5, 7));

        connect(f1, f2);
        connect(f1, other);
        connect(f2, other);

        solver.nakedPair(f1, false);

        assertFalse(other.getLegalEntries().contains(2));
        assertFalse(other.getLegalEntries().contains(5));
    }

    @Test
    void nakedPairInvalidTest() {
        Field f1 = createField(0, 0, 0);
        f1.getLegalEntries().addAll(Arrays.asList(2, 5));

        Field f2 = createField(1, 0, 0);
        f2.getLegalEntries().addAll(Arrays.asList(2, 6));

        Field other = createField(2, 0, 0);
        other.getLegalEntries().addAll(Arrays.asList(2, 5, 7));

        connect(f1, f2);
        connect(f1, other);

        solver.nakedPair(f1, false);

        assertTrue(other.getLegalEntries().contains(2));
        assertTrue(other.getLegalEntries().contains(5));
    }

    @Test
    void removeValueFromNeighboursTest() {
        Field f1 = createField(0, 0, 5);

        Field f2 = createField(1, 0, 0);
        f2.getLegalEntries().addAll(Arrays.asList(1,2,5));

        connect(f1, f2);

        f1.removeValueFromLegalEntriesOfNeighbours();

        assertFalse(f2.getLegalEntries().contains(5));
    }
}
