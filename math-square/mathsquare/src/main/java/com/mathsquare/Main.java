package com.mathsquare;

import com.mathsquare.objects.Board;
import com.mathsquare.solvers.BacktrackSolver;
import com.mathsquare.solvers.Solver;
import com.mathsquare.ui.ConsoleBoard;
import com.mathsquare.util.DataManager;

public class Main {
    public static String BOARD_TO_SOLVE ="kya-square.json";

    public static void main(String[] args) {
        Board board = DataManager.deserializeBoard("math-square/mathsquare/data/" + BOARD_TO_SOLVE);
        ConsoleBoard displayBoard = new ConsoleBoard(board);
        Solver solver = new BacktrackSolver(board, displayBoard, false, false);
        solver.beginSolving();
    }
}
