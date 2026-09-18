package com.mathsquare.objects;

import com.google.gson.annotations.Expose;
import com.mathsquare.Operation;

import java.util.Arrays;
import java.util.List;

public class Board {
    public static Board EMPTY_BOARD = new Board();

    @Expose
    private byte width;
    @Expose
    private byte height;
    @Expose
    private String[] pattern;
    private Operation[][] operationRows = new Operation[][] { };
    private Operation[][] operationColumns = new Operation[][] { };
    private int[] targetRows = new int[] { };
    private int[] targetColumns = new int[] { };

    public Board() {
        
    }

    public Board(int width, int height, Operation[][] operationRows, Operation[][] operationColumns, int[] targetRows, int[] targetColumns) {
        this.width = (byte)width;
        this.height = (byte)height;
        this.operationRows = operationRows;
        this.operationColumns = operationColumns;
        this.targetRows = targetRows;
        this.targetColumns = targetColumns;
        this.pattern = new String[] { "MANUAL CONSTRUCTOR USED: NO PATTERN AVAILABLE" };
    }

    public Board format(List<Operation[]> parsedOperationRows, List<Operation[]> parsedOperationColumns, List<Integer> parsedTargetRows, List<Integer> parsedTargetColumns) {
        this.operationRows = parsedOperationRows.toArray(this.operationRows);
        this.operationColumns = parsedOperationColumns.toArray(this.operationColumns);
        this.targetRows = parsedTargetRows.stream().mapToInt(i -> i).toArray();
        this.targetColumns = parsedTargetColumns.stream().mapToInt(i -> i).toArray();
        return this;
    }

    public String[] getPattern() {
        if (!isValidPattern(pattern)) {
            pattern = generateBoardPattern(this);
        }
        return pattern;
    }

    public byte getWidth() {
        return width;
    }
    
    public byte getHeight() {
        return height;
    }

    public Operation[][] getOperationRows() {
        return operationRows;
    }
    
    public Operation[][] getOperationColumns() {
        return operationColumns;
    }

    public int[] getTargetRows() {
        return targetRows;
    }

    public int getRowTarget(byte row) {
        return targetRows[row];
    }

    public int[] getTargetColumns() {
        return targetColumns;
    }

    public int getColumnTarget(byte column) {
        return targetColumns[column];
    }

    public int getBoardLength() {
        return width * height;
    }

    public static boolean isValidPattern(String[] pattern) {
        return pattern.length > 0 && pattern[0].startsWith("#");
    }

    public static String[] generateBoardPattern(Board board) {
        int width = board.getWidth();
        int height = board.getHeight();

        // Maybe unnecessary?
        int targetNumLength = Arrays.stream(board.getTargetColumns())
                .map(Math::abs)
                .max()
                .toString()
                .length() + 1;
        int patternWidth = width * 3 + 1 + targetNumLength;

        int patternHeight = height * 2 + 1;

        String[] rows = new String[patternHeight];
        StringBuilder row = new StringBuilder();
        Operation[] ops;
        for (int i = 0; i < height * 2; i++) {
            // Reset current row
            row.setLength(0);

            if (i % 2 == 0) {
                // Numbers row
                ops = board.getOperationRows()[i / 2];
                for (int x = 0; x < width - 1; x++) { row.append("# "); row.append(ops[x]); row.append(" "); }
                row.append("#  = ");

                // Row targets
                row.append(board.getTargetRows()[i / 2]);
            } else if (i == height * 2 - 1) {
                // Equals row
                for (int x = 0; x < width; x++) { row.append("=   "); }
            } else {
                // Operations row
                for (int x = 0; x < width; x++) { row.append(board.getOperationColumns()[x][i / 2]); row.append("   "); }
            }

            // Add current row
            rows[i] = row.toString();
        }


        // Column targets
        row.setLength(0);
        int totalLength = 0;
        for (byte x = 0; x < width; x++) {
            int target = board.getColumnTarget(x);
            totalLength += Integer.toString(target).length();
            row.append(target);
            int numSpaces = Math.max(4 * x + 4 - totalLength, 1);
            row.append(" ".repeat(numSpaces));
            totalLength += numSpaces;
        }
        rows[patternHeight - 1] = row.toString();

        return rows;
    }

    public void debugPrint() {
        System.out.print("\n\nOperation Rows:");
        for (int i = 0; i < operationRows.length; i++) {
            System.out.print("\n>   ");
            for (int k = 0; k < operationRows[i].length; k++) {
                System.out.print(operationRows[i][k].toString() + ", ");
            }
        }
        System.out.print("\n\nOperation Columns:");
        for (int i = 0; i < operationColumns.length; i++) {
            System.out.print("\n>   ");
            for (int k = 0; k < operationColumns[i].length; k++) {
                System.out.print(operationColumns[i][k].toString() + ", ");
            }
        }
        System.out.print("\n\nRow Targets:\n>   ");
        for (int i = 0; i < targetRows.length; i++) {
            System.out.print(targetRows[i] + ", ");
        }
        System.out.print("\n\nColumn Targets:\n>   ");
        for (int i = 0; i < targetColumns.length; i++) {
            System.out.print(targetColumns[i] + ", ");
        }
    }
}
