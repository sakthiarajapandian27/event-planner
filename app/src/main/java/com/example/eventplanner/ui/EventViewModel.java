package com.example.eventplanner.ui;

import android.app.Application;
import androidx.lifecycle.*;
import com.example.eventplanner.database.EventRepository;
import com.example.eventplanner.model.Event;
import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private final EventRepository repository;
    private final LiveData<List<Event>> allEvents;

    public EventViewModel(Application application) {
        super(application);
        repository = new EventRepository(application);
        allEvents = repository.getAllEvents();
    }
    public LiveData<List<Event>> getAllEvents() { return allEvents; }
    public void insert(Event e) { repository.insert(e); }
    public void update(Event e) { repository.update(e); }
    public void delete(Event e) { repository.delete(e); }
}
