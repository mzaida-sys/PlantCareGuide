package com.example.plantcareguide;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddPlantActivity extends AppCompatActivity {

    private EditText etPlantName, etWaterSchedule, etPlantNotes;
    private Spinner spCategory;
    private RadioGroup rgSunlight;
    private RadioButton rbIndirect, rbDirect;
    private Button btnSave, btnCancel;

    private DatabaseHelper dbHelper;
    private int currentUserId;
    private boolean isEditMode = false;
    private Plant plantToEdit;


    private final String[] categories = {"نباتات داخلية (Indoor)", "نباتات خارجية (Outdoor)", "الصباريات والعصاريات"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);


        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("userId", -1);

        dbHelper = new DatabaseHelper(this);


        etPlantName = findViewById(R.id.etAddPlantName);
        spCategory = findViewById(R.id.spAddPlantCategory);
        etWaterSchedule = findViewById(R.id.etAddWaterSchedule);
        rgSunlight = findViewById(R.id.rgSunlight);
        rbIndirect = findViewById(R.id.rbSunIndirect);
        rbDirect = findViewById(R.id.rbSunDirect);
        etPlantNotes = findViewById(R.id.etAddPlantNotes);
        btnSave = findViewById(R.id.btnSavePlant);
        btnCancel = findViewById(R.id.btnCancelAdd);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);


        isEditMode = getIntent().getBooleanExtra("edit_mode", false);
        if (isEditMode) {
            plantToEdit = (Plant) getIntent().getSerializableExtra("selected_plant");
            fillPlantDataForEdit();
        } else {

            String defaultCat = getIntent().getStringExtra("DEFAULT_CATEGORY");
            if (defaultCat != null) {
                for (int i = 0; i < categories.length; i++) {
                    if (categories[i].equals(defaultCat)) {
                        spCategory.setSelection(i);
                        break;
                    }
                }
            }
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlant();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void fillPlantDataForEdit() {
        btnSave.setText("💾 تحديث بيانات النبتة");
        etPlantName.setText(plantToEdit.getName());
        etWaterSchedule.setText(plantToEdit.getWaterSchedule());
        etPlantNotes.setText(plantToEdit.getNotes());


        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(plantToEdit.getCategory())) {
                spCategory.setSelection(i);
                break;
            }
        }


        if (plantToEdit.getSunlight().equals("شمس مباشرة")) {
            rbDirect.setChecked(true);
        } else {
            rbIndirect.setChecked(true);
        }
    }


    private void savePlant() {
        String name = etPlantName.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();
        String water = etWaterSchedule.getText().toString().trim();
        String notes = etPlantNotes.getText().toString().trim();


        String sunlight = "ظل / غير مباشر";
        if (rgSunlight.getCheckedRadioButtonId() == R.id.rbSunDirect) {
            sunlight = "شمس مباشرة";
        }

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(water)) {
            Toast.makeText(this, "الرجاء تعبئة الحقول الأساسية (الاسم والري)!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode) {

            boolean updated = dbHelper.updatePlant(plantToEdit.getId(), name, category, water, sunlight, notes);
            if (updated) {
                Toast.makeText(this, "تم تحديث بيانات النبتة بنجاح!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "فشل في تحديث البيانات!", Toast.LENGTH_SHORT).show();
            }
        } else {

            boolean inserted = dbHelper.insertPlant(name, category, water, sunlight, notes, currentUserId);
            if (inserted) {
                Toast.makeText(this, "تم إضافة النبتة الجديدة بنجاح!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "حدث خطأ أثناء الإضافة!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}