package com.mathsquare.solvers;

import com.mathsquare.ui.DisplayBoard;

public class DFSBacktrackSolver extends BacktrackSolver {

    public DFSBacktrackSolver(DisplayBoard displayBoard, boolean useOrderOfOperations) {
        super(displayBoard, useOrderOfOperations);
    }

    public DFSBacktrackSolver(DisplayBoard displayBoard, boolean useOrderOfOperations, int ticksPerUpdate) {
        super(displayBoard, useOrderOfOperations, ticksPerUpdate);
    }

    @Override
    protected void saveBoardAndOptions(byte[] currentBoard, long currentOptions) {
        boardsToTest.addFirst(currentBoard);
        optionsToTest.addFirst(currentOptions);
    }
}
