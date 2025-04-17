package com.mathsquare.solvers.Calculators;

import java.util.List;

import com.mathsquare.objects.Board;

// Calculates rows and columns using order of operations, predicts whether unfinished
// rows or columns are still possible given the remaining options.
public class PredictiveDmasCalculator extends DmasCalculator implements IPredictiveCalculator {

    public final byte PredictAtNumOptions = 3;
    protected float minOption;
    protected float maxOption;
    protected boolean makePrediction;
    
    protected float[] lowerBoundOperands;
    protected float[] upperBoundOperands;

    public PredictiveDmasCalculator(Board boardIn) {
        super(boardIn);
        lowerBoundOperands = new float[Math.max(board.getWidth(), board.getHeight())];
        upperBoundOperands = new float[Math.max(board.getWidth(), board.getHeight())];
    }
    
    @Override
    public void loadOptions(List<Byte> options, byte justPlaced) {
        byte n = (byte)options.size();
        if (n < PredictAtNumOptions) {
            // Do not predict when there are too few options
            makePrediction = false;
        } else {
            // Get lower bound for remaining options
            minOption = options.get(0) == justPlaced ? options.get(1) : options.get(0);
    
            // Get upper bound for remaining options
            maxOption = options.get(n - 1) == justPlaced ? options.get(n - 2) : options.get(n - 1);
    
            // Bounds calculated, prediction can be made
            makePrediction = true;
        }
    }

    public boolean isRowValid(List<Byte> boardNumbers, byte y) {
        // If row is complete, compute product as usual
        if (!makePrediction || isRowFinished(boardNumbers, y)) {
            return super.isRowValid(boardNumbers, y);
        }

        // Predictive validation

        // Iterate over the row once to apply mul/div operators
        byte chainhead = -1;
        for (byte x = 0; x < board.getWidth(); x++) {
            float nextNum = getBoardNumber(boardNumbers, x, y);

            // If this is the first operand, load it immediately
            if (x == 0) {
                lowerBoundOperands[x] = nextNum == -1 ? minOption : nextNum;
                upperBoundOperands[x] = nextNum == -1 ? maxOption : nextNum;
                continue;
            }

            // Otherwise, load the number based on the operator
            switch (board.getOperationRows()[y][x-1]) {
                case ADD:
                    // Load numbers normally for add and sub
                    lowerBoundOperands[x] = nextNum == -1 ? minOption : nextNum;
                    upperBoundOperands[x] = nextNum == -1 ? maxOption : nextNum;
                    
                    // Indicate no mul/div chain
                    chainhead = -1;
                    break;
                case SUB:
                    // Load numbers normally for add and sub
                    lowerBoundOperands[x] = (nextNum == -1 ? maxOption : nextNum) * -1;
                    upperBoundOperands[x] = (nextNum == -1 ? minOption : nextNum) * -1;
                    
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
                    if (nextNum == -1) {
                        lowerBoundOperands[chainhead] *= lowerBoundOperands[chainhead] > 0 ? minOption : maxOption;
                        upperBoundOperands[chainhead] *= upperBoundOperands[chainhead] > 0 ? maxOption : minOption;
                    } else {
                        lowerBoundOperands[chainhead] *= nextNum;
                        upperBoundOperands[chainhead] *= nextNum;
                    }
                    lowerBoundOperands[x] = 1;
                    upperBoundOperands[x] = 1;
                    break;
                case DIV:
                    // Mark current mul/div chain    
                    if (chainhead < 0) {
                        chainhead = prev(x);
                    }

                    // Consolidate result of div operator to
                    // the first term in the chain
                    if (nextNum == -1) {
                        lowerBoundOperands[chainhead] /= lowerBoundOperands[chainhead] > 0 ? maxOption : minOption;
                        upperBoundOperands[chainhead] /= upperBoundOperands[chainhead] > 0 ? minOption : maxOption;
                    } else {
                        lowerBoundOperands[chainhead] /= nextNum;
                        upperBoundOperands[chainhead] /= nextNum;
                    }
                    lowerBoundOperands[x] = 1;
                    upperBoundOperands[x] = 1;
                    break;
            }
        }

        // Iterate over the row a second time to apply add/sub operators
        float lowerBound = 0;
        float upperBound = 0;
        int rowTarget = board.getRowTarget(y);
        for (byte x = 0; x < board.getWidth(); x++) {
            // If this is the first operand, load it immediately
            if (x == 0) {
                lowerBound = lowerBoundOperands[x];
                upperBound = upperBoundOperands[x];
                continue;
            }

            // Otherwise, apply appropriate operator (subtraction
            // carried out as addition with negative multipliers
            // applied in the first iteration)
            switch (board.getOperationRows()[y][x-1]) {
                case ADD:
                case SUB:
                    lowerBound += lowerBoundOperands[x];
                    upperBound += upperBoundOperands[x];
                    break;
                case MUL:
                case DIV:
                    // Multiplication and division were processed
                    // during the first iteration of the row.
                    break;
            }
        }
        
        // Return true if the target is still within the row's predicted lower and upper bounds
        return Math.round(lowerBound) <= rowTarget && Math.round(upperBound) >= rowTarget;
    }

    public boolean isColumnValid(List<Byte> boardNumbers, byte x) {
        // If column is complete, compute product as usual
        if (!makePrediction || isColumnFinished(boardNumbers, x)) {
            return super.isColumnValid(boardNumbers, x);
        }

        // Predictive validation

        // Iterate over the column once to apply mul/div operators
        byte chainhead = -1;
        for (byte y = 0; y < board.getHeight(); y++) {
            float nextNum = getBoardNumber(boardNumbers, x, y);

            // If this is the first operand, load it immediately
            if (y == 0) {
                lowerBoundOperands[y] = nextNum == -1 ? minOption : nextNum;
                upperBoundOperands[y] = nextNum == -1 ? maxOption : nextNum;
                continue;
            }

            // Otherwise, load the number based on the operator
            switch (board.getOperationColumns()[x][y-1]) {
                case ADD:
                    // Load numbers normally for add
                    lowerBoundOperands[y] = nextNum == -1 ? minOption : nextNum;
                    upperBoundOperands[y] = nextNum == -1 ? maxOption : nextNum;
                    
                    // Indicate no mul/div chain
                    chainhead = -1;
                    break;
                case SUB:
                    // Load negataive numbers for sub
                    lowerBoundOperands[y] = (nextNum == -1 ? maxOption : nextNum) * -1;
                    upperBoundOperands[y] = (nextNum == -1 ? minOption : nextNum) * -1;
                    
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
                    if (nextNum == -1) {
                        lowerBoundOperands[chainhead] *= lowerBoundOperands[chainhead] > 0 ? minOption : maxOption;
                        upperBoundOperands[chainhead] *= upperBoundOperands[chainhead] > 0 ? maxOption : minOption;
                    } else {
                        lowerBoundOperands[chainhead] *= nextNum;
                        upperBoundOperands[chainhead] *= nextNum;
                    }
                    lowerBoundOperands[y] = 1;
                    upperBoundOperands[y] = 1;
                    break;
                case DIV:
                    // Mark current mul/div chain    
                    if (chainhead < 0) {
                        chainhead = prev(y);
                    }

                    // Consolidate result of div operator to
                    // the first term in the chain
                    if (nextNum == -1) {
                        lowerBoundOperands[chainhead] /= lowerBoundOperands[chainhead] > 0 ? maxOption : minOption;
                        upperBoundOperands[chainhead] /= upperBoundOperands[chainhead] > 0 ? minOption : maxOption;
                    } else {
                        lowerBoundOperands[chainhead] /= nextNum;
                        upperBoundOperands[chainhead] /= nextNum;
                    }
                    lowerBoundOperands[y] = 1;
                    upperBoundOperands[y] = 1;
                    break;
            }
        }

        // Iterate over the row a second time to apply add/sub operators
        float lowerBound = 0;
        float upperBound = 0;
        int columnTarget = board.getColumnTarget(x);
        for (byte y = 0; y < board.getHeight(); y++) {
            // If this is the first operand, load it immediately
            if (y == 0) {
                lowerBound = lowerBoundOperands[y];
                upperBound = upperBoundOperands[y];
                continue;
            }

            // Otherwise, apply appropriate operator (subtraction
            // carried out as addition with negative multipliers
            // applied in the first iteration)
            switch (board.getOperationColumns()[x][y-1]) {
                case ADD:
                case SUB:
                    lowerBound += lowerBoundOperands[y];
                    upperBound += upperBoundOperands[y];
                    break;
                case MUL:
                case DIV:
                    // Multiplication and division were processed
                    // during the first iteration of the row.
                    break;
            }
        }
        
        // Return true if the target is still within the row's predicted lower and upper bounds
        return Math.round(lowerBound) <= columnTarget && Math.round(upperBound) >= columnTarget;
    }
}
