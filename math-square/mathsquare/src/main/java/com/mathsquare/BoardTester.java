package com.mathsquare;

import com.mathsquare.objects.Board;
import com.mathsquare.solvers.Calculators.Calculator;
import com.mathsquare.solvers.Solver;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BoardTester {
    protected Random random;
    protected Calculator calculator;
    protected Solver solver;
    protected Board currentBoard;

    public BoardTester(Calculator calculator) {
        this.solver = null;
        this.calculator = calculator;
        random = new Random();
    }

    public BoardTester(Solver solver) {
        this.solver = solver;
        this.calculator = solver.getCalculator();
        random = new Random();
    }

    public void generateAndLoadBoard(int width, int height) {
        List<Integer> boardIntegers = IntStream.rangeClosed(1, width * height).boxed().collect(Collectors.toList());
        Collections.shuffle(boardIntegers);
        byte[] boardNumbers = toByteArray(boardIntegers.stream().mapToInt(Integer::intValue).toArray());
        byte[] lineNumbers;

        Operation[][] columnOperations = new Operation[width][];
        int[] columnTargets = new int[width];
        for (int x = 0; x < width; x++) {
            // Generate random operations
            columnOperations[x] = randomOperationLine(height - 1);

            // Calculate target from operations
            lineNumbers = sliceColumn(boardNumbers, width, height, x);
            columnTargets[x] = calculator.calculateLine(lineNumbers, columnOperations[x]);

            if (columnTargets[x] == Integer.MIN_VALUE) {
                // Equation product was a fraction, so regenerate this column
                x--;
            }
        }
        Operation[][] rowOperations = new Operation[height][];
        int[] rowTargets = new int[height];
        for (int y = 0; y < height; y++) {
            // Generate random operations
            rowOperations[y] = randomOperationLine(width - 1);

            // Calculate target from operations
            lineNumbers = sliceRow(boardNumbers, width, height, y);
            rowTargets[y] = calculator.calculateLine(lineNumbers, rowOperations[y]);

            if (rowTargets[y] == Integer.MIN_VALUE) {
                // Equation product was a fraction, so regenerate this row
                y--;
            }
        }

        currentBoard = new Board(width, height, rowOperations, columnOperations, rowTargets, columnTargets);
        currentBoard.setSolution(boardNumbers);
    }

    public void loadBoard(Board board) {
        currentBoard = board;
        calculator.loadBoard(board);
    }

    protected Operation[] randomOperationLine(int length) {
        Operation[] operations = new Operation[length];
        for (int i = 0; i < length; i++) {
            operations[i] = randomOperation();
        }
        return operations;
    }

    protected Operation randomOperation() {
        return Operation.values()[random.nextInt(4)];
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public void debugCurrentBoard() {
        currentBoard.debugPrint();
    }

    public void runSolverTests(int width, int height, int numTests, boolean generateBoard) {
        int successes = 0;
        int failures = 0;
        for (int i = 0; i < numTests; i++) {
            System.out.println("========== TEST " + i + " ==========");
            if (generateBoard) { generateAndLoadBoard(width, height); }
            solver.loadBoard(currentBoard);
            byte[] solverSolution = solver.solveBoard();

            if (solverSolution.length == 0) {
                System.out.println("Test failed. Board: ");
                System.out.println(String.join("\n", currentBoard.getPattern()));
                System.out.println("\nSolution: ");
                System.out.println(getSolutionDisplay(currentBoard.getSolution(), width));
                failures++;
            } else if (!isBoardSolution(solverSolution)) {
                System.out.println("Test failed. Solution: ");
                System.out.println(getSolutionDisplay(currentBoard.getSolution(), width));
                failures++;
            } else {
                System.out.println("Test succeeded.");
                successes++;
            }
        }

        System.out.println("========== FINAL REVIEW ==========");
        System.out.println("- Successful runs: " + successes + "/" + numTests);
        System.out.println("- Failed runs: : " + failures + "/" + numTests);
    }

    public String getSolutionDisplay(byte[] solution, int width) {
        StringBuilder displayString = new StringBuilder();
        for (int i = 0; i < solution.length; i++) {
            displayString.append(solution[i]);
            displayString.append(" ");
            if (i % width == width - 1) { displayString.append("\n"); }
        }
        return displayString.toString();
    }

    public boolean isBoardSolution(byte[] solution) {
        int width = currentBoard.getWidth();
        int height = currentBoard.getHeight();

        // Validate rows
        Operation[][] operationRows = currentBoard.getOperationRows();
        int[] targetRows = currentBoard.getTargetRows();
        for (int y = 0; y < height; y++) {
            Operation[] ops = operationRows[y];
            int product = calculator.calculateLine(sliceRow(solution, width, height, y), ops);
            if (product == Integer.MIN_VALUE || product != targetRows[y]) {
                return false;
            }
        }

        // Validate columns
        Operation[][] operationCols = currentBoard.getOperationColumns();
        int[] targetCols = currentBoard.getTargetColumns();
        for (int x = 0; x < width; x++) {
            Operation[] ops = operationCols[x];
            int product = calculator.calculateLine(sliceColumn(solution, width, height, x), ops);
            if (product == Integer.MIN_VALUE || product != targetCols[x]) {
                return false;
            }
        }

        return true;
    }

    public static byte[] sliceRow(byte[] arr, int width, int height, int y) {
        return toByteArray(IntStream.range(y * width, y * width + width)
                .mapToObj(i -> arr[i])
                .mapToInt(Byte::intValue)
                .toArray());
    }

    public static byte[] sliceColumn(byte[] arr, int width, int height, int x) {
        return toByteArray(IntStream.range(0, height)
                .map(i -> i * width + x)
                .mapToObj(i -> arr[i])
                .mapToInt(Byte::intValue)
                .toArray());
    }

    public static byte[] toByteArray(int[] arr) {
        byte[] newArr = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) { newArr[i] = (byte)arr[i]; }
        return newArr;
    }
}
