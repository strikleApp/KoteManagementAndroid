package com.android.kotemanagement.fragments.records;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AllRecordsFragment extends Fragment {
    private FragmentAllRecordsBinding binding;
    private RecordsAdapter adapter;
    private RecordsViewModel recordsViewModel;
    private List<Records> cachedRecords = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAllRecordsBinding.inflate(inflater, container, false);
        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);
        adapter = new RecordsAdapter(getContext());
        binding.rvAllRecords.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvAllRecords.addItemDecoration(new ItemOffsetDecoration(16));

        // Observe LiveData for records
        recordsViewModel.getAllRecords().observe(getViewLifecycleOwner(), records -> {
            if (records != null) {
                cachedRecords.clear();
                cachedRecords.addAll(records);
                adapter.setRecordsList(records);
                binding.rvAllRecords.setAdapter(adapter);
                // binding.tvEmptyMessage.setVisibility(records.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        binding.etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterRecords(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.btnStartDate.setOnClickListener(view -> showDatePickerDialog(binding.btnStartDate, true));
        binding.btnEndDate.setOnClickListener(view -> showDatePickerDialog(binding.btnEndDate, false));
        binding.btnApplyClear.setOnClickListener(v -> onApplyClearClick());
        binding.btnSort.setOnClickListener(view -> {
            binding.etSearchUser.setText("");
            binding.clSearchAndSort.setVisibility(View.GONE);
            binding.clFilter.setVisibility(View.VISIBLE);
        });

        return binding.getRoot();
    }

    private void filterRecords(String query) {
        recordsViewModel.getAllRecords().observe(getViewLifecycleOwner(), records -> {
            List<Records> filteredRecords = records.stream()
                    .filter(record ->
                            (record.getName() != null && record.getName().toLowerCase().contains(query.toLowerCase())) ||
                                    (record.getRank() != null && record.getRank().toLowerCase().contains(query.toLowerCase())) ||
                                    (record.getSno() != null && record.getSno().toLowerCase().contains(query.toLowerCase())) ||
                                    (record.getWeaponName() != null && record.getWeaponName().toLowerCase().contains(query.toLowerCase())) ||
                                    (record.getArmyNumber() != null && record.getArmyNumber().toLowerCase().contains(query.toLowerCase())) ||
                                    (record.getDate() != null && record.getDate().toLowerCase().contains(query.toLowerCase())) ||
                                    (record.getType() != null && record.getType().toString().toLowerCase().contains(query.toLowerCase())) ||
                                    (record.getAction() != null && record.getAction().toString().toLowerCase().contains(query.toLowerCase()))
                    )
                    .collect(Collectors.toList());

            adapter.setRecordsList(filteredRecords);
            adapter.notifyDataSetChanged();
        });
    }


    private void showDatePickerDialog(Button button, boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(selectedYear, selectedMonth, selectedDay);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = sdf.format(selectedDate.getTime());
            button.setText(formattedDate);

            if (!isStartDate && !isEndDateValid()) {
                Toast.makeText(getContext(), "End date cannot be earlier than start date", Toast.LENGTH_SHORT).show();
                button.setText(isStartDate ? "Start Date" : "End Date");
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private boolean isEndDateValid() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date startDate = sdf.parse(binding.btnStartDate.getText().toString());
            Date endDate = sdf.parse(binding.btnEndDate.getText().toString());
            return startDate != null && endDate != null && !endDate.before(startDate);
        } catch (ParseException e) {
            return false;
        }
    }


    public void onApplyClearClick() {
        if (binding.btnApplyClear.getText().toString().equals("Apply")) {
            String startDate = binding.btnStartDate.getText().toString();
            String endDate = binding.btnEndDate.getText().toString();

            if (!"Start Date".equals(startDate) && !"End Date".equals(endDate)) {
                if (cachedRecords.isEmpty()) {
                    recordsViewModel.getAllRecords().observe(getViewLifecycleOwner(), records -> {
                        if (records != null) {
                            cachedRecords.clear();
                            cachedRecords.addAll(records);
                        }
                    });
                }

                try {
                    List<Records> filteredByDate = filterRecordsByDateRange(cachedRecords, startDate, endDate);
                    adapter.setRecordsList(filteredByDate);
                    adapter.notifyDataSetChanged();
                    binding.btnApplyClear.setText("Clear");
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Invalid date range or format.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please select both start and end dates.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Reset to original records
            adapter.setRecordsList(cachedRecords);
            adapter.notifyDataSetChanged();
            binding.btnApplyClear.setText("Apply");
            binding.clSearchAndSort.setVisibility(View.VISIBLE);
            binding.clFilter.setVisibility(View.GONE);
            binding.btnStartDate.setText("Start Date");
            binding.btnEndDate.setText("End Date");
        }
    }


    private List<Records> filterRecordsByDateRange(List<Records> records, String startDate, String endDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // Match the format used by LocalDateTime.toString()
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Format for the input startDate and endDate
        LocalDate start = LocalDate.parse(startDate, dateFormatter);
        LocalDate end = LocalDate.parse(endDate, dateFormatter);

        // Filter records based on date range
        return records.stream()
                .filter(record -> {
                    try {
                        LocalDateTime recordDateTime = LocalDateTime.parse(record.getDate(), dateTimeFormatter);
                        LocalDate recordDate = recordDateTime.toLocalDate(); // Extract LocalDate for comparison
                        return (recordDate.isEqual(start) || recordDate.isAfter(start)) &&
                                (recordDate.isEqual(end) || recordDate.isBefore(end));
                    } catch (DateTimeParseException e) {
                        e.printStackTrace(); // Log the issue
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }


    private LocalDateTime safeParseDate(String dateStr, DateTimeFormatter formatter) {
        try {
            return LocalDateTime.parse(dateStr, formatter);
        } catch (Exception e) {
            Log.e("Date Parsing", "Failed to parse date: " + dateStr, e);
            return null;
        }
    }
}
