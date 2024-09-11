package com.android.kotemanagement.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.kotemanagement.R;
import com.android.kotemanagement.adapter.ViewSoldierAdapter;
import com.android.kotemanagement.databinding.ActivityViewSoldiersBinding;
import com.android.kotemanagement.room.viewmodel.SoldiersViewModel;

public class ViewSoldiersActivity extends AppCompatActivity {

    private ActivityViewSoldiersBinding binding;
    private SoldiersViewModel soldiersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityViewSoldiersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        soldiersViewModel = new ViewModelProvider(this).get(SoldiersViewModel.class);

        binding.rvSoldiers.setLayoutManager(new LinearLayoutManager(this));
        ViewSoldierAdapter adapter = new ViewSoldierAdapter();
        binding.rvSoldiers.setAdapter(adapter);

        soldiersViewModel.getAllSoldiersList().observe(this, soldiersList -> {
            adapter.setSoldiersList(soldiersList);
            adapter.notifyDataSetChanged();
        });


    }
}