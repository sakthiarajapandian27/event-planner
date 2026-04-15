package com.example.eventplanner.ui.addevent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.ui.EventViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventFragment extends Fragment {

    private EditText etTitle, etLocation;
    private Spinner spinnerCategory;
    private TextView tvSelectedDateTime;
    private EventViewModel viewModel;
    private final Calendar selectedCalendar = Calendar.getInstance();
    private boolean dateWasPicked = false;
    private int editingEventId = 0;

    private static final SimpleDateFormat FMT =
            new SimpleDateFormat("EEE, dd MMM yyyy  hh:mm a", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTitle           = view.findViewById(R.id.et_title);
        etLocation        = view.findViewById(R.id.et_location);
        spinnerCategory   = view.findViewById(R.id.spinner_category);
        tvSelectedDateTime = view.findViewById(R.id.tv_selected_datetime);

        viewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        ArrayAdapter<CharSequence> sa = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.event_categories,
                android.R.layout.simple_spinner_item);
        sa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(sa);

        Bundle args = getArguments();
        if (args != null && args.containsKey("eventId")) {
            editingEventId = args.getInt("eventId");
            etTitle.setText(args.getString("title"));
            etLocation.setText(args.getString("location"));

            String[] cats = getResources().getStringArray(R.array.event_categories);
            String savedCat = args.getString("category");
            for (int i = 0; i < cats.length; i++) {
                if (cats[i].equals(savedCat)) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }
            selectedCalendar.setTimeInMillis(args.getLong("dateTimeMillis"));
            tvSelectedDateTime.setText(FMT.format(selectedCalendar.getTime()));
            dateWasPicked = true;
            ((Button) view.findViewById(R.id.btn_save_event)).setText("Update Event");
        }

        view.findViewById(R.id.btn_pick_datetime).setOnClickListener(v -> showDatePicker());
        view.findViewById(R.id.btn_save_event).setOnClickListener(v -> saveEvent(view));
    }

    private void showDatePicker() {
        new DatePickerDialog(requireContext(),
                (dp, year, month, day) -> {
                    selectedCalendar.set(year, month, day);
                    new TimePickerDialog(requireContext(),
                            (tp, hour, minute) -> {
                                selectedCalendar.set(Calendar.HOUR_OF_DAY, hour);
                                selectedCalendar.set(Calendar.MINUTE, minute);
                                selectedCalendar.set(Calendar.SECOND, 0);
                                dateWasPicked = true;
                                tvSelectedDateTime.setText(FMT.format(selectedCalendar.getTime()));
                            },
                            selectedCalendar.get(Calendar.HOUR_OF_DAY),
                            selectedCalendar.get(Calendar.MINUTE),
                            false).show();
                },
                selectedCalendar.get(Calendar.YEAR),
                selectedCalendar.get(Calendar.MONTH),
                selectedCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveEvent(View view) {
        String title    = etTitle.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (TextUtils.isEmpty(title)) {
            Snackbar.make(view, "Title cannot be empty!", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (!dateWasPicked) {
            Snackbar.make(view, "Please pick a date and time!", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (editingEventId == 0 &&
                selectedCalendar.getTimeInMillis() < System.currentTimeMillis()) {
            Snackbar.make(view, "Date cannot be in the past!", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(location)) {
            location = "No location set";
        }

        if (editingEventId == 0) {
            viewModel.insert(new Event(title, category, location,
                    selectedCalendar.getTimeInMillis()));
            Snackbar.make(view, "Event added!", Snackbar.LENGTH_SHORT).show();
        } else {
            Event updated = new Event(title, category, location,
                    selectedCalendar.getTimeInMillis());
            updated.setId(editingEventId);
            viewModel.update(updated);
            Snackbar.make(view, "Event updated!", Snackbar.LENGTH_SHORT).show();
        }
        Navigation.findNavController(view).navigateUp();
    }
}