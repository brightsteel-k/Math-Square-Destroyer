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

            // Iterate over all possible options (set bits)
            long bitset = options;
            while (bitset != 0) {
                long t = bitset & -bitset;
                byte opt = (byte)(Long.numberOfTrailingZeros(bitset) + 1);

                currentBoard = new ArrayList<>(boardNumbers);
                currentBoard.add(opt);
                if (isBoardValid(currentBoard, currentBoard.size() - 1)) {
                    boardsToTest.addFirst(currentBoard);
                    optionsToTest.addFirst(options & ~((long)1 << (opt - 1)));
                }

                bitset ^= t;
            }

            // Solve next board, if there is one
            if (boardsToTest.isEmpty()) {
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
