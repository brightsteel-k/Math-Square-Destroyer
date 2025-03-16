package com.mathsquare.solvers;

import com.mathsquare.Operation;
import com.mathsquare.objects.Board;
import com.mathsquare.ui.DisplayBoard;

public abstract class Solver {
    public static boolean IS_SOLVING = false;

    protected int width;
    protected int height;
    protected int boardlength;
    protected Operation[][] operationRows;
    protected Operation[][] operationColumns;
    protected int[] targetRows;
    protected int[] targetColumns;

    protected Board board;
    protected DisplayBoard displayBoard;

    public Solver(int width, int height, Operation[][] operationRows, Operation[][] operationColumns, int[] targetRows, int[] targetColumns, DisplayBoard displayBoard) {
        this.width = width;
        this.height = height;
        this.boardlength = width * height;
        this.operationRows = operationRows;
        this.operationColumns = operationColumns;
        this.targetRows = targetRows;
        this.targetColumns = targetColumns;
        this.displayBoard = displayBoard;
        this.board = new Board(width, height, operationRows, operationColumns, targetRows, targetColumns);
    }

    public Solver(Board board, DisplayBoard displayBoard) {
        this.width = board.getWidth();
        this.height = board.getHeight();
        this.boardlength = width * height;
        this.operationRows = board.getOperationRows();
        this.operationColumns = board.getOperationColumns();
        this.targetRows = board.getTargetRows();
        this.targetColumns = board.getTargetColumns();
        this.displayBoard = displayBoard;
        this.board = board;
    }

    public abstract void beginSolving();
}