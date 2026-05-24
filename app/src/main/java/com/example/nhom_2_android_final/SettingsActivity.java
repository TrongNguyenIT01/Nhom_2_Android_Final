package com.example.nhom_2_android_final;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.example.nhom_2_android_final.database.AppDatabase;
import com.example.nhom_2_android_final.database.entity.User;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.button.MaterialButtonToggleGroup;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.Executors;

public class SettingsActivity extends BaseActivity {

    private SharedPreferences sharedPreferences;
    private ImageView ivAvatar;
    private AppDatabase db;
    private String userId;

    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    saveAvatarLocally(uri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("CurrentUserID", "");
        db = AppDatabase.getInstance(this);

        ivAvatar = findViewById(R.id.ivAvatar);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnChangeAvatar).setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        loadUserData();

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
        MaterialButtonToggleGroup toggleGroupFontSize = findViewById(R.id.toggleGroupFontSize);
        String currentFontSize = sharedPreferences.getString("FontSize", "medium");
        if (currentFontSize.equals("small")) {
            toggleGroupFontSize.check(R.id.btnSmall);
        } else if (currentFontSize.equals("large")) {
            toggleGroupFontSize.check(R.id.btnLarge);
        } else {
            toggleGroupFontSize.check(R.id.btnMedium);
        }

        toggleGroupFontSize.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnSmall) {
                    saveFontSize("small");
                } else if (checkedId == R.id.btnMedium) {
                    saveFontSize("medium");
                } else if (checkedId == R.id.btnLarge) {
                    saveFontSize("large");
                }
            }
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

    private void loadUserData() {
        if (userId.isEmpty()) return;
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = db.userDao().getUserById(userId);
            if (user != null && user.AVT != null && !user.AVT.isEmpty()) {
                File file = new File(user.AVT);
                runOnUiThread(() -> {
                    Glide.with(this)
                            .load(user.AVT)
                            .signature(new ObjectKey(file.exists() ? file.lastModified() : System.currentTimeMillis()))
                            .centerCrop()
                            .placeholder(android.R.drawable.sym_def_app_icon)
                            .into(ivAvatar);
                });
            }
        });
    }

    private void saveAvatarLocally(Uri uri) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                File file = new File(getFilesDir(), "avatar_" + userId + ".jpg");
                FileOutputStream outputStream = new FileOutputStream(file);
                
                byte[] buffer = new byte[1024];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();

                String imagePath = file.getAbsolutePath();
                long lastModified = file.lastModified();
                
                // Update DB
                User user = db.userDao().getUserById(userId);
                if (user != null) {
                    user.AVT = imagePath;
                    db.userDao().update(user);
                    
                    runOnUiThread(() -> {
                        Glide.with(this)
                                .load(imagePath)
                                .signature(new ObjectKey(lastModified))
                                .centerCrop()
                                .into(ivAvatar);
                        Toast.makeText(this, "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void updateUserGrade(int newGrade) {
        if (userId.isEmpty()) return;

        Executors.newSingleThreadExecutor().execute(() -> {
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
        String currentSize = sharedPreferences.getString("FontSize", "medium");
        if (!currentSize.equals(size)) {
            sharedPreferences.edit().putString("FontSize", size).apply();
            Toast.makeText(this, "Đã cập nhật kích thước chữ", Toast.LENGTH_SHORT).show();
            recreate();
        }
    }
}
