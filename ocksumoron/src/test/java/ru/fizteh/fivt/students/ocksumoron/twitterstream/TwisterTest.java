package ru.fizteh.fivt.students.ocksumoron.twitterstream;

import com.beust.jcommander.JCommander;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ocksumoron on 18.12.15.
 */
public class TwisterTest {
    @Test
    public void testGetFormattedTweetsEmpty() throws Exception {
        JCommanderProperties jcp = new JCommanderProperties();
        String[] args = {"-p", "Moscow", "-q", "pulp Ficionazzio"};
        JCommander jParser = new JCommander(jcp, args);
        List<String> tweets = Twister.printTweets(jcp);
        assertEquals(tweets.size(), 0);

    }

    @Test
    public void testGetFormattedTweetsLimited() throws Exception {
        JCommanderProperties jcp = new JCommanderProperties();
        String[] args = {"-p", "New York", "-q", "a", "--limit", "14"};
        JCommander jParser = new JCommander(jcp, args);
        List<String> tweets = Twister.printTweets(jcp);
        assertEquals(tweets.size(), 14);
    }
}