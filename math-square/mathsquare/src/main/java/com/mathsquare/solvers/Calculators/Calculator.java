package com.mathsquare.solvers.Calculators;

import com.mathsquare.Operation;
import com.mathsquare.objects.Board;

public abstract class Calculator {

    protected static final byte ZERO = (byte)0;
    protected float errorThreshold = 0.00001f;
    protected Board board;

    public Calculator() { loadBoard(Board.EMPTY_BOARD);}

    public Calculator(Board boardIn) {
        loadBoard(boardIn);
    }

    public void loadBoard(Board boardIn) { board = boardIn; }

    public abstract boolean isRowValid(byte[] boardNumbers, int y);

    public abstract boolean isColumnValid(byte[] boardNumbers, int x);

    protected boolean isUnfinishedRowValid(byte[] boardNumbers, int y) {
        return true;
    }

    protected boolean isUnfinishedColumnValid(byte[] boardNumbers, int x) {
        return true;
    }

    /**
     * Calculates the result of an equation given by n lineNumbers separated by n - 1 operations.
     * @param lineNumbers The numbers of the equation.
     * @param operations The operations between each number. Should be one fewer than the numbers.
     * @return the result of the equation, or Integer.MIN_VALUE if the result is not a clean integer.
     */
    public abstract int calculateLine(byte[] lineNumbers, Operation[] operations);

    /**
     * Checks whether the change to slot n on the board makes the board invalid.
     * @param boardNumbers Current board numbers.
     * @param n Slot changed.
     * @return true if the board is valid, false otherwise.
     */
    public boolean isBoardChangeValid(byte[] boardNumbers, int n) {
        int row = n / board.getWidth();
        int column = n % board.getWidth();

        // Check row and column
        return isRowValid(boardNumbers, row) && isColumnValid(boardNumbers, column);
    }

    /**
     * Checks whether the given row is complete.
     * @param boardNumbers Current board numbers.
     * @param y Row.
     * @return true iff the final spot in the row has been filled.
     */
    protected boolean isRowFinished(byte[] boardNumbers, int y) {
        for (int x = 0; x < board.getWidth(); x++) {
            if (getBoardNumber(boardNumbers, x, y) == 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks whether the given column is complete.
     * @param boardNumbers Current board numbers.
     * @param x Column.
     * @return true iff the final spot in the column has been filled.
     */
    protected boolean isColumnFinished(byte[] boardNumbers, int x) {
        for (int y = 0; y < board.getHeight(); y++) {
            if (getBoardNumber(boardNumbers, x, y) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the number at a particular column and row in the given board numbers, or
     * 0 if the position has not been populated yet.
     * @param boardNumbers Current board numbers.
     * @param x Column.
     * @param y Row.
     * @return The number on the board. Valid values are limited to range [0,boardlength],
     *          which must be a byte.
     */
    protected byte getBoardNumber(byte[] boardNumbers, int x, int y) {
        int index = x + (y * board.getWidth());
        return boardNumbers[index + 1];
    }

    /**
     * Checks whether the given float is within a certain threshold of the target int.
     * @param value Value to check the validity of.
     * @param target Target int to validate against.
     * @return True iff value is approximately equal to target.
     */
    protected boolean matches(float value, int target) {
        float error = value - (float)target;
        return error >= 0 ? error < errorThreshold : error * -1 < errorThreshold;
    }
}
