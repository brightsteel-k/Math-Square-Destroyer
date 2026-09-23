package com.mathsquare.solvers;

import com.mathsquare.solvers.Calculators.Calculator;
import com.mathsquare.solvers.Calculators.IPredictiveCalculator;
import com.mathsquare.solvers.Calculators.PredictiveDmasCalculator;
import com.mathsquare.solvers.Calculators.PredictiveNaturalCalculator;
import com.mathsquare.ui.DisplayBoard;

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
    protected byte[] addOptionToBoard(byte nextOpt, int idx) {
        predictiveCalculator.loadOptions(options, nextOpt);
        return super.addOptionToBoard(nextOpt, idx);
    }
}
