package com.mathsquare.util;

import java.util.ArrayList;
import java.util.List;

import com.mathsquare.Operation;
import com.mathsquare.exceptions.InvalidBoardPatternException;
import com.mathsquare.objects.Board;

public class BoardPatternManager {
    
    public static Board processBoard(Board board) {
        String[] pattern = board.getPattern();
        int width = board.getWidth();
        int height = board.getHeight();

        // Row operations + targets
        List<Operation[]> parsedRowOperations = new ArrayList<>();
        List<Integer> parsedRowTargets = new ArrayList<>();
        for (int y = 0; y < height * 2 - 1; y += 2) {
            String rowPattern = pattern[y].replace(" ", "");
            parseRowOperations(parsedRowOperations, rowPattern, width);
            parseRowTarget(parsedRowTargets, rowPattern);
        }

        // Column operations
        List<Operation[]> parsedColumnOperations = new ArrayList<>();
        int rowCount = 0;
        for (int k = 0; k < width; k++) {
            parsedColumnOperations.add(new Operation[height - 1]);
        }
        for (int y = 1; y < height * 2 - 1; y += 2) {
            parseColumnOperations(parsedColumnOperations, pattern[y], width, rowCount++);
        }

        // Column targets
        List<Integer> parsedColumnTargets = new ArrayList<>();
        if (!checkEqualsRow(pattern[pattern.length - 2].replace(" ", ""), width)) {
            throw new InvalidBoardPatternException("Board pattern column targets improperly formatted.");
        }
        parseColumnTargets(parsedColumnTargets, pattern[pattern.length - 1]);

        if (parsedRowOperations.size() != height) {
            throw new InvalidBoardPatternException("Board pattern height does not match height variable.");
        } else if (parsedColumnOperations.size() != width) {
            throw new InvalidBoardPatternException("Board pattern width does not match width variable.");
        } else if (parsedRowTargets.size() != height) {
            throw new InvalidBoardPatternException("Board pattern row targets does not match up with height of board.");
        } else if (parsedColumnOperations.size() != width) {
            throw new InvalidBoardPatternException("Board pattern column targets does not match up with width of board.");
        }

        return board.format(parsedRowOperations, parsedColumnOperations, parsedRowTargets, parsedColumnTargets);
    }

    public static void parseRowOperations(List<Operation[]> rowOperations, String rowPattern, int width) {
        Operation[] result = new Operation[width - 1];
        rowPattern = rowPattern.replace(" ", "");
        int x = 0;
        int numbers = 0;
        for (int i = 0; i < rowPattern.length(); i++) {
            char c = rowPattern.charAt(i);
            if (c == '#') {
                 numbers++;
                 continue;
            } else if (c == '=') {
                break;
            }
            result[x++] = Operation.parseOperation(c);
        }

        if (numbers != width) {
            throw new InvalidBoardPatternException("Row length in pattern inconsistent with width variable.", rowPattern);
        }
        rowOperations.add(result);
    }

    public static void parseColumnOperations(List<Operation[]> columnOperations, String rowPattern, int width, int y) {
        rowPattern = rowPattern.replace(" ", "");
        int column = 0;
        for (int i = 0; i < rowPattern.length(); i++) {
            char c = rowPattern.charAt(i);
            columnOperations.get(column)[y] = Operation.parseOperation(c);
            if (++column >= width) {
                break;
            }
        }
    }

    public static void parseRowTarget(List<Integer> rowTargets, String rowPattern) {
        int index = rowPattern.indexOf('=');
        if (index == -1) {
            throw new InvalidBoardPatternException("Row detected without specified target.", rowPattern);
        }

        rowTargets.add(Integer.parseInt(rowPattern.substring(index + 1, rowPattern.length())));
    }

    public static void parseColumnTargets(List<Integer> columnTargets, String rowPattern) {
        String[] targets = rowPattern.split(" ");
        for (int i = 0; i < targets.length; i++) {
            if (targets[i].length() > 0) {
                columnTargets.add(Integer.parseInt(targets[i]));
            }
        }
    }

    public static boolean checkEqualsRow(String equalsRow, int width) {
        StringBuilder idealEqualsRow = new StringBuilder();
        for (int k = 0; k < width; k++) {
            idealEqualsRow.append("=");
        }
        return idealEqualsRow.toString().equals(equalsRow);
    }
}
