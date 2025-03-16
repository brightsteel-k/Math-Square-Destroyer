package com.mathsquare.solvers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mathsquare.objects.Board;
import com.mathsquare.solvers.Calculators.Calculator;
import com.mathsquare.solvers.Calculators.DmasCalculator;
import com.mathsquare.solvers.Calculators.NaturalCalculator;
import com.mathsquare.ui.DisplayBoard;

public class BacktrackSolver extends Solver {

    protected int tick = 0;
    protected int ticksPerUpdate = 100000;

    protected List<Integer> boardNumbers;
    protected LinkedList<List<Integer>> boardsToTest;
    protected List<Integer> options;
    protected LinkedList<List<Integer>> optionsToTest;
    protected Calculator calculator;

    protected boolean breadthFirst;

    public BacktrackSolver(Board board, DisplayBoard displayBoard) {
        this(board, displayBoard, false, false);
    }

    public BacktrackSolver(Board board, DisplayBoard displayBoard, boolean breadthFirst) {
        this(board, displayBoard, breadthFirst, false);
    }

    public BacktrackSolver(Board board, DisplayBoard displayBoard, boolean breadthFirst, boolean useOrderOfOperations) {
        super(board, displayBoard);
        this.breadthFirst = breadthFirst;
        if (useOrderOfOperations) {
            calculator = new DmasCalculator(board);
        } else {
            calculator = new NaturalCalculator(board);
        }
    }
    
    @Override
    public void beginSolving() {
        IS_SOLVING = true;
        List<Integer> possibleNumbers = new ArrayList<>();
        for (int k = 1; k <= boardlength; k++) {
            possibleNumbers.add(k);
        }

        this.boardNumbers = new ArrayList<>();
        this.boardsToTest = new LinkedList<>();
        this.options = possibleNumbers;
        this.optionsToTest = new LinkedList<>();

        if (breadthFirst) {
            beginSolvingBreadthFirst();
        } else {
            beginSolvingDepthFirst();
        }
        
    }

    public void beginSolvingDepthFirst() {
        while (IS_SOLVING) {
            if (solveBoardDepthFirst()) {
                IS_SOLVING = false;
                displayBoard.onSolved(boardNumbers);
            } else if (++tick == ticksPerUpdate) {
                displayBoard.updateNumbers(boardNumbers);
                tick = 0;
            }
        }
    }

    public void beginSolvingBreadthFirst() {
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

    private boolean solveBoardDepthFirst() {
        if (isBoardSolved(boardNumbers)) {
            return true;
        } else {
            // Calculate new boards
            List<Integer> currentBoard;
            for (int i = 0; i < options.size(); i++) {
                currentBoard = new ArrayList<>(boardNumbers);
                currentBoard.add(options.get(i));

                if (isBoardValid(currentBoard, currentBoard.size() - 1)) {
                    boardsToTest.addFirst(currentBoard);
                    List<Integer> opts = new ArrayList<>(options);
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

    private boolean solveBoardBreadthFirst() {
        if (isBoardSolved(boardNumbers)) {
            return true;
        } else {
            // Calculate new boards
            List<Integer> currentBoard;
            for (int i = 0; i < options.size(); i++) {
                currentBoard = new ArrayList<>(boardNumbers);
                currentBoard.add(options.get(i));

                if (isBoardValid(currentBoard, currentBoard.size() - 1)) {
                    boardsToTest.add(currentBoard);
                    List<Integer> opts = new ArrayList<>(options);
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

    private boolean isBoardSolved(List<Integer> boardNumbers) {
        return boardNumbers.size() == boardlength;
    }

    private boolean isBoardValid(List<Integer> boardNumbers, int n) {
        if (n < width - 1) {
            return true;
        }

        int row = n / width;
        int column = n % width;

        // Check row and column
        return calculator.isRowValid(boardNumbers, (byte)row) && calculator.isColumnValid(boardNumbers, (byte)column);
    }

    public void setDepthFirst() {
        breadthFirst = false;
    }

    public void setBreadthFirst() {
        breadthFirst = true;
    }
}
