package com.example.nhom_2_android_final;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.switchmaterial.SwitchMaterial;

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
    }

    private void saveFontSize(String size) {
        sharedPreferences.edit().putString("FontSize", size).apply();
        // Lưu ý: Để thay đổi font size toàn app cần khởi động lại activity hoặc sử dụng BaseActivity
    }
}