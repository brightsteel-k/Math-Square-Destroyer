package com.mathsquare;

import com.mathsquare.calculators.BacktrackCalculator;
import com.mathsquare.calculators.Calculator;
import com.mathsquare.objects.Board;
import com.mathsquare.ui.ConsoleBoard;
import com.mathsquare.util.DataManager;

public class Main {
    public static String BOARD_TO_SOLVE ="bday-card/masd.json";

    public static void main(String[] args) {
        Board board = DataManager.deserializeBoard("math-square/mathsquare/data/" + BOARD_TO_SOLVE);
        ConsoleBoard displayBoard = new ConsoleBoard(board);
        Calculator calculator = new BacktrackCalculator(board, displayBoard, false, true);
        calculator.beginSolving();
    }
}
