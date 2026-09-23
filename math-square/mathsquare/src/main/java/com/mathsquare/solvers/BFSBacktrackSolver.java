package com.mathsquare.solvers;

import com.mathsquare.ui.DisplayBoard;

public class BFSBacktrackSolver extends BacktrackSolver {

    public BFSBacktrackSolver(DisplayBoard displayBoard, boolean useOrderOfOperations) {
        super(displayBoard, useOrderOfOperations);
    }

    public BFSBacktrackSolver(DisplayBoard displayBoard, boolean useOrderOfOperations, int ticksPerUpdate) {
        super(displayBoard, useOrderOfOperations, ticksPerUpdate);
    }

    protected void saveBoardAndOptions(byte[] currentBoard, long currentOptions) {
        boardsToTest.add(currentBoard);
        optionsToTest.add(currentOptions);
    }
}
