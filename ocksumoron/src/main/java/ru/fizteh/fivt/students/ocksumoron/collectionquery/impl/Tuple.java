package ru.fizteh.fivt.students.ocksumoron.collectionquery.impl;

/**
 * Created by ocksumoron on 17.12.15.
 */
public class Tuple<F, S> {

    private final F first;
    private final S second;

    public Tuple(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "Tuple{"
                + "first=" + first
                + ", second=" + second
                + '}';
    }
}
