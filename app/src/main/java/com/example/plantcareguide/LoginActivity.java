package com.example.plantcareguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginInput, etPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin;
    private TextView tvGoToRegister;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isRemembered = sharedPreferences.getBoolean("rememberMe", false);
        int savedUserId = sharedPreferences.getInt("userId", -1);

        if (isRemembered && savedUserId != -1) {
            goToMainActivity();
            return;
        }

        setContentView(R.layout.activity_login);


        etLoginInput = findViewById(R.id.etLoginIdentifier);
        etPassword = findViewById(R.id.etLoginPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLoginSubmit);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        dbHelper = new DatabaseHelper(this);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });


        tvGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String loginInput = etLoginInput.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(loginInput) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "الرجاء إدخال اسم المستخدم وكلمة المرور!", Toast.LENGTH_SHORT).show();
            return;
        }


        int userId = dbHelper.checkUser(loginInput, password);

        if (userId != -1) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userId", userId);

            editor.putBoolean("rememberMe", cbRememberMe.isChecked());
            editor.apply();

            Toast.makeText(this, "تم تسجيل الدخول بنجاح!", Toast.LENGTH_SHORT).show();
            goToMainActivity();
        } else {
            Toast.makeText(this, "اسم المستخدم أو كلمة المرور غير صحيحة!", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToMainActivity() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}