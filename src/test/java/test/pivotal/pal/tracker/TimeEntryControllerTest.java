package test.pivotal.pal.tracker;

import io.pivotal.pal.tracker.TimeEntry;
import io.pivotal.pal.tracker.TimeEntryController;
import io.pivotal.pal.tracker.TimeEntryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TimeEntryControllerTest {
    private TimeEntryRepository timeEntryRepository;
    private TimeEntryController controller;

    @BeforeEach
    public void setUp() {
        timeEntryRepository = Mockito.mock(TimeEntryRepository.class);
        controller = new TimeEntryController(timeEntryRepository);
    }

    @Test
    public void testCreate() {
        long projectId = 123L;
        long userId = 456L;
        TimeEntry timeEntryToCreate = new TimeEntry(projectId, userId, LocalDate.parse("2017-01-08"), 8);

        long timeEntryId = 1L;
        TimeEntry expectedResult = new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8);
        Mockito.doReturn(expectedResult)
            .when(timeEntryRepository)
            .create(ArgumentMatchers.any(TimeEntry.class));

        ResponseEntity response = controller.create(timeEntryToCreate);

        Mockito.verify(timeEntryRepository).create(timeEntryToCreate);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isEqualTo(expectedResult);
    }

    @Test
    public void testRead() {
        long timeEntryId = 1L;
        long projectId = 123L;
        long userId = 456L;
        TimeEntry expected = new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8);
        Mockito.doReturn(expected)
            .when(timeEntryRepository)
            .find(timeEntryId);

        ResponseEntity<TimeEntry> response = controller.read(timeEntryId);

        Mockito.verify(timeEntryRepository).find(timeEntryId);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void testRead_NotFound() {
        long nonExistentTimeEntryId = 1L;
        Mockito.doReturn(null)
            .when(timeEntryRepository)
            .find(nonExistentTimeEntryId);

        ResponseEntity<TimeEntry> response = controller.read(nonExistentTimeEntryId);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testList() {
        List<TimeEntry> expected = asList(
            new TimeEntry(1L, 123L, 456L, LocalDate.parse("2017-01-08"), 8),
            new TimeEntry(2L, 789L, 321L, LocalDate.parse("2017-01-07"), 4)
        );
        Mockito.doReturn(expected).when(timeEntryRepository).list();

        ResponseEntity<List<TimeEntry>> response = controller.list();

        Mockito.verify(timeEntryRepository).list();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void testUpdate() {
        long timeEntryId = 1L;
        long projectId = 987L;
        long userId = 654L;
        TimeEntry expected = new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-07"), 4);
        Mockito.doReturn(expected)
            .when(timeEntryRepository)
            .update(ArgumentMatchers.eq(timeEntryId), ArgumentMatchers.any(TimeEntry.class));

        ResponseEntity response = controller.update(timeEntryId, expected);

        Mockito.verify(timeEntryRepository).update(timeEntryId, expected);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void testUpdate_NotFound() {
        long nonExistentTimeEntryId = 1L;
        Mockito.doReturn(null)
            .when(timeEntryRepository)
            .update(ArgumentMatchers.eq(nonExistentTimeEntryId), ArgumentMatchers.any(TimeEntry.class));

        ResponseEntity response = controller.update(nonExistentTimeEntryId, new TimeEntry());
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDelete() {
        long timeEntryId = 1L;
        ResponseEntity response = controller.delete(timeEntryId);
        Mockito.verify(timeEntryRepository).delete(timeEntryId);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
