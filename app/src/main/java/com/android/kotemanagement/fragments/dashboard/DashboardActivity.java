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
import com.android.kotemanagement.room.viewmodel.IssueWeaponsViewModel;
import com.android.kotemanagement.room.viewmodel.RecordsViewModel;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends Fragment {

    private ActivityDashboardBinding binding;
    private IssueWeaponsViewModel issueWeaponsViewModel;
    private RecordsViewModel recordsViewModel;

    // private int previousSize = 0;
    private int inventorySize;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = ActivityDashboardBinding.inflate(inflater, container, false);

        recordsViewModel = new ViewModelProvider(this).get(RecordsViewModel.class);
        recordsViewModel.getInventoryRecords().observe(getViewLifecycleOwner(), records -> {
            inventorySize = records.size();
             float returnedWeapons = 0;
             float issuedWeaponsSize = 0;
            for (Records record : records) {
                if (record.getAction().equals(RecordAction.INVENTORY_ADDED)) {
                    issuedWeaponsSize += 1.0F;
                } else if (record.getAction().equals(RecordAction.INVENTORY_RETURNED)) {
                    returnedWeapons += 1.0F;

                }
            }
            setupPieChart(issuedWeaponsSize,returnedWeapons);
        });





        return binding.getRoot();
    }

    private void setupPieChart(float issuedWeaponsSize , float returnedWeapons) {
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(issuedWeaponsSize, "Issued Weapons"));
        pieEntries.add(new PieEntry(returnedWeapons, "Returned Weapons"));

        if (issuedWeaponsSize == 0 && returnedWeapons == 0) {
            // If no data is available, add placeholder data
            pieEntries.add(new PieEntry(1, "No Data"));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(ContextCompat.getColor(requireContext(), R.color.primaryColor),
                ContextCompat.getColor(requireContext(), R.color.red));

        PieData data = new PieData(dataSet);
        data.setValueTextSize(12f);
        data.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.black));

        binding.pieChart.setData(data);
        binding.pieChart.setCenterText("Inventory Status");
        binding.pieChart.setCenterTextSize(18f);
        binding.pieChart.setDrawEntryLabels(false);
        binding.pieChart.setContentDescription("Total issued and returned weapons");
        binding.pieChart.setHoleRadius(75f);
        binding.pieChart.setTransparentCircleRadius(80f);
        binding.pieChart.setRotationEnabled(true);

        Legend legend = binding.pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(12f);
        legend.setFormSize(20);
        legend.setFormToTextSpace(12);

        // Refresh the chart
        binding.pieChart.invalidate();
    }
}
