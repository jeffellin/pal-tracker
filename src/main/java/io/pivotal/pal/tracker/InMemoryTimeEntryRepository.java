package io.pivotal.pal.tracker;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    public InMemoryTimeEntryRepository() {
        data = new HashMap<Long, TimeEntry>();
        l = 1L;
    }

    public  HashMap<Long,TimeEntry>  data;

    public  Long l ;

    public TimeEntry find(long timeEntryId) {
        return data.get(timeEntryId);
    }

    public TimeEntry create(TimeEntry timeEntry){

        timeEntry.setId(l++);
        data.put(timeEntry.getId(),timeEntry);
        return data.get(timeEntry.getId());

    }

    public List<TimeEntry> list() {
        return data.values().stream().collect(Collectors.toList());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        if(data.get(id)==null){
            return null;
        }
        timeEntry.setId(id);
        data.put(timeEntry.getId(),timeEntry);
        return data.get(timeEntry.getId());
    }

    public void delete(long id) {

        data.remove(id);

    }
}
