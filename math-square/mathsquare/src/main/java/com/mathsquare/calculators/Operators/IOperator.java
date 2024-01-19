package com.mathsquare.calculators.Operators;

import java.util.List;

public interface IOperator {
    public boolean isRowValid(int y, List<Integer> numbers, int target);

    public boolean isColumnValid(int x, List<Integer> numbers, int target);
}
