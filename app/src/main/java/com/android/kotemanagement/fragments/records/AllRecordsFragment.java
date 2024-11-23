package com.android.kotemanagement.fragments.records;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.kotemanagement.adapter.ItemOffsetDecoration;
import com.android.kotemanagement.adapter.RecordsAdapter;
import com.android.kotemanagement.databinding.FragmentAllRecordsBinding;
import com.android.kotemanagement.room.entities.Records;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AllRecordsFragment extends Fragment {
    private FragmentAllRecordsBinding binding;
    private RecordsAdapter adapter;
    private RecordsViewModel recordsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAllRecordsBinding.inflate(inflater, container, false);
        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);
        adapter = new RecordsAdapter(getContext());
        binding.rvAllRecords.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvAllRecords.addItemDecoration(new ItemOffsetDecoration(16));

        recordsViewModel.getAllRecords().observe(getViewLifecycleOwner(), records -> {
            if (records != null && !records.isEmpty()) {
                adapter.setRecordsList(records);
                binding.rvAllRecords.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Log.e("All Records Fragment", "No records found.");
            }

        });
        binding.btnStartDate.setOnClickListener(view -> onStartDateClick());
        binding.btnEndDate.setOnClickListener(view -> onEndDateClick());
        binding.btnApplyClear.setOnClickListener(v -> onApplyClearClick());
        binding.btnSort.setOnClickListener(view -> {
            binding.clSearchAndSort.setVisibility(View.GONE); // Hide the menu
            binding.clFilter.setVisibility(View.VISIBLE);

        });

        return binding.getRoot();
    }

    public void onStartDateClick() {
        showDatePickerDialog(binding.btnStartDate, true);
    }

    public void onEndDateClick() {
        showDatePickerDialog(binding.btnEndDate, false);
    }

    private boolean isEndDateValid() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date startDate = sdf.parse(binding.btnStartDate.getText().toString());
            Date endDate = sdf.parse(binding.btnEndDate.getText().toString());
            return !endDate.before(startDate);
        } catch (ParseException e) {
            return false;
        }
    }

    private void showDatePickerDialog(Button button, boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDate = sdf.format(selectedDate.getTime());

                    // Update button text with the selected date
                    button.setText(formattedDate);

                    // Perform date validation if necessary
                    if (!isStartDate && !isEndDateValid()) {
                        Toast.makeText(getContext(), "End date cannot be earlier than start date", Toast.LENGTH_SHORT).show();
                        button.setText("End Date");
                    }
                },
                year, month, dayOfMonth
        );

        datePickerDialog.show();
    }

    public void onApplyClearClick() {
        if (binding.btnApplyClear.getText().toString().equals("Apply")) {
            binding.btnApplyClear.setText("Clear");
            // Logic to apply filters goes here
        } else {
            binding.btnApplyClear.setText("Apply");
            binding.clSearchAndSort.setVisibility(View.VISIBLE);
            binding.clFilter.setVisibility(View.GONE);
            binding.btnStartDate.setText("Start Date");
            binding.btnEndDate.setText("End Date");
        }
    }

    // Method to filter records between startDate and endDate
    public List<Records> filterRecordsByDateRange(List<Records> records, String startDateStr, String endDateStr) {
        // Define DateTimeFormatter to convert strings to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        // Convert startDate and endDate from String to LocalDateTime
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

        // Filter records that fall within the given date range
        return records.stream()
                .filter(record -> {
                    // Parse the record's date (assumed to be stored as a String in ISO_LOCAL_DATE_TIME format)
                    LocalDateTime recordDate = LocalDateTime.parse(record.getDate(), formatter);

                    // Check if recordDate is between startDate and endDate (inclusive)
                    return !recordDate.isBefore(startDate) && !recordDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
    }
}
