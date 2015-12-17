package ru.fizteh.fivt.students.ocksumoron.moduletests;

import ru.fizteh.fivt.students.ocksumoron.twitterstream.JCommanderProperties;

import java.util.List;

/**
 * Created by ocksumoron on 05.10.15.
 */
public interface TwisterService {
    List<String> printTweets(JCommanderProperties jcp);

    void printStream(JCommanderProperties jcp);
}
