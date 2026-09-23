package com.mathsquare.solvers;

import com.mathsquare.objects.Board;
import com.mathsquare.solvers.Calculators.Calculator;
import com.mathsquare.solvers.Calculators.DmasCalculator;
import com.mathsquare.solvers.Calculators.NaturalCalculator;
import com.mathsquare.ui.DisplayBoard;

import java.util.Arrays;
import java.util.LinkedList;

public abstract class BacktrackSolver extends Solver {

    protected int tick = 0;
    protected int ticksPerUpdate = 100000;

    // Formatted as [solvedLength, boardNumbers...]
    //    ex. [5, 4, 6, 2, 3, 9, 0, 0, 0, 0]
    protected byte[] boardNumbers;
    protected LinkedList<byte[]> boardsToTest;
    // Formatted as 0b<0 or 1 for each option from boardlength to one>
    //    ex. 0b000011101 to represent [5, 4, 3, 1]
    protected long options;
    protected LinkedList<Long> optionsToTest;
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
    public byte[] solveBoard() {
        IS_SOLVING = true;

        this.boardNumbers = new byte[boardlength + 1];
        this.boardsToTest = new LinkedList<>();
        this.options = ((long)1 << boardlength) - 1;
        this.optionsToTest = new LinkedList<>();

        // Begin Depth-First Search
        while (IS_SOLVING) {
            if (searchBoard()) {
                IS_SOLVING = false;
                byte[] solution = Arrays.copyOfRange(boardNumbers, 1, boardNumbers[0]+1);
                displayBoard.onSolved(solution);
                return solution;
            } else if (++tick == ticksPerUpdate) {
                displayBoard.updateNumbers(boardNumbers);
                tick = 0;
            }
        }
        return new byte[0];
    }

    protected boolean searchBoard() {
        if (isBoardSolved(boardNumbers)) {
            return true;
        } else {
            // Calculate new boards
            byte[] currentBoard;

            // Iterate over all possible options (set bits)
            long bitset = options;
            while (bitset != 0) {
                long t = bitset & -bitset;
                byte opt = (byte)(Long.numberOfTrailingZeros(bitset) + 1);

                int nextIdx = getNextSlotIdx();
                currentBoard = addOptionToBoard(opt, nextIdx);
                if (isBoardValid(currentBoard, nextIdx)) {
                    saveBoardAndOptions(currentBoard, options & ~((long)1 << (opt - 1)));
                }

                bitset ^= t;
            }

            // Solve next board, if there is one
            if (boardsToTest.isEmpty()) {
                boardNumbers = new byte[0];
                return true;
            } else {
                boardNumbers = boardsToTest.poll();
                options = optionsToTest.poll();
                return false;
            }
        }
    }

    protected abstract void saveBoardAndOptions(byte[] currentBoard, long currentOptions);

    protected int getNextSlotIdx() {
        return boardNumbers[0];
    }

    protected byte[] addOptionToBoard(byte nextOpt, int idx) {
        byte[] newBoard = Arrays.copyOf(boardNumbers, boardlength + 1);
        newBoard[idx+1] = nextOpt;
        newBoard[0]++;
        return newBoard;
    }

    protected boolean isBoardSolved(byte[] boardNumbers) {
        return boardNumbers[0] == boardlength;
    }

    protected boolean isBoardValid(byte[] boardNumbers, int n) {
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
    public byte[] getBoardNumbers() {
        return boardNumbers;
    }
}
