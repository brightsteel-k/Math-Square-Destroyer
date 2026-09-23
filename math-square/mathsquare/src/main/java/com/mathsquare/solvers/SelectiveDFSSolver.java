package com.mathsquare.solvers;

import com.mathsquare.objects.Board;
import com.mathsquare.ui.DisplayBoard;

public class SelectiveDFSSolver extends PredictiveDFSSolver {
    public static int[] selectionOrder;

    public SelectiveDFSSolver(DisplayBoard displayBoard, boolean useOrderOfOperations) {
        super(displayBoard, useOrderOfOperations);
    }

    public SelectiveDFSSolver(DisplayBoard displayBoard, boolean useOrderOfOperations, int ticksPerUpdate) {
        super(displayBoard, useOrderOfOperations, ticksPerUpdate);
    }

    protected void initializeSelectionOrder() {
        // selectionOrder = IntStream.range(0, boardlength).toArray();
        selectionOrder = new int[] { 12, 13, 14, 15, 1, 5, 9, 3, 7, 11, 0, 4, 8, 2, 10, 6 };
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
