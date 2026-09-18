package com.mathsquare.solvers;

import com.mathsquare.ui.DisplayBoard;

import java.util.ArrayList;
import java.util.List;

public class DFSBacktrackSolver extends BacktrackSolver {

    public DFSBacktrackSolver(DisplayBoard displayBoard, boolean useOrderOfOperations) {
        super(displayBoard, useOrderOfOperations);
    }

    public DFSBacktrackSolver(DisplayBoard displayBoard, boolean useOrderOfOperations, int ticksPerUpdate) {
        super(displayBoard, useOrderOfOperations, ticksPerUpdate);
    }
    
    @Override
    public List<Byte> solveBoard() {
        super.solveBoard();

        // Begin Depth-First Search
        while (IS_SOLVING) {
            if (solveBoardDepthFirst()) {
                IS_SOLVING = false;
                displayBoard.onSolved(boardNumbers);
                return boardNumbers;
            } else if (++tick == ticksPerUpdate) {
                displayBoard.updateNumbers(boardNumbers);
                tick = 0;
            }
        }
        return new ArrayList<>();
    }

    protected boolean solveBoardDepthFirst() {
        if (isBoardSolved(boardNumbers)) {
            return true;
        } else {
            // Calculate new boards
            List<Byte> currentBoard;
            for (int i = 0; i < options.size(); i++) {
                currentBoard = new ArrayList<>(boardNumbers);
                currentBoard.add(options.get(i));

                if (isBoardValid(currentBoard, currentBoard.size() - 1)) {
                    boardsToTest.addFirst(currentBoard);
                    List<Byte> opts = new ArrayList<>(options);
                    opts.remove(options.get(i));
                    optionsToTest.addFirst(opts);
                }
            }

            // Solve next board, if there is one
            if (boardsToTest.size() == 0) {
                boardNumbers = new ArrayList<>();
                return true;
            } else {
                boardNumbers = boardsToTest.poll();
                options = optionsToTest.poll();
                return false;
            }
        }
    }
}
