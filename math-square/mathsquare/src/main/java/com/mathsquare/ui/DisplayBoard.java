package com.mathsquare.ui;

import com.mathsquare.objects.Board;

import java.util.List;

public abstract class DisplayBoard {

    protected Board board;

    public DisplayBoard() {}

    public void loadBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return this.board;
    }

    public abstract void onSolved(List<Byte> solution);

    public abstract void updateNumbers(List<Byte> numbers);
}
