package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class TimeEntryController {

    TimeEntryRepository timeEntryRepository = null;

    MeterRegistry meterRegistry = null;

    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry ) {

        this.timeEntryRepository = timeEntryRepository;
        this.meterRegistry = meterRegistry;



        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");

    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long nonExistentTimeEntryId) {
        TimeEntry te = timeEntryRepository.find(nonExistentTimeEntryId);
        if(te!=null) {
            actionCounter.increment();

            return new ResponseEntity(
                    te,
                    HttpStatus.OK
            );
        }else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();

        return new ResponseEntity(
                timeEntryRepository.list(),
                HttpStatus.OK
        );
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable("id") long timeEntryId, @RequestBody TimeEntry expected) {

        TimeEntry te = timeEntryRepository.update(timeEntryId, expected);
        if(te!=null) {
            actionCounter.increment();

            return new ResponseEntity(
                    te,
                    HttpStatus.OK
            );
        }else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable("id") long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        timeEntrySummary.record(timeEntryRepository.list().size());
        actionCounter.increment();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        actionCounter.increment();
        TimeEntry te = timeEntryRepository.create(timeEntryToCreate);

        timeEntrySummary.record(timeEntryRepository.list().size());


        return new ResponseEntity(te,HttpStatus.CREATED);
    }
}
