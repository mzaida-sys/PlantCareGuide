package com.example.plantcareguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvProfileName, tvProfileEmail, tvProfileRank;
    private Button btnProfileBack, btnProfileLogout;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("userId", -1);


        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvProfileRank = findViewById(R.id.tvProfileRank);
        btnProfileBack = findViewById(R.id.btnProfileBack);
        btnProfileLogout = findViewById(R.id.btnProfileLogout);


        loadUserData();


        if (btnProfileBack != null) {
            btnProfileBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


        if (btnProfileLogout != null) {
            btnProfileLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutUser();
                }
            });
        }
    }

    private void loadUserData() {
        Cursor cursor = dbHelper.getUserById(currentUserId);
        if (cursor != null && cursor.moveToFirst()) {

            String name = cursor.getString(1);
            String email = cursor.getString(3);

            tvProfileName.setText(name);
            tvProfileEmail.setText(email);


            Cursor plantsCursor = dbHelper.getPlantsByUserId(currentUserId);
            if (plantsCursor != null) {
                int plantCount = plantsCursor.getCount();
                if (plantCount >= 10) {
                    tvProfileRank.setText("كبير مزارعي الحديقة ");
                } else if (plantCount >= 5) {
                    tvProfileRank.setText("مزارع محترف ");
                } else if (plantCount >= 1) {
                    tvProfileRank.setText("صديق البيئة النشط ");
                } else {
                    tvProfileRank.setText("صديق البيئة المبتدئ");
                }
                plantsCursor.close();
            }

            cursor.close();
        } else {
            Toast.makeText(this, "فشل جلب بيانات الحساب!", Toast.LENGTH_SHORT).show();
        }
    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(ProfileActivity.this, "تم تسجيل الخروج بنجاح", Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}