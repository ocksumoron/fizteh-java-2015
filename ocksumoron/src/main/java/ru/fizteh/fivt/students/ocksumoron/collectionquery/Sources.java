package ru.fizteh.fivt.students.ocksumoron.collectionquery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ocksumoron on 17.12.15.
 */
public class Sources {

    /**
     * @param items
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> List<T> list(T... items) {
        return new ArrayList<>(Arrays.asList(items));
    }

}
