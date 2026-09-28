package com.mathsquare.solvers;

import com.mathsquare.objects.Board;
import com.mathsquare.solvers.Calculators.Calculator;
import com.mathsquare.ui.DisplayBoard;

public abstract class Solver {
    public static boolean IS_SOLVING = false;

    protected int width;
    protected int height;
    protected int boardlength;

    protected Board board;
    protected DisplayBoard displayBoard;

    public Solver(DisplayBoard displayBoard) {
        this.displayBoard = displayBoard;
    }

    public void loadBoard(Board board) {
        this.width = board.getWidth();
        this.height = board.getHeight();
        this.boardlength = (byte)(width * height);
        this.board = board;
    }

    public abstract byte[] solveBoard();

    public abstract byte[] getBoardNumbers();

    public DisplayBoard getDisplayBoard() { return displayBoard; }
    public abstract Calculator getCalculator();
}