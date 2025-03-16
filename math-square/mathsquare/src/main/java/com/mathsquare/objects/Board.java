package com.mathsquare.objects;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.mathsquare.Operation;
public class Board {
    @Expose
    private int width;
    @Expose
    private int height;
    @Expose
    private String[] pattern;
    private Operation[][] operationRows = new Operation[][] { };
    private Operation[][] operationColumns = new Operation[][] { };
    private int[] targetRows = new int[] { };
    private int[] targetColumns = new int[] { };

    public Board() {
        
    }

    public Board(int width, int height, Operation[][] operationRows, Operation[][] operationColumns, int[] targetRows, int[] targetColumns) {
        this.width = width;
        this.height = height;
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
        return pattern;
    }

    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
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
