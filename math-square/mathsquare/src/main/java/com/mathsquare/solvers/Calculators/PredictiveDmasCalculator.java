package com.mathsquare.solvers.Calculators;

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
    public void loadOptions(long options, int justPlaced) {
        byte min = (byte)(Long.numberOfTrailingZeros(options) + 1);
        byte max = (byte)(64 - Long.numberOfLeadingZeros(options));
        int n = Long.bitCount(options);
        if (n < PredictAtNumOptions) {
            // Do not predict when there are too few options
            makePrediction = false;
        } else {
            // Get lower bound for remaining options
            if (min == justPlaced) {
                long offOptions = options & ~((long)1 << (justPlaced - 1));
                minOption = (byte)(Long.numberOfTrailingZeros(offOptions) + 1);
            }

            // Get upper bound for remaining options
            if (max == justPlaced) {
                long offOptions = options & ~((long)1 << (justPlaced - 1));
                maxOption = (byte)(64 - Long.numberOfLeadingZeros(offOptions));
            }

            // Bounds calculated, prediction can be made
            makePrediction = true;
        }
    }

    public boolean isUnfinishedRowValid(byte[] boardNumbers, int y) {
        // If not enough numbers, skip prediction
        if (!makePrediction) {
            return true;
        }

        // Predictive validation

        // Iterate over the row once to apply mul/div operators
        int chainhead = -1;
        for (int x = 0; x < board.getWidth(); x++) {
            float nextNum = getBoardNumber(boardNumbers, x, y);

            // If this is the first operand, load it immediately
            if (x == 0) {
                lowerBoundOperands[x] = nextNum == 0 ? minOption : nextNum;
                upperBoundOperands[x] = nextNum == 0 ? maxOption : nextNum;
                continue;
            }

            // Otherwise, load the number based on the operator
            switch (board.getOperationRows()[y][x-1]) {
                case ADD:
                    // Load numbers normally for add and sub
                    lowerBoundOperands[x] = nextNum == 0 ? minOption : nextNum;
                    upperBoundOperands[x] = nextNum == 0 ? maxOption : nextNum;
                    
                    // Indicate no mul/div chain
                    chainhead = -1;
                    break;
                case SUB:
                    // Load numbers normally for add and sub
                    lowerBoundOperands[x] = (nextNum == 0 ? maxOption : nextNum) * -1;
                    upperBoundOperands[x] = (nextNum == 0 ? minOption : nextNum) * -1;
                    
                    // Indicate no mul/div chain
                    chainhead = -1;
                    break;
                case MUL:
                    // Mark current mul/div chain    
                    if (chainhead < 0) {
                        chainhead = x - 1;
                    }

                    // Consolidate result of mul operator to
                    // the first term in the chain
                    if (nextNum == 0) {
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
                        chainhead = x - 1;
                    }

                    // Consolidate result of div operator to
                    // the first term in the chain
                    if (nextNum == 0) {
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
        for (int x = 0; x < board.getWidth(); x++) {
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

    public boolean isUnfinishedColumnValid(byte[] boardNumbers, int x) {
        // If not enough numbers, skip prediction
        if (!makePrediction) {
            return true;
        }

        // Predictive validation

        // Iterate over the column once to apply mul/div operators
        int chainhead = -1;
        for (int y = 0; y < board.getHeight(); y++) {
            float nextNum = getBoardNumber(boardNumbers, x, y);

            // If this is the first operand, load it immediately
            if (y == 0) {
                lowerBoundOperands[y] = nextNum == 0 ? minOption : nextNum;
                upperBoundOperands[y] = nextNum == 0 ? maxOption : nextNum;
                continue;
            }

            // Otherwise, load the number based on the operator
            switch (board.getOperationColumns()[x][y-1]) {
                case ADD:
                    // Load numbers normally for add
                    lowerBoundOperands[y] = nextNum == 0 ? minOption : nextNum;
                    upperBoundOperands[y] = nextNum == 0 ? maxOption : nextNum;
                    
                    // Indicate no mul/div chain
                    chainhead = -1;
                    break;
                case SUB:
                    // Load negataive numbers for sub
                    lowerBoundOperands[y] = (nextNum == 0 ? maxOption : nextNum) * -1;
                    upperBoundOperands[y] = (nextNum == 0 ? minOption : nextNum) * -1;
                    
                    // Indicate no mul/div chain
                    chainhead = -1;
                    break;
                case MUL:
                    // Mark current mul/div chain    
                    if (chainhead < 0) {
                        chainhead = y - 1;
                    }

                    // Consolidate result of mul operator to
                    // the first term in the chain
                    if (nextNum == 0) {
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
                        chainhead = y - 1;
                    }

                    // Consolidate result of div operator to
                    // the first term in the chain
                    if (nextNum == 0) {
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
        for (int y = 0; y < board.getHeight(); y++) {
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
