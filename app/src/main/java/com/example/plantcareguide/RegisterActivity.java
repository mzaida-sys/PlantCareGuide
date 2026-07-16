package com.example.plantcareguide;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etUsername, etEmail, etPassword;
    private Button btnRegister;
    private TextView tvGoToLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        etName = findViewById(R.id.etRegisterName);
        etUsername = findViewById(R.id.etRegisterUsername);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        btnRegister = findViewById(R.id.btnRegisterSubmit);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        dbHelper = new DatabaseHelper(this);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "الرجاء تعبئة جميع الحقول!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "كلمة المرور يجب أن تكون 6 خانات على الأقل!", Toast.LENGTH_SHORT).show();
            return;
        }


        if (dbHelper.isUsernameExists(username)) {
            Toast.makeText(this, "اسم المستخدم محجوز مسبقاً، اختر اسماً آخر!", Toast.LENGTH_SHORT).show();
            return;
        }


        boolean isSuccess = dbHelper.insertUser(name, username, email, password);
        if (isSuccess) {
            Toast.makeText(this, "تم إنشاء الحساب بنجاح! يمكنك الآن تسجيل الدخول.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "فشل إنشاء الحساب، يرجى المحاولة لاحقاً.", Toast.LENGTH_SHORT).show();
        }
    }
}