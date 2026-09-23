package com.mathsquare.solvers.Calculators;

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
    public void loadOptions(long options, int justPlaced) {
        int n = Long.bitCount(options);
        if (n < PredictAtNumOptions) {
            // Do not predict when there are too few options
            makePrediction = false;
        } else {
            minOption = (byte)(Long.numberOfTrailingZeros(options) + 1);
            maxOption = (byte)(64 - Long.numberOfLeadingZeros(options));

            // Get lower bound for remaining options
            if (minOption == justPlaced) {
                long offOptions = options & ~((long)1 << (justPlaced - 1));
                minOption = (byte)(Long.numberOfTrailingZeros(offOptions) + 1);
            }

            // Get upper bound for remaining options
            if (maxOption == justPlaced) {
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
        float upperBound = 0;
        float lowerBound = 0;
        int rowTarget = board.getRowTarget(y);

        // Calculate product and bounds for row
        for (int x = 0; x < board.getWidth(); x++) {
            float nextNum = getBoardNumber(boardNumbers, x, y);

            // If this is the first operand, initialize bounds
            if (x == 0) {
                upperBound = nextNum == 0 ? maxOption : nextNum;
                lowerBound = nextNum == 0 ? minOption : nextNum;
                continue;
            }

            // Otherwise, apply the appropriate operator with the next number
            switch (board.getOperationRows()[y][x-1]) {
                case ADD:
                    lowerBound += nextNum == 0 ? minOption : nextNum;
                    upperBound += nextNum == 0 ? maxOption : nextNum;
                    break;
                case SUB:
                    lowerBound -= nextNum == 0 ? maxOption : nextNum;
                    upperBound -= nextNum == 0 ? minOption : nextNum;
                    break;
                case MUL:
                    if (nextNum == 0) {
                        lowerBound *= lowerBound > 0 ? minOption : maxOption;
                        upperBound *= upperBound > 0 ? maxOption : minOption;
                    } else {
                        lowerBound *= nextNum;
                        upperBound *= nextNum;
                    }
                    break;
                case DIV:
                    if (nextNum == 0) {
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
        return Math.round(lowerBound) <= rowTarget && Math.round(upperBound) >= rowTarget;
    }

    public boolean isUnfinishedColumnValid(byte[] boardNumbers, int x) {
        // If not enough numbers, skip prediction
        if (!makePrediction) {
            return true;
        }

        // Predictive validation
        float upperBound = 0;
        float lowerBound = 0;
        int columnTarget = board.getColumnTarget(x);

        // Calculate product and bounds for row
        for (int y = 0; y < board.getHeight(); y++) {
            float nextNum = getBoardNumber(boardNumbers, x, y);

            // If this is the first operand, initialize bounds
            if (y == 0) {
                upperBound = nextNum == 0 ? maxOption : nextNum;
                lowerBound = nextNum == 0 ? minOption : nextNum;
                continue;
            }

            // Otherwise, apply the appropriate operator with the next number
            switch (board.getOperationColumns()[x][y-1]) {
                case ADD:
                    lowerBound += nextNum == 0 ? minOption : nextNum;
                    upperBound += nextNum == 0 ? maxOption : nextNum;
                    break;
                case SUB:
                    lowerBound -= nextNum == 0 ? maxOption : nextNum;
                    upperBound -= nextNum == 0 ? minOption : nextNum;
                    break;
                case MUL:
                    if (nextNum == 0) {
                        lowerBound *= lowerBound > 0 ? minOption : maxOption;
                        upperBound *= upperBound > 0 ? maxOption : minOption;
                    } else {
                        lowerBound *= nextNum;
                        upperBound *= nextNum;
                    }
                    break;
                case DIV:
                    if (nextNum == 0) {
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
