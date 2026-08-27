package luigibot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests date-based searching in {@link TaskList}.
 */
public class TaskListTest {

    @Test
    public void findIndexesOnDate_emptyTaskList_emptyListReturned() {
        TaskList tasks = new TaskList();

        assertEquals(List.of(), tasks.findIndexesOnDate(LocalDate.of(2026, 8, 27)));
    }

    @Test
    public void findIndexesOnDate_multipleMatchingTasks_zeroBasedIndexesInListOrderReturned() {
        TaskList tasks = new TaskList(List.of(
                new Todo("read book"),
                new Deadline("submit report", "2026-08-27 1800"),
                new Event("conference", "2026-08-26 2300", "2026-08-28 0100"),
                new Deadline("return book", "2026-08-28 1800")));

        assertEquals(List.of(1, 2),
                tasks.findIndexesOnDate(LocalDate.of(2026, 8, 27)));
    }

    @Test
    public void findIndexesOnDate_noMatchingTasks_emptyListReturned() {
        TaskList tasks = new TaskList(List.of(
                new Todo("read book"),
                new Deadline("return book", "2026-08-28 1800"),
                new Event("meeting", "2026-08-28 1400", "2026-08-28 1600")));

        assertEquals(List.of(), tasks.findIndexesOnDate(LocalDate.of(2026, 8, 27)));
    }
}
