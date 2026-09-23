package com.mathsquare.ui;

import com.mathsquare.objects.Board;

public abstract class DisplayBoard {

    protected Board board;

    public DisplayBoard() {}

    public void loadBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return this.board;
    }

    public abstract void onSolved(byte[] solution);

    public abstract void updateNumbers(byte[] numbers);
}
