package ru.fizteh.fivt.students.ocksumoron.collectionquery.impl;

import java.lang.reflect.InvocationTargetException;

public interface Query<R> {
    Iterable<R> execute() throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException;
}
