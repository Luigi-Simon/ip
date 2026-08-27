package luigibot.task;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Tests the date-overlap behavior of {@link Event}.
 */
public class EventTest {

    @Test
    public void occursOn_eventWithinDate_trueReturned() {
        Event event = new Event("project meeting", "2026-08-27 1400", "2026-08-27 1600");

        assertTrue(event.occursOn(LocalDate.of(2026, 8, 27)));
    }

    @Test
    public void occursOn_overnightEventStartDate_trueReturned() {
        Event event = new Event("night shift", "2026-08-27 2300", "2026-08-28 0100");

        assertTrue(event.occursOn(LocalDate.of(2026, 8, 27)));
    }

    @Test
    public void occursOn_overnightEventEndDate_trueReturned() {
        Event event = new Event("night shift", "2026-08-27 2300", "2026-08-28 0100");

        assertTrue(event.occursOn(LocalDate.of(2026, 8, 28)));
    }

    @Test
    public void occursOn_eventSpansWholeDate_trueReturned() {
        Event event = new Event("conference", "2026-08-26 2300", "2026-08-28 0100");

        assertTrue(event.occursOn(LocalDate.of(2026, 8, 27)));
    }

    @Test
    public void occursOn_dateBeforeEvent_falseReturned() {
        Event event = new Event("project meeting", "2026-08-27 1400", "2026-08-27 1600");

        assertFalse(event.occursOn(LocalDate.of(2026, 8, 26)));
    }

    @Test
    public void occursOn_dateAfterEvent_falseReturned() {
        Event event = new Event("project meeting", "2026-08-27 1400", "2026-08-27 1600");

        assertFalse(event.occursOn(LocalDate.of(2026, 8, 28)));
    }

    @Test
    public void occursOn_eventEndsAtStartOfDate_falseReturned() {
        Event event = new Event("night shift", "2026-08-27 2300", "2026-08-28 0000");

        assertFalse(event.occursOn(LocalDate.of(2026, 8, 28)));
    }
}
