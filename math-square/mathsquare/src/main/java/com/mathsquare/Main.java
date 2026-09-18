package com.mathsquare;

import com.mathsquare.objects.Board;
import com.mathsquare.solvers.PredictiveDFSSolver;
import com.mathsquare.solvers.Solver;
import com.mathsquare.ui.ConsoleBoard;
import com.mathsquare.util.DataManager;

import java.io.IOException;

public class Main {
    public static String BOARD_TO_SOLVE ="5x5.json";

    public static void main(String[] args) {
        Board board = DataManager.deserializeBoard("math-square/mathsquare/data/" + BOARD_TO_SOLVE);
        ConsoleBoard displayBoard = new ConsoleBoard();
        Solver solver = new PredictiveDFSSolver(displayBoard, false, 10000000);
        solver.loadBoard(board);
        solver.solveBoard();
    }

    public static void TestSolver() {
        ConsoleBoard displayBoard = new ConsoleBoard();
        Solver solver = new PredictiveDFSSolver(displayBoard, false);
        BoardTester tester = new BoardTester(solver);
        tester.runSolverTests(4, 4, 10, true);
    }


    // - - - - - - - - - - - - - - - - - - SOLVING THE BDAY CARD CHALLENGE - - - - - - - - - - - - - - - - - - \\ 
    public static void SolveMultipleBoards() {
        String[] BOARDS = { "adms", "adsm", "amds", "amsd", "asdm", "asmd", "dams", "dasm", "dmas", "dmsa", "dsam", 
            "dsma", "mads", "masd", "mdas", "mdsa", "msad", "msda", "sadm", "samd", "sdam", "sdma", "smad", "smda"};
        for (int i = 0; i < 24; i++) {
            System.out.println("SOLVING BOARD: " + BOARDS[i]);

            Board board = DataManager.deserializeBoard("math-square/mathsquare/data/bday-card/" + BOARDS[i] + ".json");
            ConsoleBoard displayBoard = new ConsoleBoard();
            Solver solver = new PredictiveDFSSolver(displayBoard, true);
            solver.solveBoard();

            System.out.println("Press Enter to Continue...");
            // Wait until enter has been pressed
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
