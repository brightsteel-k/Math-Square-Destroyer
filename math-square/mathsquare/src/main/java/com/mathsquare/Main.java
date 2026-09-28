package com.mathsquare;

import com.mathsquare.objects.Board;
import com.mathsquare.solvers.PredictiveDFSSolver;
import com.mathsquare.solvers.SelectiveDFSSolver;
import com.mathsquare.solvers.Solver;
import com.mathsquare.ui.ConsoleBoard;
import com.mathsquare.util.DataManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static String BOARD_TO_SOLVE ="6x6.json";

    public static void main(String[] args) {
        TestSolver(true);
        /*int count = 0;
        int size = 25;

        Board board = DataManager.deserializeBoard("math-square/mathsquare/data/" + BOARD_TO_SOLVE);
        Calculator calc = new NaturalCalculator(board); // calc is short for calculator
        Operation[] line = board.getOperationRows()[3];

        Timer timer = new Timer();
        timer.startTimer();
        for (byte a = 0; a < size; a++) {
            for (byte b = 0; b < size; b++) {
                if (a == b) { continue;}
                for (byte c = 0; c < size; c++) {
                    if (a == c || b == c) { continue;}
                    for (byte d = 0; d < size; d++) {
                        if (a == d || b == d || c == d) { continue;}
                        for (byte e = 0; e < size; e++) {
                            if (a == e || b == e || c == e || d == e) { continue;}
                            // if (calc.calculateLine(new byte[]{a, b, c, d, e}, line) == 20) {
                            if (a - b - c + d + e == 20) {
                                count++;
                            }
                        }
                    }
                }
            }
        }

        long time = timer.stopTimer();
        System.out.println("Number of solutions: " + count);
        System.out.println(Timer.printTime(time));*/
    }


    public static void analyzeSolutionCounts() {
        List<Integer> counts = new ArrayList<>();

        System.out.println("====== ANALYSIS ======");
        System.out.println(" p    | n    ");
        System.out.println("------|------");
        for (int p = -8; p <= 560; p++) {
            int count = countSolutions(p, false);
            if (count == 0) { continue; }
            String pstr = " " + p + (p < 0 ? "   " : "    ");
            System.out.println(pstr + "| " + count);
            counts.add(count);
        }

        System.out.println("\nMax: " + Collections.max(counts));
        System.out.println("Min: " + Collections.min(counts));
        System.out.println("Mean: "
                + counts.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0));
    }
    public static int countSolutions(int p, boolean list) {
        int count = 0;

        for (int a = 1; a <= 9; a++) {
            for (int b = 1; b <= 9; b++) {
                if (a==b) { continue; }
                for (int c = 1; c <= 9; c++) {
                    if (a==c || b==c) { continue; }
                    if (a - b / c == p && (double)(a - b) % (double)c == 0) {
                        if (list) {
                            System.out.println(a + " * " + b + " / " + c + " = " + p);
                        }
                        count++;
                    }
                }
            }
        }
        return count;
    }


    public static void TestSolver(boolean useBoard) {
        Board board = DataManager.deserializeBoard("math-square/mathsquare/data/" + BOARD_TO_SOLVE);
        ConsoleBoard displayBoard = new ConsoleBoard();
        Solver solver = new SelectiveDFSSolver(displayBoard, false, 100000000);
        BoardTester tester = new BoardTester(solver);
        if (useBoard) {
            tester.loadBoard(board);
        }
        tester.runSolverTests(4, 4, useBoard ? 1 : 10, !useBoard);
    }

    public static void RunSolver() {
        Board board = DataManager.deserializeBoard("math-square/mathsquare/data/" + BOARD_TO_SOLVE);
        ConsoleBoard displayBoard = new ConsoleBoard();
        Solver solver = new SelectiveDFSSolver(displayBoard, false, 100000000);
        solver.loadBoard(board);
        solver.solveBoard();
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
