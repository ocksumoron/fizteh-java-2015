package ru.fizteh.fivt.students.ocksumoron.twitterstream;

import org.junit.Test;
import twitter4j.Status;



import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import twitter4j.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import static org.junit.Assert.*;

public class FormatMasterTest {

    static final String SEPARATOR =
            "\n-----------------------------------------------------------------------------------------------------\n";

    @Test
    public void testFormatRetweets() {

        User alPacino = mock(User.class);
        when(alPacino.getName()).thenReturn("Al Pacino");

        User marlonBrando = mock(User.class);
        when(marlonBrando.getName()).thenReturn("Marlon Brando");

        Status statusRetweeted = mock(Status.class);

        when(statusRetweeted.getCreatedAt()).thenReturn(
                Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        when(statusRetweeted.isRetweet()).thenReturn(false);
        when(statusRetweeted.getUser()).thenReturn(marlonBrando);
        when(statusRetweeted.getText()).thenReturn("No respect.");
        when(statusRetweeted.isRetweeted()).thenReturn(true);
        when(statusRetweeted.getRetweetCount()).thenReturn(10);
        assertEquals("[только что]@Marlon Brando: No respect. (10 ретвитов)" + SEPARATOR,
                FormatMaster.format(statusRetweeted, false, false));
        when(statusRetweeted.getRetweetCount()).thenReturn(2);
        assertEquals("[только что]@Marlon Brando: No respect. (2 ретвита)" + SEPARATOR,
                FormatMaster.format(statusRetweeted, false, false));
        when(statusRetweeted.getRetweetCount()).thenReturn(21);
        assertEquals("[только что]@Marlon Brando: No respect. (21 ретвит)" + SEPARATOR,
                FormatMaster.format(statusRetweeted, false, false));
        when(statusRetweeted.getRetweetCount()).thenReturn(117);
        assertEquals("[только что]@Marlon Brando: No respect. (117 ретвитов)" + SEPARATOR,
                FormatMaster.format(statusRetweeted, false, false));
        when(statusRetweeted.getRetweetCount()).thenReturn(204);
        assertEquals("[только что]@Marlon Brando: No respect. (204 ретвита)" + SEPARATOR,
                FormatMaster.format(statusRetweeted, false, false));
        Status statusRetweet = mock(Status.class);
        when(statusRetweet.getCreatedAt()).thenReturn(
                Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        when(statusRetweet.isRetweet()).thenReturn(true);
        when(statusRetweet.getUser()).thenReturn(alPacino);
        when(statusRetweet.getRetweetedStatus()).thenReturn(statusRetweeted);
        assertEquals("[только что]@Al Pacino ретвитнул @Marlon Brando: No respect." + SEPARATOR,
                FormatMaster.format(statusRetweet, false, false));

    }

    @Test
    public void testFormatNoRetweets() {

        User marlonBrando = mock(User.class);
        when(marlonBrando.getName()).thenReturn("Marlon Brando");

        Status status = mock(Status.class);
        when(status.getCreatedAt()).thenReturn(
                Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        when(status.isRetweet()).thenReturn(false);
        when(status.getUser()).thenReturn(marlonBrando);
        when(status.getText()).thenReturn("No respect.");
        when(status.isRetweet()).thenReturn(false);
        assertEquals("[только что]@Marlon Brando: No respect." + SEPARATOR,
                FormatMaster.format(status, true, false));
    }

    @Test
    public void testTimeForms() {

        User marlonBrando = mock(User.class);
        when(marlonBrando.getName()).thenReturn("Marlon Brando");

        Status status = mock(Status.class);
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime newTime;


        when(status.isRetweet()).thenReturn(false);
        when(status.getUser()).thenReturn(marlonBrando);
        when(status.getText()).thenReturn("No respect.");
        when(status.isRetweet()).thenReturn(false);

        newTime = currentTime.minusHours(3);
        when(status.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        if (ChronoUnit.DAYS.between(newTime, currentTime) == 0) {
            assertEquals("[3 часа назад]@Marlon Brando: No respect." + SEPARATOR,
                    FormatMaster.format(status, true, false));
        } else {
            assertEquals("[вчера]@Marlon Brando: No respect." + SEPARATOR,
                    FormatMaster.format(status, true, false));
        }


        newTime = currentTime.minusHours(1);
        when(status.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        if (ChronoUnit.DAYS.between(newTime, currentTime) == 0) {
            assertEquals("[1 час назад]@Marlon Brando: No respect." + SEPARATOR,
                    FormatMaster.format(status, true, false));
        } else {
            assertEquals("[вчера]@Marlon Brando: No respect." + SEPARATOR,
                    FormatMaster.format(status, true, false));
        }


        newTime = currentTime.minusHours(10);
        when(status.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        if (ChronoUnit.DAYS.between(newTime, currentTime) == 0) {
            assertEquals("[10 часов назад]@Marlon Brando: No respect." + SEPARATOR,
                    FormatMaster.format(status, true, false));
        } else {
            assertEquals("[вчера]@Marlon Brando: No respect." + SEPARATOR,
                    FormatMaster.format(status, true, false));
        }

        newTime = currentTime.minusDays(1);
        if (ChronoUnit.DAYS.between(newTime, currentTime) == 0) {
            newTime = currentTime.minusDays(2);
        }
        when(status.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertEquals("[вчера]@Marlon Brando: No respect." + SEPARATOR,
                FormatMaster.format(status, true, false));

        newTime = currentTime.minusDays(2);
        when(status.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertEquals("[2 дня назад]@Marlon Brando: No respect." + SEPARATOR,
                FormatMaster.format(status, true, false));
        newTime = currentTime.minusDays(5);
        when(status.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertEquals("[5 дней назад]@Marlon Brando: No respect." + SEPARATOR,
                FormatMaster.format(status, true, false));
        newTime = currentTime.minusDays(31);
        when(status.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertEquals("[31 день назад]@Marlon Brando: No respect." + SEPARATOR,
                FormatMaster.format(status, true, false));
        newTime = currentTime.minusDays(1543);
        when(status.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertEquals("[1543 дня назад]@Marlon Brando: No respect." + SEPARATOR,
                FormatMaster.format(status, true, false));
        newTime = currentTime.minusDays(111);
        when(status.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertEquals("[111 дней назад]@Marlon Brando: No respect." + SEPARATOR,
                FormatMaster.format(status, true, false));

        newTime = currentTime.minusMinutes(3);
        when(status.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        if (ChronoUnit.HOURS.between(newTime, currentTime) == 0) {
            assertEquals("[3 минуты назад]@Marlon Brando: No respect." + SEPARATOR,
                    FormatMaster.format(status, true, false));
        } else {
            assertEquals("[1 час назад]@Marlon Brando: No respect." + SEPARATOR,
                    FormatMaster.format(status, true, false));
        }

        newTime = currentTime.minusMinutes(21);
        when(status.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        if (ChronoUnit.HOURS.between(newTime, currentTime) == 0) {
            assertEquals("[21 минуту назад]@Marlon Brando: No respect." + SEPARATOR,
                    FormatMaster.format(status, true, false));
        } else {
            assertEquals("[1 час назад]@Marlon Brando: No respect." + SEPARATOR,
                    FormatMaster.format(status, true, false));
        }

        newTime = currentTime.minusMinutes(5);
        when(status.getCreatedAt()).thenReturn(
                Date.from(newTime.atZone(ZoneId.systemDefault()).toInstant()));
        if (ChronoUnit.HOURS.between(newTime, currentTime) == 0) {
            assertEquals("[5 минут назад]@Marlon Brando: No respect." + SEPARATOR,
                    FormatMaster.format(status, true, false));
        } else {
            assertEquals("[1 час назад]@Marlon Brando: No respect." + SEPARATOR,
                    FormatMaster.format(status, true, false));
        }


    }
}