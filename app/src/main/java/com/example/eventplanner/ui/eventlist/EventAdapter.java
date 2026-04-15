package com.example.eventplanner.ui.eventlist;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eventplanner.R;
import com.example.eventplanner.model.Event;
import java.text.SimpleDateFormat;
import java.util.*;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    public interface OnEditClickListener { void onEditClick(Event event); }
    public interface OnDeleteClickListener { void onDeleteClick(Event event); }

    private List<Event> events = new ArrayList<>();
    private final OnEditClickListener editListener;
    private final OnDeleteClickListener deleteListener;
    private static final SimpleDateFormat FMT =
            new SimpleDateFormat("EEE, dd MMM yyyy  hh:mm a", Locale.getDefault());

    public EventAdapter(OnEditClickListener e, OnDeleteClickListener d) {
        editListener = e; deleteListener = d;
    }

    @NonNull @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder h, int position) {
        Event event = events.get(position);
        h.tvTitle.setText(event.getTitle());
        h.tvCategory.setText(event.getCategory());
        h.tvLocation.setText("\uD83D\uDCCD " + event.getLocation());
        h.tvDate.setText("\uD83D\uDCC5 " + FMT.format(new Date(event.getDateTimeMillis())));
        h.btnEdit.setOnClickListener(v -> editListener.onEditClick(event));
        h.btnDelete.setOnClickListener(v -> deleteListener.onDeleteClick(event));
    }

    @Override public int getItemCount() { return events.size(); }

    public void setEvents(List<Event> newEvents) {
        this.events = newEvents;
        notifyDataSetChanged();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvLocation, tvDate;
        ImageButton btnEdit, btnDelete;
        EventViewHolder(View v) {
            super(v);
            tvTitle    = v.findViewById(R.id.tv_event_title);
            tvCategory = v.findViewById(R.id.tv_event_category);
            tvLocation = v.findViewById(R.id.tv_event_location);
            tvDate     = v.findViewById(R.id.tv_event_date);
            btnEdit    = v.findViewById(R.id.btn_edit);
            btnDelete  = v.findViewById(R.id.btn_delete);
        }
    }
}

