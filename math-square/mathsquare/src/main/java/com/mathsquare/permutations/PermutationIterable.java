package com.mathsquare.permutations;

import java.util.Iterator;
import java.util.List;

/**
 * Iterable to cycle through all permutations of a list.
 * Algorithm taken from oopexpert on stack exchange.
 * https://codereview.stackexchange.com/questions/119969/an-iterator-returning-all-possible-permutations-of-a-list-in-java
 */
public class PermutationIterable<T> implements Iterable<List<T>> {

    private List<T> base;
    private RecursiveCounter<T> resolver;

    public PermutationIterable(List<T> base, RecursiveCounter<T> resolver) {
        super();
        this.base = base;
        this.resolver = resolver;
    }

    @Override
    public Iterator<List<T>> iterator() {
        return new PermutationIterator<T>(base, resolver);
    }

}
