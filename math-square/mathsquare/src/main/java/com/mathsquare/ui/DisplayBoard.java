package com.mathsquare.ui;

import java.util.List;

import com.mathsquare.objects.Board;

public abstract class DisplayBoard {

    protected Board board;

    public DisplayBoard(Board board) {
        this.board = board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return this.board;
    }

    public abstract void onSolved(List<Byte> solution);

    public abstract void updateNumbers(List<Byte> numbers);
}
