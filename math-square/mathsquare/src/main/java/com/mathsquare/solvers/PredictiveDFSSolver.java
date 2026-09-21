package com.mathsquare.solvers;

import com.mathsquare.solvers.Calculators.Calculator;
import com.mathsquare.solvers.Calculators.IPredictiveCalculator;
import com.mathsquare.solvers.Calculators.PredictiveDmasCalculator;
import com.mathsquare.solvers.Calculators.PredictiveNaturalCalculator;
import com.mathsquare.ui.DisplayBoard;

import java.util.ArrayList;
import java.util.List;

public class PredictiveDFSSolver extends DFSBacktrackSolver {

    protected IPredictiveCalculator predictiveCalculator;

    public PredictiveDFSSolver(DisplayBoard displayBoard, boolean useOrderOfOperations) {
        super(displayBoard, useOrderOfOperations);
    }
    
    public PredictiveDFSSolver(DisplayBoard displayBoard, boolean useOrderOfOperations, int ticksPerUpdate) {
        super(displayBoard, useOrderOfOperations, ticksPerUpdate);
    }

    @Override
    public Calculator initializeCalculator() {
        Calculator tempCalculator = useOrderOfOperations ? new PredictiveDmasCalculator(board) : new PredictiveNaturalCalculator(board);
        predictiveCalculator = (IPredictiveCalculator)tempCalculator;
        return tempCalculator;
    }

    @Override
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

                // Load predictive calculator
                predictiveCalculator.loadOptions(options, opt);

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
