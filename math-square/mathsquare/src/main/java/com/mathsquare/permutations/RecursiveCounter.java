package com.mathsquare.permutations;

import java.util.ArrayList;
import java.util.List;

/**
 * Counter to generate all permutations of a list.
 * Algorithm taken from oopexpert on stack exchange.
 * https://codereview.stackexchange.com/questions/119969/an-iterator-returning-all-possible-permutations-of-a-list-in-java
 */
public class RecursiveCounter<T>  {

    private int i;
    private int max;
    private RecursiveCounter<T> nextState;

    public RecursiveCounter(int max) {
        this.max = max;
        this.i = 0;
        if (this.max > 1) {
            nextState = new RecursiveCounter<T>(max - 1);
        }
    }

    public boolean nextStep() {
        boolean wasIncremented = true;
        if (nextState != null) {
            if (!nextState.nextStep()) {
                if (this.i == this.max - 1) {
                    this.i = 0;
                    wasIncremented = false;
                } else {
                    i++;
                }
            }
        } else {
            wasIncremented = false;
        }

        return wasIncremented;
    }


    private int getI(int level) {
        if (level == 0) {
            return this.i;
        } else {
            return nextState.getI(level - 1);
        }
    }

    public List<T> resolvePermutation(List<T> base) {
        List<T> result = new ArrayList<>();
        List<T> work = new ArrayList<>(base);
        for(int i = 0; i < base.size(); i++) {
            result.add(work.remove(getI(i)));
        }
        return result;
    }
}