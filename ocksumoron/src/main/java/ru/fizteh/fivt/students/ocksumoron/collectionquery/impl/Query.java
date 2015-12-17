package ru.fizteh.fivt.students.ocksumoron.collectionquery.impl;

import java.util.stream.Stream;

/**
 * Created by ocksumoron on 17.12.15.
 */
public interface Query<R> {

    Iterable<R> execute();

    Stream<R> stream();
}