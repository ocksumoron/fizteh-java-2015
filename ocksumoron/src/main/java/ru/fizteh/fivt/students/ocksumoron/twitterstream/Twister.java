package ru.fizteh.fivt.students.ocksumoron.twitterstream;

import com.beust.jcommander.JCommander;
import twitter4j.*;

import ru.fizteh.fivt.students.ocksumoron.twitterstream.InvalidLocationException;
import java.util.ArrayList;
import java.util.List;


public class Twister {

    private static final long MILLISEC_IN_SEC = 1000;

    public static void main(String[] args)  {
        JCommanderProperties jcp = new JCommanderProperties();
        try {
            JCommander jParser = new JCommander(jcp, args);
            if (jcp.isPrintHelp()) {
                jParser.usage();
            }
            if (!jcp.isStream()) {
                List<String> toPrint = printTweets(jcp);
                toPrint.forEach(System.out::print);
            } else {
                printStream(jcp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JCommander jc = new JCommander(jcp);
            jc.addCommand(args);
        }
    }

    public static List<String> printTweets(JCommanderProperties jcp) {
        LocationMaster locationMaster = new LocationMaster();
        List<String> ans = new ArrayList<>();
        try {
            Location location = locationMaster.getLocation(jcp.getPlace());
            if (location.getError() != 0) {
                System.err.println("Bad location");
                System.exit(1);
            }
            GeoLocation geoLocation = new GeoLocation(location.getLatitudeCenter(), location.getLongitudeCenter());
            Twitter twitter = new TwitterFactory().getInstance();
            Query query = new Query(jcp.getQuery());
            query.setCount(jcp.getLimitNumber());
            Query.Unit resUnit = Query.Unit.km;
            query.setGeoCode(geoLocation, location.getRes(), resUnit);
            query.setCount(jcp.getLimitNumber());
            FormatMaster formatter = new FormatMaster();
            twitter.search(query).getTweets().stream()
                    .map(s -> formatter.format(s, jcp.isHideRetweets(), false)).forEach(ans::add);

        } catch (TwitterException | InvalidLocationException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public static void printStream(JCommanderProperties jcp) throws InvalidLocationException {
        Location location = LocationMaster.getLocation(jcp.getPlace());
        if (location.getError() == 0) {
            FilterQuery filterQuery = new FilterQuery();
            String[] keyword = {jcp.getQuery()};

            double[][] locationBox = {{location.getLongitudeSWCorner(), location.getLatitudeSWCorner()},
                    {location.getLongitudeNECorner(), location.getLatitudeNECorner()}};

            filterQuery.track(keyword);

            filterQuery.locations(locationBox);

            TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

            twitterStream.addListener(new StatusAdapter() {
                @Override
                public void onStatus(Status status) {
                    try {
                        Thread.sleep(MILLISEC_IN_SEC);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(FormatMaster.format(status, jcp.isHideRetweets(), true));
                }

                @Override
                public void onException(Exception ex) {
                    TwitterException twex = (TwitterException) ex;
                    twex.printStackTrace();
                }
            });
            twitterStream.filter(filterQuery);
        } else {
            throw new InvalidLocationException("very bad");
        }
    }
}
