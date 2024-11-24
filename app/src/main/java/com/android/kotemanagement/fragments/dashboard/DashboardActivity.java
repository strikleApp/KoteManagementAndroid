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
import com.android.kotemanagement.room.viewmodel.IssueWeaponsViewModel;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends Fragment {

    private ActivityDashboardBinding binding;
    private IssueWeaponsViewModel issueWeaponsViewModel;
    private int returnedWeapons = 0;
    private int issuedWeaponsSize;
    private int previousSize = 0;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = ActivityDashboardBinding.inflate(inflater, container, false);

        issueWeaponsViewModel = new ViewModelProvider(this).get(IssueWeaponsViewModel.class);
        issueWeaponsViewModel.getAllIssuedWeaponsList().observe(getViewLifecycleOwner(), issuedWeaponsList -> {
            issuedWeaponsSize = issuedWeaponsList.size();
            if(previousSize > issuedWeaponsSize) {
                returnedWeapons = previousSize - issuedWeaponsSize;
                previousSize = issuedWeaponsSize;
            } else {
                previousSize = issuedWeaponsSize;
            }

        });

        setupPieChart();


        return binding.getRoot();
    }

    private void setupPieChart() {
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(10, "Issued Weapons"));
        pieEntries.add(new PieEntry(5, "Returned Weapons"));

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(ContextCompat.getColor(requireContext(), R.color.primaryColor)
                , ContextCompat.getColor(requireContext(), R.color.red));
        PieData data = new PieData(dataSet);

        binding.pieChart.setData(data);
        binding.pieChart.invalidate();
        binding.pieChart.setCenterText("Inventory Status");
        binding.pieChart.setDrawEntryLabels(false);
        binding.pieChart.setContentDescription("Total issued and returned weapons");
        binding.pieChart.setEntryLabelTextSize(16f);
        binding.pieChart.setHoleRadius(75f);

        Legend legend = binding.pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(16f);
        legend.setFormSize(20);
        legend.setFormToTextSpace(12);

    }
}
