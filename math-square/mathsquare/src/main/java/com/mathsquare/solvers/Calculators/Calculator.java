package com.mathsquare.solvers.Calculators;

import com.mathsquare.Operation;
import com.mathsquare.objects.Board;

import java.util.List;

public abstract class Calculator {

    protected static final byte ZERO = (byte)0;
    protected float errorThreshold = 0.00001f;
    protected Board board;

    public Calculator() { loadBoard(Board.EMPTY_BOARD);}

    public Calculator(Board boardIn) {
        loadBoard(boardIn);
    }

    public void loadBoard(Board boardIn) { board = boardIn; }

    public abstract boolean isRowValid(List<Byte> boardNumbers, byte y);

    public abstract boolean isColumnValid(List<Byte> boardNumbers, byte x);

    /**
     * Calculates the result of an equation given by n lineNumbers separated by n - 1 operations.
     * @param lineNumbers The numbers of the equation.
     * @param operations The operations between each number. Should be one fewer than the numbers.
     * @return the result of the equation, or Integer.MIN_VALUE if the result is not a clean integer.
     */
    public abstract int calculateLine(List<Byte> lineNumbers, Operation[] operations);

    /**
     * Checks whether the change to slot n on the board makes the board invalid.
     * @param boardNumbers Current board numbers.
     * @param n Slot changed.
     * @return true if the board is valid, false otherwise.
     */
    public boolean isBoardChangeValid(List<Byte> boardNumbers, byte n) {
        int row = n / board.getWidth();
        int column = n % board.getWidth();

        // Check row and column
        return isRowValid(boardNumbers, (byte)row) && isColumnValid(boardNumbers, (byte)column);
    }

    /**
     * Checks whether the given row is complete.
     * @param boardNumbers Current board numbers.
     * @param y Row.
     * @return true iff the final spot in the row has been filled.
     */
    protected boolean isRowFinished(List<Byte> boardNumbers, byte y) {
        int lastIndex = (y + 1) * board.getWidth() - 1;
        return lastIndex < boardNumbers.size();
    }
    
    /**
     * Checks whether the given column is complete.
     * @param boardNumbers Current board numbers.
     * @param x Column.
     * @return true iff the final spot in the column has been filled.
     */
    protected boolean isColumnFinished(List<Byte> boardNumbers, byte x) {
        int lastIndex = board.getBoardLength() - board.getWidth() + x;
        return lastIndex < boardNumbers.size();
    }

    /**
     * Returns the number at a particular column and row in the given board numbers, or
     * -1 if the position has not been populated yet.
     * @param boardNumbers Current board numbers.
     * @param x Column.
     * @param y Row.
     * @return The number on the board. Valid values are limited to range [0,boardlength],
     *          which must be a byte.
     */
    protected byte getBoardNumber(List<Byte> boardNumbers, byte x, byte y) {
        int index = x + (y * board.getWidth());
        if (index < boardNumbers.size()) {
            return boardNumbers.get(index);
        }

        // Signal no number exists there yet
        return -1;
    }

    /**
     * Returns the index immediately before the given index.
     * @param x The given index.
     * @return x - 1.
     */
    protected byte prev(byte x) {
        return (byte)(x - 1);
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
