package com.mathsquare.solvers.Calculators;

import java.util.List;

import com.mathsquare.objects.Board;

// Calculates rows and columns in the order the operations appear (NO order of operations)
public class NaturalCalculator extends Calculator {

    public NaturalCalculator(Board boardIn) {
        super(boardIn);
    }
    
    public boolean isRowValid(List<Byte> boardNumbers, byte y) {
        float product = 0;

        // Calculate current product for row
        for (byte x = 0; x < board.getWidth(); x++) {
            float nextNum = getBoardNumber(boardNumbers, x, y);
            if (nextNum == -1) {
                // Return true for an unfinished row
                return true;
            }

            // If this is the first operand, initialize product
            if (x == 0) {
                product = nextNum;
                continue;
            }

            // Otherwise, apply the appropriate operator with the next number
            switch (board.getOperationRows()[y][x-1]) {
                case ADD:
                    product += nextNum;
                    break;
                case SUB:
                    product -= nextNum;
                    break;
                case MUL:
                    product *= nextNum;
                    break;
                case DIV:
                    product /= nextNum;
                    break;
            }
        }

        // Compare calculated value to target value
        return Math.round(product) == board.getRowTarget(y);
    }

    public boolean isColumnValid(List<Byte> boardNumbers, byte x) {
        float product = 0;

        // Calculate current product for column
        for (byte y = 0; y < board.getHeight(); y++) {
            float nextNum = getBoardNumber(boardNumbers, x, y);
            if (nextNum == -1) {
                // Return true for an unfinished column
                return true;
            }

            // If this is the first operand, initialize product
            if (y == 0) {
                product = nextNum;
                continue;
            }

            // Otherwise, apply the appropriate operator with the next number
            switch (board.getOperationColumns()[x][y-1]) {
                case ADD:
                    product += nextNum;
                    break;
                case SUB:
                    product -= nextNum;
                    break;
                case MUL:
                    product *= nextNum;
                    break;
                case DIV:
                    product /= nextNum;
                    break;
            }
        }

        // Compare calculated value to target value
        return Math.round(product) == board.getColumnTarget(x);
    }
}
