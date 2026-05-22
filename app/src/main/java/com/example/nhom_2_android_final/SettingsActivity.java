package com.example.nhom_2_android_final;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.nhom_2_android_final.database.AppDatabase;
import com.example.nhom_2_android_final.database.entity.User;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.concurrent.Executors;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Dark Mode
        SwitchMaterial switchDarkMode = findViewById(R.id.switchDarkMode);
        boolean isDarkMode = sharedPreferences.getBoolean("DarkMode", false);
        switchDarkMode.setChecked(isDarkMode);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                sharedPreferences.edit().putBoolean("DarkMode", true).apply();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                sharedPreferences.edit().putBoolean("DarkMode", false).apply();
            }
        });

        // Font Size
        findViewById(R.id.btnSmall).setOnClickListener(v -> {
            Toast.makeText(this, "Đã chọn kích thước chữ Nhỏ", Toast.LENGTH_SHORT).show();
            saveFontSize("small");
        });

        findViewById(R.id.btnMedium).setOnClickListener(v -> {
            Toast.makeText(this, "Đã chọn kích thước chữ Vừa", Toast.LENGTH_SHORT).show();
            saveFontSize("medium");
        });

        findViewById(R.id.btnLarge).setOnClickListener(v -> {
            Toast.makeText(this, "Đã chọn kích thước chữ Lớn", Toast.LENGTH_SHORT).show();
            saveFontSize("large");
        });

        // Chọn lại khối lớp
        findViewById(R.id.cardChangeGrade).setOnClickListener(v -> {
            String[] grades = {"Khối 10", "Khối 11", "Khối 12"};
            new AlertDialog.Builder(this)
                    .setTitle("Chọn lại khối lớp")
                    .setItems(grades, (dialog, which) -> {
                        int newGrade = 10 + which;
                        updateUserGrade(newGrade);
                    })
                    .show();
        });

        // Change Password navigation
        findViewById(R.id.cardChangePassword).setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });
    }

    private void updateUserGrade(int newGrade) {
        String userId = sharedPreferences.getString("CurrentUserID", "");
        if (userId.isEmpty()) return;

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            User user = db.userDao().getUserById(userId);
            if (user != null) {
                user.KhoiLop = newGrade;
                db.userDao().update(user);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Đã đổi sang Khối " + newGrade, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void saveFontSize(String size) {
        sharedPreferences.edit().putString("FontSize", size).apply();
        // Lưu ý: Để thay đổi font size toàn app cần khởi động lại activity hoặc sử dụng BaseActivity
    }
}
