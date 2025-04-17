package com.mathsquare.solvers.Calculators;

import java.util.List;

import com.mathsquare.objects.Board;

// Calculates rows and columns in the order the operations appear (NO order of operations),
// predicts whether unfinished rows or columns are still possible given the remaining options.
public class PredictiveNaturalCalculator extends NaturalCalculator implements IPredictiveCalculator {

    public final byte PredictAtNumOptions = 3;
    protected float minOption;
    protected float maxOption;
    protected boolean makePrediction;

    public PredictiveNaturalCalculator(Board boardIn) {
        super(boardIn);
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
        float upperBound = 0;
        float lowerBound = 0;
        int rowTarget = board.getRowTarget(y);

        // Calculate product and bounds for row
        for (byte x = 0; x < board.getWidth(); x++) {
            float nextNum = getBoardNumber(boardNumbers, x, y);

            // If this is the first operand, initialize bounds
            if (x == 0) {
                upperBound = nextNum == -1 ? maxOption : nextNum;
                lowerBound = nextNum == -1 ? minOption : nextNum;
                continue;
            }

            // Otherwise, apply the appropriate operator with the next number
            switch (board.getOperationRows()[y][x-1]) {
                case ADD:
                    lowerBound += nextNum == -1 ? minOption : nextNum;
                    upperBound += nextNum == -1 ? maxOption : nextNum;
                    break;
                case SUB:
                    lowerBound -= nextNum == -1 ? maxOption : nextNum;
                    upperBound -= nextNum == -1 ? minOption : nextNum;
                    break;
                case MUL:
                    lowerBound *= nextNum == -1 ? minOption : nextNum;
                    upperBound *= nextNum == -1 ? maxOption : nextNum;
                    break;
                case DIV:
                    lowerBound /= nextNum == -1 ? maxOption : nextNum;
                    upperBound /= nextNum == -1 ? minOption : nextNum;
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
        float upperBound = 0;
        float lowerBound = 0;
        int columnTarget = board.getColumnTarget(x);

        // Calculate product and bounds for row
        for (byte y = 0; y < board.getHeight(); y++) {
            float nextNum = getBoardNumber(boardNumbers, x, y);

            // If this is the first operand, initialize bounds
            if (y == 0) {
                upperBound = nextNum == -1 ? maxOption : nextNum;
                lowerBound = nextNum == -1 ? minOption : nextNum;
                continue;
            }

            // Otherwise, apply the appropriate operator with the next number
            switch (board.getOperationColumns()[x][y-1]) {
                case ADD:
                    lowerBound += nextNum == -1 ? minOption : nextNum;
                    upperBound += nextNum == -1 ? maxOption : nextNum;
                    break;
                case SUB:
                    lowerBound -= nextNum == -1 ? maxOption : nextNum;
                    upperBound -= nextNum == -1 ? minOption : nextNum;
                    break;
                case MUL:
                    if (nextNum == -1) {
                        lowerBound *= lowerBound > 0 ? minOption : maxOption;
                        upperBound *= upperBound > 0 ? maxOption : minOption;
                    } else {
                        lowerBound *= nextNum;
                        upperBound *= nextNum;
                    }
                    break;
                case DIV:
                    if (nextNum == -1) {
                        lowerBound /= lowerBound > 0 ? maxOption : minOption;
                        upperBound /= upperBound > 0 ? minOption : maxOption;
                    } else {
                        lowerBound /= nextNum;
                        upperBound /= nextNum;
                    }
                    break;
            }
        }

        // Return true if the target is still within the row's predicted lower and upper bounds
        return Math.round(lowerBound) <= columnTarget && Math.round(upperBound) >= columnTarget;
    }
}
