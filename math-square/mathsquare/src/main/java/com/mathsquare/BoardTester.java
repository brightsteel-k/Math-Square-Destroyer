package com.mathsquare;

import com.mathsquare.objects.Board;
import com.mathsquare.solvers.Calculators.Calculator;
import com.mathsquare.solvers.Solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BoardTester {
    protected Random random;
    protected Calculator calculator;
    protected Solver solver;
    protected Board currentBoard;
    protected List<Byte> currentSolution;

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
        List<Byte> boardNumbers = boardIntegers.stream()
                .map(Integer::byteValue)
                .collect(Collectors.toList());
        List<Byte> lineNumbers;

        Operation[][] columnOperations = new Operation[width][];
        int[] columnTargets = new int[width];
        for (int x = 0; x < width; x++) {
            // Generate random operations
            columnOperations[x] = randomOperationLine(height - 1);
            int finalX = x;

            // Calculate target from operations
            lineNumbers = IntStream.range(0, height)
                    .map(i -> i * width + finalX)
                    .mapToObj(boardNumbers::get)
                    .collect(Collectors.toList());
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
            lineNumbers = IntStream.range(y * width, y * width + width)
                    .mapToObj(boardNumbers::get)
                    .collect(Collectors.toList());
            rowTargets[y] = calculator.calculateLine(lineNumbers, rowOperations[y]);

            if (rowTargets[y] == Integer.MIN_VALUE) {
                // Equation product was a fraction, so regenerate this row
                y--;
            }
        }

        currentBoard = new Board(width, height, rowOperations, columnOperations, rowTargets, columnTargets);
        currentSolution = boardNumbers;
    }

    public void loadBoard(Board board, int[] solution) {
        currentBoard = board;
        currentSolution = new ArrayList<>();
        for (int j : solution) {
            currentSolution.add((byte) j);
        }
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

    public List<Byte> getCurrentSolution() {
        return currentSolution;
    }

    public void runSolverTests(int width, int height, int numTests, boolean generateBoard) {
        int successes = 0;
        int failures = 0;
        for (int i = 0; i < numTests; i++) {
            System.out.println("========== TEST " + i + " ==========");
            if (generateBoard) { generateAndLoadBoard(width, height); }
            solver.loadBoard(currentBoard);
            List<Byte> solverSolution = solver.solveBoard();

            if (solverSolution.isEmpty()) {
                System.out.println("Test failed. Board: ");
                System.out.println(String.join("\n", currentBoard.getPattern()));
                System.out.println("\nSolution: ");
                System.out.println(getSolutionDisplay(currentSolution, width));
                failures++;
            } else if (!solverSolution.equals(currentSolution)) {
                System.out.println("Test failed. Solution: ");
                System.out.println(getSolutionDisplay(currentSolution, width));
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

    public String getSolutionDisplay(List<Byte> solution, int width) {
        StringBuilder displayString = new StringBuilder();
        for (int i = 0; i < solution.size(); i++) {
            displayString.append(solution.get(i));
            displayString.append(" ");
            if (i % width == width - 1) { displayString.append("\n"); }
        }
        return displayString.toString();
    }
}
