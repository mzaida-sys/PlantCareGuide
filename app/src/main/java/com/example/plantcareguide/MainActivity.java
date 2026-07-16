package com.example.plantcareguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcomeUser, tvTotalPlantsCount, tvWaterTodayCount;
    private ImageButton btnMainProfile;
    private Button btnCatIndoor, btnCatOutdoor, btnCatSucculents;

    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("userId", -1);
        if (currentUserId == -1) {
            goToLogin();
            return;
        }

        dbHelper = new DatabaseHelper(this);


        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        tvTotalPlantsCount = findViewById(R.id.tvTotalPlantsCount);
        tvWaterTodayCount = findViewById(R.id.tvWaterTodayCount);
        btnMainProfile = findViewById(R.id.btnMainProfile);

        btnCatIndoor = findViewById(R.id.btnCatIndoor);
        btnCatOutdoor = findViewById(R.id.btnCatOutdoor);
        btnCatSucculents = findViewById(R.id.btnCatSucculents);


        displayWelcomeMessage();

        btnMainProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });


        btnCatIndoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryActivity("نباتات داخلية (Indoor)");
            }
        });

        btnCatOutdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryActivity("نباتات خارجية (Outdoor)");
            }
        });

        btnCatSucculents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryActivity("الصباريات والعصاريات");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDashboardStats();
    }

    private void displayWelcomeMessage() {
        Cursor cursor = dbHelper.getUserById(currentUserId);
        if (cursor != null && cursor.moveToFirst()) {
            String fullName = cursor.getString(1);
            tvWelcomeUser.setText("أهلاً بك، " + fullName + "! 👋");
            cursor.close();
        }
    }

    private void updateDashboardStats() {
        Cursor cursor = dbHelper.getPlantsByUserId(currentUserId);
        int totalPlants = 0;
        int waterToday = 0;

        if (cursor != null) {
            totalPlants = cursor.getCount();

            while (cursor.moveToNext()) {
                String waterSchedule = cursor.getString(3); // COL_PLANT_WATER
                if (waterSchedule.contains("يوم") || waterSchedule.contains("1") || waterSchedule.contains("اليوم")) {
                    waterToday++;
                }
            }
            cursor.close();
        }

        tvTotalPlantsCount.setText(String.valueOf(totalPlants));
        tvWaterTodayCount.setText(String.valueOf(waterToday));
    }

    private void openCategoryActivity(String categoryName) {
        Intent intent = new Intent(MainActivity.this, CategoryPlantsActivity.class);
        intent.putExtra("CATEGORY_NAME", categoryName);
        startActivity(intent);
    }

    private void goToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}