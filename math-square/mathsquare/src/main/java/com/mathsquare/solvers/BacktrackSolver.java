package com.mathsquare.solvers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mathsquare.objects.Board;
import com.mathsquare.solvers.Calculators.Calculator;
import com.mathsquare.solvers.Calculators.DmasCalculator;
import com.mathsquare.solvers.Calculators.NaturalCalculator;
import com.mathsquare.ui.DisplayBoard;

public abstract class BacktrackSolver extends Solver {

    protected int tick = 0;
    protected int ticksPerUpdate = 100000;

    protected List<Byte> boardNumbers;
    protected LinkedList<List<Byte>> boardsToTest;
    protected List<Byte> options;
    protected LinkedList<List<Byte>> optionsToTest;
    protected Calculator calculator;

    protected boolean useOrderOfOperations;

    public BacktrackSolver(Board board, DisplayBoard displayBoard) {
        this(board, displayBoard, false);
    }

    public BacktrackSolver(Board board, DisplayBoard displayBoard, boolean useOrderOfOperations) {
        super(board, displayBoard);
        this.useOrderOfOperations = useOrderOfOperations;
        calculator = initializeCalculator();
    }

    public Calculator initializeCalculator() {
        if (useOrderOfOperations) {
            return new DmasCalculator(board);
        } else {
            return new NaturalCalculator(board);
        }
    }
    
    @Override
    public void beginSolving() {
        IS_SOLVING = true;
        List<Byte> possibleNumbers = new ArrayList<>();
        for (byte k = 1; k <= boardlength; k++) {
            possibleNumbers.add(k);
        }

        this.boardNumbers = new ArrayList<>();
        this.boardsToTest = new LinkedList<>();
        this.options = possibleNumbers;
        this.optionsToTest = new LinkedList<>();        
    }

    protected boolean isBoardSolved(List<Byte> boardNumbers) {
        return boardNumbers.size() == boardlength;
    }

    protected boolean isBoardValid(List<Byte> boardNumbers, int n) {
        if (n < width - 1) {
            return true;
        }

        int row = n / width;
        int column = n % width;

        // Check row and column
        return calculator.isRowValid(boardNumbers, (byte)row) && calculator.isColumnValid(boardNumbers, (byte)column);
    }
}
