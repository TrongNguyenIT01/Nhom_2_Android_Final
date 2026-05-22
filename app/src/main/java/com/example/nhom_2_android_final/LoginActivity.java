package com.example.nhom_2_android_final;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.nhom_2_android_final.Model.Login;
import com.example.nhom_2_android_final.database.entity.User;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etIdentifier, etPassword;
    private Login loginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);

        // Khởi tạo Model
        loginModel = new Login(this);

        initViews();

        findViewById(R.id.tvRegister).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.tvForgotPassword).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });


        findViewById(R.id.btnLogin).setOnClickListener(v -> performLogin());
    }

    private void initViews() {
        etIdentifier = findViewById(R.id.etIdentifier);
        etPassword = findViewById(R.id.etPassword);

    }

    private void performLogin() {
        if (etIdentifier.getText() == null || etPassword.getText() == null) return;

        String identifier = etIdentifier.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        loginModel.executeLogin(identifier, password, new Login.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                runOnUiThread(() -> {
                    // Lưu UserID vào SharedPreferences để dùng cho các chức năng khác (như đổi mật khẩu)
                    SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    sharedPreferences.edit().putString("CurrentUserID", user.UserID).apply();

                    Toast.makeText(LoginActivity.this, "Chào mừng " + user.HoTen, Toast.LENGTH_SHORT).show();
                    
                    // Chuyển sang MainActivity và truyền dữ liệu qua Intent
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USER_ID", user.UserID);
                    intent.putExtra("USER_NAME", user.HoTen);
                    intent.putExtra("USER_GRADE", user.KhoiLop);


                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
