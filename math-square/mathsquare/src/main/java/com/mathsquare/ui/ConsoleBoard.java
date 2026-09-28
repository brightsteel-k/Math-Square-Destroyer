package com.mathsquare.ui;

import com.mathsquare.objects.Board;
import com.mathsquare.util.Timer;

import java.util.Arrays;


public class ConsoleBoard extends DisplayBoard {

    private Timer timer = new Timer();
    private long duration = 0;

    public ConsoleBoard() {
        super();
    }

    @Override
    public void loadBoard(Board board) {
        super.loadBoard(board);
        timer.startTimer();
    }

    @Override
    public void onSolved(byte[] solution) {
        duration = timer.stopTimer();
        System.out.println(getSolutionDisplay(solution));
    }

    public String getSolutionDisplay(byte[] solution) {
        StringBuilder displayString = new StringBuilder();
        if (solution.length > 0) {
            displayString.append("SOLUTION FOUND: \n");
            String[] pattern = board.getPattern();

            int width = board.getWidth();
            int digits = (int)Math.log10(board.getBoardLength());
            for (int i = 0; i < pattern.length; i++) {
                if (i % 2 == 0 && i < pattern.length - 1) {
                    displayString.append(formatSolvedNumberRow(pattern[i], Arrays.copyOfRange(solution, i / 2 * width, (i / 2 + 1) * width), digits));
                    displayString.append("\n");
                } else if (i == pattern.length - 1) {
                    displayString.append(formatTargetsRow(pattern[i], digits));
                    displayString.append("\n");
                } else {
                    displayString.append(formatSolvedOperationRow(pattern[i], digits));
                    displayString.append("\n");
                }
            }
        } else {
            displayString.append("Program ended, no solution found.\n");
        }

        displayString.append("\n");
        displayString.append(printTime(duration));
        displayString.append("\n");

        return displayString.toString();
    }

    private static String formatSolvedNumberRow(String row, byte[] rowNumbers, int digits) {
        StringBuilder newRow = new StringBuilder(row);
        int index = newRow.indexOf("#");
        int i = 0;
        while (index > -1) {
            byte x = rowNumbers[i++];
            newRow.replace(index, index + 1, Byte.toString(x));
            for (int xDigits = (int)Math.log10(x); xDigits < digits; xDigits++) {
                newRow.insert(index + 1, " ");
            }
            index = newRow.indexOf("#");
        }
        return newRow.toString();
    }

    private static String formatSolvedOperationRow(String row, int extraSpaces) {
        if (extraSpaces == 0) {
            return row;
        }

        StringBuilder newRow = new StringBuilder();
        for (int i = 0; i < row.length(); i++) {
            char c = row.charAt(i);
            newRow.append(c);
            if (c != ' ') {
                newRow.append(" ".repeat(extraSpaces));
            }
        }
        return newRow.toString();
    }
    
    private static String formatTargetsRow(String row, int extraSpaces) {
        if (extraSpaces == 0) {
            return row;
        }

        StringBuilder newRow = new StringBuilder();
        boolean trailing = false;
        for (int i = 0; i < row.length(); i++) {
            char c = row.charAt(i);
            newRow.append(c);
            if (c == ' ') {
                if (trailing) {
                    newRow.append(" ".repeat(extraSpaces));
                    trailing = false;
                }
            } else if (!trailing) {
                trailing = true;
            }
        }
        return newRow.toString();
    }

    private static String printTime(long duration) {
        String commonTime = duration > 120000l ? String.format("%.2f min", (float)duration / 60000f) : String.format("%.2f sec", (float)duration / 1000f);
        return "TIME: " + duration + " ms / " + commonTime;
    }

    @Override
    public void updateNumbers(byte[] numbers) {
        StringBuilder update = new StringBuilder("CHECKING NUMBERS: \n");
        for (int i = 1; i < numbers.length; i++) {
            update.append(numbers[i]);
            if ((i+1) % board.getWidth() == 0) {
                update.append(",\n");
            } else {
                update.append(", ");
            }
        }
        update.append("[First entry: ");
        update.append(numbers[progressTrackerIndex + 1]);
        update.append(" | Entries Filled: ");
        update.append(numbers[0]);
        update.append("]");
        System.out.println(update);
    }
}
