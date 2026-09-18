package com.mathsquare.solvers;

import com.mathsquare.objects.Board;
import com.mathsquare.solvers.Calculators.Calculator;
import com.mathsquare.solvers.Calculators.DmasCalculator;
import com.mathsquare.solvers.Calculators.NaturalCalculator;
import com.mathsquare.ui.DisplayBoard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class BacktrackSolver extends Solver {

    protected int tick = 0;
    protected int ticksPerUpdate = 100000;

    protected List<Byte> boardNumbers;
    protected LinkedList<List<Byte>> boardsToTest;
    protected List<Byte> options;
    protected LinkedList<List<Byte>> optionsToTest;
    protected Calculator calculator;

    protected boolean useOrderOfOperations;

    public BacktrackSolver(DisplayBoard displayBoard, boolean useOrderOfOperations) {
        this(displayBoard, useOrderOfOperations, 100000);
    }

    public BacktrackSolver(DisplayBoard displayBoard, boolean useOrderOfOperations, int ticksPerUpdate) {
        super(displayBoard);
        this.useOrderOfOperations = useOrderOfOperations;
        this.ticksPerUpdate = ticksPerUpdate;
        this.calculator = initializeCalculator();
    }

    public Calculator initializeCalculator() {
        if (useOrderOfOperations) {
            return new DmasCalculator();
        } else {
            return new NaturalCalculator();
        }
    }

    @Override
    public void loadBoard(Board board) {
        super.loadBoard(board);
        calculator.loadBoard(board);
        displayBoard.loadBoard(board);
    }

    @Override
    public List<Byte> solveBoard() {
        IS_SOLVING = true;
        List<Byte> possibleNumbers = new ArrayList<>();
        for (byte k = 1; k <= boardlength; k++) {
            possibleNumbers.add(k);
        }

        this.boardNumbers = new ArrayList<>();
        this.boardsToTest = new LinkedList<>();
        this.options = possibleNumbers;
        this.optionsToTest = new LinkedList<>();
        return new ArrayList<>();
    }

    protected boolean isBoardSolved(List<Byte> boardNumbers) {
        return boardNumbers.size() == boardlength;
    }

    protected boolean isBoardValid(List<Byte> boardNumbers, int n) {
        if (n < width - 1) {
            return true;
        }

        return calculator.isBoardChangeValid(boardNumbers, (byte)n);
    }

    @Override
    public Calculator getCalculator() {
        return calculator;
    }
    
    @Override
    public List<Byte> getBoardNumbers() {
        return boardNumbers;
    }

}
