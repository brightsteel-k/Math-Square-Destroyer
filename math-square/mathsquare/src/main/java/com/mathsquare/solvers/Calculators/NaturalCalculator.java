package com.mathsquare.solvers.Calculators;

import com.mathsquare.Operation;
import com.mathsquare.objects.Board;

// Calculates rows and columns in the order the operations appear (NO order of operations)
public class NaturalCalculator extends Calculator {

    public NaturalCalculator() {
        super();
    }

    public NaturalCalculator(Board boardIn) {
        super(boardIn);
    }
    
    public boolean isRowValid(byte[] boardNumbers, int y) {
        // Unfinished rows automatically pass validation 
        if (!isRowFinished(boardNumbers, y)) {
            return isUnfinishedRowValid(boardNumbers, y);
        }

        // Calculate current product for row
        float product = 0;
        for (int x = 0; x < board.getWidth(); x++) {
            float nextNum = getBoardNumber(boardNumbers, x, y);

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
        return matches(product, board.getRowTarget(y));
    }

    public boolean isColumnValid(byte[] boardNumbers, int x) {
        // Unfinished columns automatically pass validation
        if (!isColumnFinished(boardNumbers, x)) {
            return isUnfinishedColumnValid(boardNumbers, x);
        }

        // Calculate current product for column
        float product = 0;
        for (int y = 0; y < board.getHeight(); y++) {
            float nextNum = getBoardNumber(boardNumbers, x, y);

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
        return matches(product, board.getColumnTarget(x));
    }

    @Override
    public int calculateLine(byte[] lineNumbers, Operation[] operations) {
        // Calculate current product for line
        float product = lineNumbers[0];
        for (byte i = 1; i < lineNumbers.length; i++) {
            float nextNum = lineNumbers[i];

            // Otherwise, apply the appropriate operator with the next number
            switch (operations[i - 1]) {
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

        int roundedProduct = Math.round(product);
        return matches(product, roundedProduct) ? roundedProduct : Integer.MIN_VALUE;
    }
}
