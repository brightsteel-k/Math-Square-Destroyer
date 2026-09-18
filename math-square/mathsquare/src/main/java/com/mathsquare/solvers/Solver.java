package com.mathsquare.solvers;

import com.mathsquare.Operation;
import com.mathsquare.objects.Board;
import com.mathsquare.solvers.Calculators.Calculator;
import com.mathsquare.ui.DisplayBoard;

import java.util.List;

public abstract class Solver {
    public static boolean IS_SOLVING = false;

    protected byte width;
    protected byte height;
    protected byte boardlength;
    protected Operation[][] operationRows;
    protected Operation[][] operationColumns;
    protected int[] targetRows;
    protected int[] targetColumns;

    protected Board board;
    protected DisplayBoard displayBoard;

    public Solver(DisplayBoard displayBoard) {
        this.displayBoard = displayBoard;
    }

    public void loadBoard(Board board) {
        this.width = board.getWidth();
        this.height = board.getHeight();
        this.boardlength = (byte)(width * height);
        this.operationRows = board.getOperationRows();
        this.operationColumns = board.getOperationColumns();
        this.targetRows = board.getTargetRows();
        this.targetColumns = board.getTargetColumns();
        this.board = board;
    }

    public abstract List<Byte> solveBoard();

    public abstract List<Byte> getBoardNumbers();

    public DisplayBoard getDisplayBoard() { return displayBoard; }
    public abstract Calculator getCalculator();
}