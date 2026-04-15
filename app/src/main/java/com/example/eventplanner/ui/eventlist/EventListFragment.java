package com.example.eventplanner.ui.eventlist;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.*;
import com.example.eventplanner.R;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.ui.EventViewModel;
import com.google.android.material.snackbar.Snackbar;

public class EventListFragment extends Fragment {

    private EventViewModel viewModel;
    private EventAdapter adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.recycler_events);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(this::onEditClicked, this::onDeleteClicked);
        rv.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        viewModel.getAllEvents().observe(getViewLifecycleOwner(), events -> {
            adapter.setEvents(events);
            view.findViewById(R.id.tv_empty).setVisibility(
                    events.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    private void onEditClicked(Event event) {
        Bundle b = new Bundle();
        b.putInt("eventId", event.getId());
        b.putString("title", event.getTitle());
        b.putString("category", event.getCategory());
        b.putString("location", event.getLocation());
        b.putLong("dateTimeMillis", event.getDateTimeMillis());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_eventListFragment_to_addEventFragment, b);
    }

    private void onDeleteClicked(Event event) {
        viewModel.delete(event);
        Snackbar.make(requireView(),
                "\"" + event.getTitle() + "\" deleted",
                Snackbar.LENGTH_SHORT).show();
    }
}
