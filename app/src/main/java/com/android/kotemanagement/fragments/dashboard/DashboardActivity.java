package com.android.kotemanagement.fragments.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.kotemanagement.R;
import com.android.kotemanagement.databinding.ActivityDashboardBinding;
import com.android.kotemanagement.room.entities.RecordAction;
import com.android.kotemanagement.room.entities.Records;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;


public class DashboardActivity extends Fragment {

    private ActivityDashboardBinding binding;
    private RecordsViewModel recordsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityDashboardBinding.inflate(inflater, container, false);

        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);

        recordsViewModel.getInventoryRecords().observe(getViewLifecycleOwner(), records -> {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            binding.dailyPieChart.getDescription().setEnabled(false);
            binding.monthlyPieChart.getDescription().setEnabled(false);
            binding.weeklyPieChart.getDescription().setEnabled(false);

            List<Records> parsedRecords = records.stream().filter(record -> {
                // Parse the date string and filter based on your condition
                LocalDateTime parsedDate = LocalDateTime.parse(record.getDate(), formatter);
                return parsedDate.toLocalDate().isEqual(LocalDate.now()); // Example: filter today's records
            }).collect(Collectors.toList());

            // Filter for daily, weekly, and monthly records
            List<Records> dailyRecords = filterRecords(parsedRecords, LocalDateTime.now().minusDays(1));
            List<Records> weeklyRecords = filterRecords(parsedRecords, LocalDateTime.now().minusWeeks(1));
            List<Records> monthlyRecords = filterRecords(parsedRecords, LocalDateTime.now().minusMonths(1));

            // Set the date range text
            setDateRangeText();

            // Create and setup pie charts for each time range
            setupPieChart(dailyRecords, binding.dailyPieChart, "Daily Inventory");
            setupPieChart(weeklyRecords, binding.weeklyPieChart, "Weekly Inventory");
            setupPieChart(monthlyRecords, binding.monthlyPieChart, "Monthly Inventory");
        });

        return binding.getRoot();
    }

    private void setDateRangeText() {
        // Today's date
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        binding.dailyDateRange.setText("Today: " + today);

        // Weekly range (e.g., last week)
        LocalDate startOfWeek = LocalDate.now().minusDays(7);
        String weekRange = startOfWeek.format(DateTimeFormatter.ofPattern("dd MMM")) + " - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM"));
        binding.weeklyDateRange.setText("Week: " + weekRange);

        // Monthly range (e.g., current month)
        String monthRange = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM yyyy"));
        binding.monthlyDateRange.setText("Month: " + monthRange);
    }

    private List<Records> filterRecords(List<Records> records, LocalDateTime cutoffDate) {
        return records.stream().filter(record -> parseDate(record.getDate()).isAfter(cutoffDate)).collect(Collectors.toList());
    }

    private void setupPieChart(List<Records> records, PieChart chart, String centerText) {
        float issuedWeaponsSize = 0;
        float returnedWeapons = 0;

        for (Records record : records) {
            if (record.getAction().equals(RecordAction.INVENTORY_ADDED)) {
                issuedWeaponsSize += 1.0F;
            } else if (record.getAction().equals(RecordAction.INVENTORY_RETURNED)) {
                returnedWeapons += 1.0F;
            }
        }

        List<PieEntry> pieEntries = new ArrayList<>();
        if (issuedWeaponsSize > 0 || returnedWeapons > 0) {
            pieEntries.add(new PieEntry(issuedWeaponsSize, "Issued Weapons"));
            pieEntries.add(new PieEntry(returnedWeapons, "Returned Weapons"));
        } else {
            pieEntries.add(new PieEntry(1, "No Data"));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(ContextCompat.getColor(requireContext(), R.color.primaryColor), ContextCompat.getColor(requireContext(), R.color.red));

        PieData data = new PieData(dataSet);
        data.setValueTextSize(12f);
        data.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.black));

        chart.setData(data);
        chart.setCenterText(centerText);
        chart.setCenterTextSize(18f);
        chart.setDrawEntryLabels(false);
        chart.setContentDescription(centerText);
        chart.setHoleRadius(75f);
        chart.setTransparentCircleRadius(80f);
        chart.setRotationEnabled(true);

        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(10f);
        legend.setFormSize(10);
        legend.setFormToTextSpace(10);

        // Refresh the chart
        chart.invalidate();
    }

    private LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    }
}

