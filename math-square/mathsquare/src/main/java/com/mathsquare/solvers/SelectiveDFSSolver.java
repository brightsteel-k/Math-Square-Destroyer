package com.mathsquare.solvers;

import com.mathsquare.Operation;
import com.mathsquare.objects.Board;
import com.mathsquare.ui.DisplayBoard;
import com.mathsquare.util.Timer;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

public class SelectiveDFSSolver extends PredictiveDFSSolver {
    public int[] selectionOrder;
    protected byte[] lineNumbers;
    protected Operation[] lineOperations;
    int lineTarget;

    public SelectiveDFSSolver(DisplayBoard displayBoard, boolean useOrderOfOperations) {
        super(displayBoard, useOrderOfOperations);
    }

    public SelectiveDFSSolver(DisplayBoard displayBoard, boolean useOrderOfOperations, int ticksPerUpdate) {
        super(displayBoard, useOrderOfOperations, ticksPerUpdate);
    }

    protected void initializeSelectionOrder() {
        Timer timer = new Timer();
        timer.startTimer();

        // Calculate scores for each column and row
        int[] lineScores = new int[width + height];
        Operation[][] operationCols = board.getOperationColumns();
        int[] targetCols = board.getTargetColumns();
        for (int x = 0; x < width; x++) {
            lineScores[x] = countSolutionsForLine(operationCols[x], targetCols[x]);
            System.out.println("Found " + lineScores[x] + " solutions for Column " + x);
        }
        Operation[][] operationRows = board.getOperationRows();
        int[] targetRows = board.getTargetRows();
        for (int y = 0; y < height; y++) {
            lineScores[width + y] = countSolutionsForLine(operationRows[y], targetRows[y]);
            System.out.println("Found " + lineScores[width + y] + " solutions for Row " + y);
        }

        // Sort columns and row by score
        Integer[] lineIndices = IntStream.range(0, width + height).boxed().toArray(Integer[]::new);
        Arrays.sort(lineIndices, Comparator.comparingInt((idx) -> lineScores[idx]));

        // Get indices from columns and rows
        selectionOrder = new int[boardlength];
        Arrays.fill(selectionOrder, -1);
        for (int line_idx : lineIndices) {
            if (line_idx < width) {
                // Add column
                for (int y = 0; y < height; y++) {
                    tryAddIdxToOrder(y * width + line_idx);
                }
            } else {
                // Add row
                for (int x = 0; x < width; x++) {
                    tryAddIdxToOrder((line_idx - width) * width + x);
                }
            }
        }

        StringBuilder out = new StringBuilder("Selection Order: [");
        for (int i = 0; i < width + height; i++) {
            if (i != 0) {
                out.append(", ");
            }
            out.append(lineIndices[i] < width ? "Column " : "Row ");
            out.append(lineIndices[i] % width);
        }
        out.append("]");
        System.out.println(out);
        System.out.println(Timer.printTime(timer.stopTimer()));
    }

    protected void tryAddIdxToOrder(int idx) {
        for (int i = 0; i < boardlength; i++) {
            if (selectionOrder[i] == -1) {
                selectionOrder[i] = idx;
                return;
            } else if (selectionOrder[i] == idx) {
                return;
            }
        }
    }

    protected int countSolutionsForLine(Operation[] operations, int target) {
        lineOperations = operations;
        lineNumbers = new byte[lineOperations.length + 1];
        lineTarget = target;
        return recursiveCountSolutions(0);
    }

    protected int recursiveCountSolutions(int index) {
        if (index == lineNumbers.length) {
            return calculator.calculateLine(lineNumbers, lineOperations) == lineTarget ? 1 : 0;
        }

        int total = 0;
        for (byte x = 0; x < boardlength; x++) {
            lineNumbers[index] = x;
            total += recursiveCountSolutions(index + 1);
        }
        return total;
    }

    @Override
    public void loadBoard(Board board) {
        super.loadBoard(board);
        initializeSelectionOrder();
    }

    @Override
    protected int getNextSlotIdx() {
        int n_idx = boardNumbers[0];
        return selectionOrder[n_idx];
    }
}
