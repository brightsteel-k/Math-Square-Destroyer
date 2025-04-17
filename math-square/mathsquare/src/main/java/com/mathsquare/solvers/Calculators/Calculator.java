package com.mathsquare.solvers.Calculators;

import java.util.List;

import com.mathsquare.objects.Board;

public abstract class Calculator {

    protected static final byte ZERO = (byte)0;
    protected Board board;

    public Calculator(Board boardIn) {
        board = boardIn;
    }

    public abstract boolean isRowValid(List<Byte> boardNumbers, byte y);

    public abstract boolean isColumnValid(List<Byte> boardNumbers, byte x);

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
}
