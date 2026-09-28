package com.mathsquare.permutations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterator to cycle through all permutations of a list.
 * Algorithm taken from oopexpert on stack exchange.
 * https://codereview.stackexchange.com/questions/119969/an-iterator-returning-all-possible-permutations-of-a-list-in-java
 */
public class PermutationIterator<T> implements Iterator<List<T>> {

    private List<T> base;

    private RecursiveCounter<T> permutationResolver;
    private List<T> next;

    public PermutationIterator(List<T> base, RecursiveCounter<T> resolver) {
        this.base = base;
        this.next = new ArrayList<>(base);
        this.permutationResolver = resolver;
    }

    private List<T> generateNextPermutation(boolean isLast) {
        List<T> result = null;
        if (!isLast) {
            result = getPermutationResolver().resolvePermutation(base);
        }
        return result;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public List<T> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        List<T> current = next;
        this.next = generateNextPermutation(!getPermutationResolver().nextStep());
        return current;
    }

    private RecursiveCounter<T> getPermutationResolver() {
        return permutationResolver;
    }

}
