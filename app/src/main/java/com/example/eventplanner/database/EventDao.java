package com.example.eventplanner.database;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.eventplanner.model.Event;
import java.util.List;

@Dao
public interface EventDao {

    @Insert
    void insert(Event event);

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);

    @Query("SELECT * FROM events ORDER BY dateTimeMillis ASC")
    LiveData<List<Event>> getAllEventsSortedByDate();
}
