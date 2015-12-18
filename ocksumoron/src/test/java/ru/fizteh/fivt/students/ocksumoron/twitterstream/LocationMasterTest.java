package ru.fizteh.fivt.students.ocksumoron.twitterstream;

import junit.framework.Assert;
import org.junit.Test;
import ru.fizteh.fivt.students.ocksumoron.twitterstream.LocationMaster;

import static org.junit.Assert.*;

/**
 * Created by ocksumoron on 18.12.15.
 */
public class LocationMasterTest {
    @Test
    public void testGetLocation() throws Exception {
        String place = "Moscow";
        double eps = 1e-5;
        Location location = LocationMaster.getLocation(place);
        assertEquals(location.getLatitudeCenter(), 55.75396, eps);
        assertEquals(location.getLongitudeCenter(), 37.620393, eps);
    }

    @Test
    public void testBadLocation() throws Exception {
        String place = "Aldebaran";
        Location location = LocationMaster.getLocation(place);
        assertEquals(location.getError(), -1);
    }
}