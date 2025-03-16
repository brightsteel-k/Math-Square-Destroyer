package com.mathsquare.ui;

import java.util.List;

import com.mathsquare.objects.Board;
import com.mathsquare.util.Timer;


public class ConsoleBoard extends DisplayBoard {

    private Timer timer = new Timer();

    public ConsoleBoard(Board board) {
        super(board);
        timer.startTimer();
    }

    @Override
    public void onSolved(List<Byte> solution) {
        long duration = timer.stopTimer();
        if (solution.size() > 0) {
            System.out.println("SOLUTION FOUND: ");
            String[] pattern = board.getPattern();
            int width = board.getWidth();
            int digits = (int)Math.log10(board.getBoardLength());
            for (int i = 0; i < pattern.length; i++) {
                if (i % 2 == 0 && i < pattern.length - 1) {
                    System.out.println(formatSolvedNumberRow(pattern[i], solution.subList(i / 2 * width, (i / 2 + 1) * width), digits));
                } else if (i == pattern.length - 1) {
                    System.out.println(formatTargetsRow(pattern[i], digits));
                } else {
                    System.out.println(formatSolvedOperationRow(pattern[i], digits));
                }
            }
        } else {
            System.out.println("Program ended, no solution found.");
        }

        System.out.println("\n" + printTime(duration));
    }

    private String formatSolvedNumberRow(String row, List<Byte> rowNumbers, int digits) {
        StringBuilder newRow = new StringBuilder(row);
        int index = newRow.indexOf("#");
        int i = 0;
        while (index > -1) {
            Byte x = rowNumbers.get(i++);
            newRow.replace(index, index + 1, x.toString());
            for (int xDigits = (int)Math.log10(x); xDigits < digits; xDigits++) {
                newRow.insert(index + 1, " ");
            }
            index = newRow.indexOf("#");
        }
        return newRow.toString();
    }

    private String formatSolvedOperationRow(String row, int extraSpaces) {
        if (extraSpaces == 0) {
            return row;
        }

        StringBuilder newRow = new StringBuilder();
        for (int i = 0; i < row.length(); i++) {
            char c = row.charAt(i);
            newRow.append(c);
            if (c != ' ') {
                addSpaces(newRow, extraSpaces);
            }
        }
        return newRow.toString();
    }
    
    private String formatTargetsRow(String row, int extraSpaces) {
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
                    addSpaces(newRow, extraSpaces);
                    trailing = false;
                }
            } else if (!trailing) {
                trailing = true;
            }
        }
        return newRow.toString();
    }

    private void addSpaces(StringBuilder string, int count) {
        for (int s = 0; s < count; s++) {
            string.append(" ");
        }
    }

    private String printTime(long duration) {
        String commonTime = duration > 120000l ? String.format("%.2f min", (float)duration / 60000f) : String.format("%.2f sec", (float)duration / 1000f);
        return "TIME: " + duration + " ms / " + commonTime;
    }

    @Override
    public void updateNumbers(List<Byte> numbers) {
        StringBuilder update = new StringBuilder("CHECKING NUMBERS: [ ");
        for (int i = 0; i < numbers.size(); i++) {
            update.append(numbers.get(i));
            if (i < numbers.size() - 1) {
                update.append(", ");
            } else {
                update.append(" ]");
            }
        }
        System.out.println(update.toString());
    }
    
}
