package com.example.plantcareguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class CategoryPlantsActivity extends AppCompatActivity {

    private TextView tvCategoryHeader;
    private RecyclerView rvCategoryPlants;
    private FloatingActionButton fabAddPlant;

    private PlantAdapter adapter;
    private ArrayList<Plant> plantList;
    private DatabaseHelper dbHelper;
    private String categoryName;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_plants);


        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("userId", -1);

        dbHelper = new DatabaseHelper(this);
        plantList = new ArrayList<>();

        categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        tvCategoryHeader = findViewById(R.id.tvCategoryHeader);
        rvCategoryPlants = findViewById(R.id.rvCategoryPlants);
        fabAddPlant = findViewById(R.id.fabAddPlant);

        tvCategoryHeader.setText(categoryName);
        rvCategoryPlants.setLayoutManager(new LinearLayoutManager(this));


        fabAddPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryPlantsActivity.this, AddPlantActivity.class);
                intent.putExtra("DEFAULT_CATEGORY", categoryName);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategoryPlants();
    }

    private void loadCategoryPlants() {
        plantList.clear();
        Cursor cursor = dbHelper.getPlantsByUserIdAndCategory(currentUserId, categoryName);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String category = cursor.getString(2);
                String water = cursor.getString(3);
                String sun = cursor.getString(4);
                String notes = cursor.getString(5);

                plantList.add(new Plant(id, name, category, water, sun, notes, currentUserId));
            }
            cursor.close();
        }

        adapter = new PlantAdapter(this, plantList);
        rvCategoryPlants.setAdapter(adapter);
    }
}