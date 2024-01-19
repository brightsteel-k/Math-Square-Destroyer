package com.mathsquare.calculators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mathsquare.calculators.Operators.IOperator;
import com.mathsquare.objects.Board;
import com.mathsquare.ui.DisplayBoard;

public class BacktrackCalculator extends Calculator {

    protected int tick = 0;
    protected int ticksPerUpdate = 1000;

    protected List<Integer> boardNumbers;
    protected LinkedList<List<Integer>> boardsToTest;
    protected List<Integer> options;
    protected LinkedList<List<Integer>> optionsToTest;
    protected IOperator operator;

    protected boolean breadthFirst;

    public BacktrackCalculator(Board board, DisplayBoard displayBoard) {
        this(board, displayBoard, false, false);
    }

    public BacktrackCalculator(Board board, DisplayBoard displayBoard, boolean breadthFirst) {
        this(board, displayBoard, breadthFirst, false);
    }

    public BacktrackCalculator(Board board, DisplayBoard displayBoard, boolean breadthFirst, boolean useOrderOfOperations) {
        super(board, displayBoard);
        this.breadthFirst = breadthFirst;
        if (useOrderOfOperations) {
            operator = new DmasOperator();
        } else {
            operator = new NaturalOperator();
        }
    }
    
    @Override
    public void beginSolving() {
        IS_SOLVING = true;
        List<Integer> possibleNumbers = new ArrayList<>();
        for (int k = 1; k <= boardlength; k++) {
            possibleNumbers.add(k);
        }

        this.boardNumbers = new ArrayList<>();
        this.boardsToTest = new LinkedList<>();
        this.options = possibleNumbers;
        this.optionsToTest = new LinkedList<>();

        if (breadthFirst) {
            beginSolvingBreadthFirst();
        } else {
            beginSolvingDepthFirst();
        }
        
    }

    public void beginSolvingDepthFirst() {
        while (IS_SOLVING) {
            if (solveBoardDepthFirst()) {
                IS_SOLVING = false;
                displayBoard.onSolved(boardNumbers);
            } else if (++tick == ticksPerUpdate) {
                displayBoard.updateNumbers(boardNumbers);
                tick = 0;
            }
        }
    }

    public void beginSolvingBreadthFirst() {
        while (IS_SOLVING) {
            if (solveBoardBreadthFirst()) {
                IS_SOLVING = false;
                displayBoard.onSolved(boardNumbers);
            } else if (++tick == ticksPerUpdate) {
                displayBoard.updateNumbers(boardNumbers);
                tick = 0;
            }
        }
    }

    private boolean solveBoardDepthFirst() {
        if (isBoardSolved(boardNumbers)) {
            return true;
        } else {
            // Calculate new boards
            List<Integer> currentBoard;
            for (int i = 0; i < options.size(); i++) {
                currentBoard = new ArrayList<>(boardNumbers);
                currentBoard.add(options.get(i));

                if (isBoardValid(currentBoard, currentBoard.size() - 1)) {
                    boardsToTest.addFirst(currentBoard);
                    List<Integer> opts = new ArrayList<>(options);
                    opts.remove(options.get(i));
                    optionsToTest.addFirst(opts);
                }
            }

            // Solve next board, if there is one
            if (boardsToTest.size() == 0) {
                boardNumbers = new ArrayList<>();
                return true;
            } else {
                boardNumbers = boardsToTest.poll();
                options = optionsToTest.poll();
                return false;
            }
        }
    }

    private boolean solveBoardBreadthFirst() {
        if (isBoardSolved(boardNumbers)) {
            return true;
        } else {
            // Calculate new boards
            List<Integer> currentBoard;
            for (int i = 0; i < options.size(); i++) {
                currentBoard = new ArrayList<>(boardNumbers);
                currentBoard.add(options.get(i));

                if (isBoardValid(currentBoard, currentBoard.size() - 1)) {
                    boardsToTest.add(currentBoard);
                    List<Integer> opts = new ArrayList<>(options);
                    opts.remove(options.get(i));
                    optionsToTest.add(opts);
                }
            }

            // Solve next board, if there is one
            if (boardsToTest.size() == 0) {
                boardNumbers = new ArrayList<>();
                return true;
            } else {
                boardNumbers = boardsToTest.poll();
                options = optionsToTest.poll();
                return false;
            }
        }
    }

    private boolean isBoardSolved(List<Integer> boardNumbers) {
        return boardNumbers.size() == boardlength;
    }

    private boolean isBoardValid(List<Integer> boardNumbers, int n) {
        if (n < width - 1) {
            return true;
        }

        int row = n / width;
        int column = n % width;

        // Check row and column
        return operator.isRowValid(row, getRowNumbers(boardNumbers, row), targetRows[row])
                && operator.isColumnValid(column, getColumnNumbers(boardNumbers, column), targetColumns[column]);
    }

    private List<Integer> getRowNumbers(List<Integer> boardNumbers, int y) {
        int endPoint = Math.min((y + 1) * width, boardNumbers.size());
        return new ArrayList<Integer>(boardNumbers.subList(y * width, endPoint));
    }

    private List<Integer> getColumnNumbers(List<Integer> boardNumbers, int x) {
        List<Integer> columnNumbers = new ArrayList<>();
        for (int j = x; j < boardlength; j = j + width) {
            if (j < boardNumbers.size()) {
                columnNumbers.add(boardNumbers.get(j));
            } else {
                break;
            }
        }
        return columnNumbers;
    }

    public void setDepthFirst() {
        breadthFirst = false;
    }

    public void setBreadthFirst() {
        breadthFirst = true;
    }

    public class NaturalOperator implements IOperator {
        public boolean isRowValid(int y, List<Integer> numbers, int target) {
            int product = numbers.get(0);
            for (int x = 1; x < width; x++) {
                try {
                    switch (operationRows[y][x-1]) {
                        case ADD:
                            product += numbers.get(x);
                            break;
                        case SUB:
                            product -= numbers.get(x);
                            break;
                        case MUL:
                            product *= numbers.get(x);
                            break;
                        case DIV:
                            if (product % numbers.get(x) != 0) {
                                return false;
                            }
                            product /= numbers.get(x);
                            break;
                    }
                } catch (IndexOutOfBoundsException e) {
                    return true;
                }
            }
            return product == target;
        }

        public boolean isColumnValid(int x, List<Integer> numbers, int target) {
            int product = numbers.get(0);
            for (int y = 1; y < height; y++) {
                try {
                    switch (operationColumns[x][y-1]) {
                        case ADD:
                            product += numbers.get(y);
                            break;
                        case SUB:
                            product -= numbers.get(y);
                            break;
                        case MUL:
                            product *= numbers.get(y);
                            break;
                        case DIV:
                            if (product % numbers.get(y) != 0) {
                                return false;
                            }
                            product /= numbers.get(y);
                            break;
                    }
                } catch (IndexOutOfBoundsException e) {
                    return true;
                }
            }
            return product == target;
        }
    }

    public class DmasOperator implements IOperator {
        public boolean isRowValid(int y, List<Integer> numbers, int target) {
            float product = 1;
            int chainHead = -1;
            try {
                for (int x = 1; x < width; x++) {
                    switch (operationRows[y][x-1]) {
                        case MUL:
                            if (chainHead == -1) {
                                product = numbers.get(x-1) * numbers.get(x);
                                chainHead = x-1;
                            } else {
                                product *= numbers.get(x);
                            }
                            numbers.set(x, 1);
                            break;
                        case DIV:
                            if (chainHead == -1) {
                                product = (float)numbers.get(x-1) / (float)numbers.get(x);
                                chainHead = x-1;
                            } else {
                                product /= (float)numbers.get(x);
                            }
                            numbers.set(x, 1);
                            break;
                        default:
                            if (chainHead != -1) {
                                if (product != (int)product) { return false; }
                                numbers.set(chainHead, (int)product);
                                chainHead = -1;
                            }
                            numbers.get(x);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                return true;
            }

            if (chainHead != -1) {
                if (product != (int)product) { return false; }
                numbers.set(chainHead, (int)product);
            }

            product = numbers.get(0);
            for (int x = 1; x < width; x++) {
                switch (operationRows[y][x-1]) {
                    case ADD:
                        product += numbers.get(x);
                        break;
                    case SUB:
                        product -= numbers.get(x);
                        break;
                    case MUL:
                        product *= numbers.get(x);
                        break;
                    case DIV:
                        product /= numbers.get(x);
                        break;
                }
            }
            return (int)product == target;
        }

        public boolean isColumnValid(int x, List<Integer> numbers, int target) {
            float product = 1;
            int chainHead = -1;
            for (int y = 1; y < height; y++) {
                try {
                    switch (operationColumns[x][y-1]) {
                        case MUL:
                            if (chainHead == -1) {
                                product = numbers.get(y-1) * numbers.get(y);
                                chainHead = y-1;
                            } else {
                                product *= numbers.get(y);
                            }
                            numbers.set(y, 1);
                            break;
                        case DIV:
                            if (chainHead == -1) {
                                product = (float)numbers.get(y-1) / (float)numbers.get(y);
                                chainHead = y-1;
                            } else {
                                product /= (float)numbers.get(y);
                            }
                            numbers.set(y, 1);
                            break;
                        default:
                            if (chainHead != -1) {
                                if (product != (int)product) { return false; }
                                numbers.set(chainHead, (int)product);
                                chainHead = -1;
                            }
                            numbers.get(y);
                    }
                } catch (IndexOutOfBoundsException e) {
                    return true;
                }
            }

            if (chainHead != -1) {
                if (product != (int)product) { return false; }
                numbers.set(chainHead, (int)product);
            }

            product = numbers.get(0);
            for (int y = 1; y < height; y++) {
                switch (operationColumns[x][y-1]) {
                    case ADD:
                        product += numbers.get(y);
                        break;
                    case SUB:
                        product -= numbers.get(y);
                        break;
                    case MUL:
                        product *= numbers.get(y);
                        break;
                    case DIV:
                        product /= numbers.get(y);
                        break;
                }
            }
            return (int)product == target;
        }
    }
}
