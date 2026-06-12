package com.sudoku.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.smartcardio.TerminalFactory;

import com.sudoku.view.TerminalView;

public class algoAnalyser {
    public static void algoAnalysisManager(){
        int counter = 0;
        algoXSolver algoXManager = new algoXSolver();
        Solver solver = new Solver();
        long algoXTime = 0;
        long solverTime = 0;
        SudokuBoard sudokuBoard = new SudokuBoard(9);
        SudokuBoard sudokuBoard2 = new SudokuBoard(9);
        try (BufferedReader br = new BufferedReader(new FileReader("lwjgl-sudoku\\diabolical.txt"))) {
            String line;
            while ((line = br.readLine()) != null && counter < 1){
                counter++;
                String[] stringArray = line.split(" ");
                String sudokuLine = stringArray[1];
                int[][] board = new int[9][9];

                for (int i = 0; i < 81; i++) {
                    board[i / 9][i % 9] = sudokuLine.charAt(i) - '0';
                }
                sudokuBoard.readIntoBoard(board);
                long startTime = System.nanoTime();
                algoXManager.algoXManager(sudokuBoard);
                if (algoXManager.algoXIsUnique(sudokuBoard)){
                    System.out.println("The sudoku is unique");
                }
                else {
                    System.out.println("The sudoku isn't unique");
                }
                if (!sudokuIsSolved(sudokuBoard)){
                    System.out.println("AlgoX couldn't solve");
                }
                else {
                    System.out.println("AlgoX could solve");
                }
                TerminalView terminalView = new TerminalView(sudokuBoard);
                terminalView.printBoard();
                long endTime = System.nanoTime();
                algoXTime+= endTime-startTime;
                sudokuBoard.clear();

                sudokuBoard2.readIntoBoard(board);
                startTime = System.nanoTime();
                solver.solves(sudokuBoard2);
                if (!sudokuIsSolved(sudokuBoard2)){
                    System.out.println("Solver couldn't solve");
                }
                else {
                    System.out.println("Solver could solve");
                }
                endTime = System.nanoTime();
                terminalView = new TerminalView(sudokuBoard2);
                terminalView.printBoard();
                solverTime+= endTime-startTime;
                
                sudokuBoard2.clear();
            }
        System.out.println("The algoX average time is: ");
        System.out.println(algoXTime/counter);
        System.out.println("The solver average time is: ");
        System.out.println(solverTime/counter);
        } catch (IOException e) {
            System.out.println("Error in reading file");
        }
    }
    public static boolean sudokuIsSolved(SudokuBoard sudokuBoard){
        for (Field field: sudokuBoard.getFields()){
            if (field.getValue() == 0){
                return false;
            }
        }
        return true;
    }
}
