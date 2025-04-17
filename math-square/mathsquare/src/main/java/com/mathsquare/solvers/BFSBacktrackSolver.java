package com.mathsquare.solvers;

import java.util.ArrayList;
import java.util.List;

import com.mathsquare.objects.Board;
import com.mathsquare.ui.DisplayBoard;

public class BFSBacktrackSolver extends BacktrackSolver {

    public BFSBacktrackSolver(Board board, DisplayBoard displayBoard) {
        super(board, displayBoard, false);
    }

    public BFSBacktrackSolver(Board board, DisplayBoard displayBoard, boolean useOrderOfOperations) {
        super(board, displayBoard, useOrderOfOperations);
    }
    
    @Override
    public void beginSolving() {
        super.beginSolving();

        // Begin Breadth-First Search
        while (IS_SOLVING) {
            if (solveBoardBreadthFirst()) {
                IS_SOLVING = false;
                displayBoard.onSolved(boardNumbers);
            } else if (++tick == ticksPerUpdate) {
                displayBoard.updateNumbers(boardNumbers);
                tick = 0;
            }
        }
    }

    private boolean solveBoardBreadthFirst() {
        if (isBoardSolved(boardNumbers)) {
            return true;
        } else {
            // Calculate new boards
            List<Byte> currentBoard;
            for (int i = 0; i < options.size(); i++) {
                currentBoard = new ArrayList<>(boardNumbers);
                currentBoard.add(options.get(i));

                if (isBoardValid(currentBoard, currentBoard.size() - 1)) {
                    boardsToTest.add(currentBoard);
                    List<Byte> opts = new ArrayList<>(options);
                    opts.remove(options.get(i));
                    optionsToTest.add(opts);
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
