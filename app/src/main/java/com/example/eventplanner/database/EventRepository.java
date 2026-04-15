package com.example.eventplanner.database;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.eventplanner.model.Event;
import java.util.List;
import java.util.concurrent.*;

public class EventRepository {

    private final EventDao eventDao;
    private final LiveData<List<Event>> allEvents;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public EventRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        eventDao = db.eventDao();
        allEvents = eventDao.getAllEventsSortedByDate();
    }

    public LiveData<List<Event>> getAllEvents() { return allEvents; }

    public void insert(Event event) {
        executor.execute(() -> eventDao.insert(event));
    }
    public void update(Event event) {
        executor.execute(() -> eventDao.update(event));
    }
    public void delete(Event event) {
        executor.execute(() -> eventDao.delete(event));
    }
}
