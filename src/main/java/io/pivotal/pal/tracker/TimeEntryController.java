package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class TimeEntryController {

    TimeEntryRepository timeEntryRepository = null;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {

        this.timeEntryRepository = timeEntryRepository;

    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long nonExistentTimeEntryId) {
        TimeEntry te = timeEntryRepository.find(nonExistentTimeEntryId);
        if(te!=null) {
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

        return new ResponseEntity(
                timeEntryRepository.list(),
                HttpStatus.OK
        );
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable("id") long timeEntryId, @RequestBody TimeEntry expected) {

        TimeEntry te = timeEntryRepository.update(timeEntryId, expected);
        if(te!=null) {
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
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {

        TimeEntry te = timeEntryRepository.create(timeEntryToCreate);
        return new ResponseEntity(te,HttpStatus.CREATED);
    }
}
