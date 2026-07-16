package com.example.plantcareguide;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PlantDetailActivity extends AppCompatActivity {

    private TextView tvName, tvCategory, tvWater, tvSun, tvNotes;
    private Button btnBack;
    private Plant selectedPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);


        tvName = findViewById(R.id.tvDetailName);
        tvCategory = findViewById(R.id.tvDetailCategory);
        tvWater = findViewById(R.id.tvDetailWater);
        tvSun = findViewById(R.id.tvDetailSun);
        tvNotes = findViewById(R.id.tvDetailNotes);
        btnBack = findViewById(R.id.btnBack);


        selectedPlant = (Plant) getIntent().getSerializableExtra("selected_plant");

        if (selectedPlant != null) {
            tvName.setText(selectedPlant.getName());
            tvCategory.setText("الفئة: " + selectedPlant.getCategory());
            tvWater.setText("جدول الري: " + selectedPlant.getWaterSchedule());
            tvSun.setText("الإضاءة المطلوبة: " + selectedPlant.getSunlight());

            if (!TextUtils.isEmpty(selectedPlant.getNotes())) {
                tvNotes.setText(selectedPlant.getNotes());
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}