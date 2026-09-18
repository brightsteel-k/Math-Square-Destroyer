package com.mathsquare.solvers.Calculators;

import com.mathsquare.Operation;
import com.mathsquare.objects.Board;

import java.util.List;

// Calculates rows and columns using order of operations
public class DmasCalculator extends Calculator {

    protected float[] operands;

    public DmasCalculator() {}

    public DmasCalculator(Board boardIn) {
        super(boardIn);
    }

    @Override
    public void loadBoard(Board boardIn) {
        super.loadBoard(boardIn);
        operands = new float[Math.max(board.getWidth(), board.getHeight())];
    }

    protected void processRowOperands(List<Byte> boardNumbers, byte y) {
        // Iterate over the row once to apply mul/div operators
        byte chainhead = -1;
        for (byte x = 0; x < board.getWidth(); x++) {
            float nextNum = getBoardNumber(boardNumbers, x, y);

            // If this is the first operand, load it immediately
            if (x == 0) {
                operands[x] = nextNum;
                continue;
            }

            // Otherwise, load the number based on the operator
            switch (board.getOperationRows()[y][x-1]) {
                case ADD:
                case SUB:
                    // Load numbers normally for add and sub
                    operands[x] = nextNum;

                    // Indicate no mul/div chain
                    chainhead = -1;
                    break;
                case MUL:
                    // Mark current mul/div chain
                    if (chainhead < 0) {
                        chainhead = prev(x);
                    }

                    // Consolidate result of mul operator to
                    // the first term in the chain
                    operands[chainhead] *= nextNum;
                    operands[x] = 1;

                    break;
                case DIV:
                    // Mark current mul/div chain
                    if (chainhead < 0) {
                        chainhead = prev(x);
                    }

                    // Consolidate result of div operator to
                    // the first term in the chain
                    operands[chainhead] /= nextNum;
                    operands[x] = 1;
                    break;
            }
        }
    }

    public boolean isRowValid(List<Byte> boardNumbers, byte y) {
        // Unfinished rows automatically pass validation 
        if (!isRowFinished(boardNumbers, y)) {
            return true;
        }

        // Iterate over the row once to apply mul/div operators
        processRowOperands(boardNumbers, y);

        // Iterate over the row a second time to apply add/sub operators
        float product = 0;
        for (byte x = 0; x < board.getWidth(); x++) {
            // If this is the first operand, load it immediately
            if (x == 0) {
                product = operands[x];
                continue;
            }

            // Otherwise, apply appropriate operator
            switch (board.getOperationRows()[y][x-1]) {
                case ADD:
                    product += operands[x];
                    break;
                case SUB:
                    product -= operands[x];
                    break;
                case MUL:
                case DIV:
                    // Multiplication and division were processed
                    // during the first iteration of the row.
                    break;
            }
        }
        
        // Compare calculated value to target value
        return matches(product, board.getRowTarget(y));
    }

    protected void processColumnOperands(List<Byte> boardNumbers, byte x) {
        // Iterate over the column once to apply mul/div operators
        byte chainhead = -1;
        for (byte y = 0; y < board.getHeight(); y++) {
            float nextNum = getBoardNumber(boardNumbers, x, y);

            // If this is the first operand, load it immediately
            if (y == 0) {
                operands[y] = nextNum;
                continue;
            }

            // Otherwise, load the number based on the operator
            switch (board.getOperationColumns()[x][y-1]) {
                case ADD:
                case SUB:
                    // Load numbers normally for add and sub
                    operands[y] = nextNum;

                    // Indicate no mul/div chain
                    chainhead = -1;
                    break;
                case MUL:
                    // Mark current mul/div chain
                    if (chainhead < 0) {
                        chainhead = prev(y);
                    }

                    // Consolidate result of mul operator to
                    // the first term in the chain
                    operands[chainhead] *= nextNum;
                    operands[y] = 1;

                    break;
                case DIV:
                    // Mark current mul/div chain
                    if (chainhead < 0) {
                        chainhead = prev(y);
                    }

                    // Consolidate result of div operator to
                    // the first term in the chain
                    operands[chainhead] /= nextNum;
                    operands[y] = 1;
                    break;
            }
        }
    }

    public boolean isColumnValid(List<Byte> boardNumbers, byte x) {
        // Unfinished columns automatically pass validation 
        if (!isColumnFinished(boardNumbers, x)) {
            return true;
        }

        // Iterate over the column once to apply mul/div operators
        processColumnOperands(boardNumbers, x);

        // Iterate over the row a second time to apply add/sub operators
        float product = 0;
        for (byte y = 0; y < board.getHeight(); y++) {
            // If this is the first operand, load it immediately
            if (y == 0) {
                product = operands[y];
                continue;
            }

            // Otherwise, apply appropriate operator
            switch (board.getOperationColumns()[x][y-1]) {
                case ADD:
                    product += operands[y];
                    break;
                case SUB:
                    product -= operands[y];
                    break;
                case MUL:
                case DIV:
                    // Multiplication and division were processed
                    // during the first iteration of the column.
                    break;
            }
        }
        
        // Compare calculated value to target value
        return matches(product, board.getColumnTarget(x));
    }

    @Override
    public int calculateLine(List<Byte> lineNumbers, Operation[] operations) {
        operands = new float[lineNumbers.size()];

        // Iterate over the column once to apply mul/div operators
        byte chainhead = -1;
        for (byte i = 0; i < lineNumbers.size(); i++) {
            float nextNum = lineNumbers.get(i);

            // If this is the first operand, load it immediately
            if (i == 0) {
                operands[i] = nextNum;
                continue;
            }

            // Otherwise, load the number based on the operator
            switch (operations[i - 1]) {
                case ADD:
                case SUB:
                    // Load numbers normally for add and sub
                    operands[i] = nextNum;

                    // Indicate no mul/div chain
                    chainhead = -1;
                    break;
                case MUL:
                    // Mark current mul/div chain
                    if (chainhead < 0) {
                        chainhead = prev(i);
                    }

                    // Consolidate result of mul operator to
                    // the first term in the chain
                    operands[chainhead] *= nextNum;
                    operands[i] = 1;

                    break;
                case DIV:
                    // Mark current mul/div chain
                    if (chainhead < 0) {
                        chainhead = prev(i);
                    }

                    // Consolidate result of div operator to
                    // the first term in the chain
                    operands[chainhead] /= nextNum;
                    operands[i] = 1;
                    break;
            }
        }

        // Iterate over the row a second time to apply add/sub operators
        float product = 0;
        for (byte i = 0; i < lineNumbers.size(); i++) {
            // If this is the first operand, load it immediately
            if (i == 0) {
                product = operands[i];
                continue;
            }

            // Otherwise, apply appropriate operator
            switch (operations[i-1]) {
                case ADD:
                    product += operands[i];
                    break;
                case SUB:
                    product -= operands[i];
                    break;
                case MUL:
                case DIV:
                    // Multiplication and division were processed
                    // during the first iteration of the column.
                    break;
            }
        }

        int roundedProduct = Math.round(product);
        return matches(product, roundedProduct) ? roundedProduct : Integer.MIN_VALUE;
    }
}
