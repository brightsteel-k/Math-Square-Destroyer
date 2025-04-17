package com.mathsquare.solvers.Calculators;

import java.util.List;

import com.mathsquare.objects.Board;

// Calculates rows and columns in the order the operations appear (NO order of operations)
public class NaturalCalculator extends Calculator {

    public NaturalCalculator(Board boardIn) {
        super(boardIn);
    }
    
    public boolean isRowValid(List<Byte> boardNumbers, byte y) {
        // Unfinished rows automatically pass validation 
        if (!isRowFinished(boardNumbers, y)) {
            return true;
        }

        // Calculate current product for row
        float product = 0;
        for (byte x = 0; x < board.getWidth(); x++) {
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
        return Math.round(product) == board.getRowTarget(y);
    }

    public boolean isColumnValid(List<Byte> boardNumbers, byte x) {
        // Unfinished columns automatically pass validation
        if (!isColumnFinished(boardNumbers, x)) {
            return true;
        }

        // Calculate current product for column
        float product = 0;
        for (byte y = 0; y < board.getHeight(); y++) {
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
        return Math.round(product) == board.getColumnTarget(x);
    }
}
