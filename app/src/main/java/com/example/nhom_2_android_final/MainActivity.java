package com.example.nhom_2_android_final;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.nhom_2_android_final.database.AppDatabase;
import com.example.nhom_2_android_final.database.entity.User;
import com.example.nhom_2_android_final.utils.AlarmScheduler;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity {

    private String userId;
    private int userGrade;
    private String userName;
    private TextView tvUserGrade;
    private TextView tvUserName;
    private ImageView ivUserAvatar;
    private static final int NOTIFICATION_PERMISSION_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextView & ImageView
        tvUserName = findViewById(R.id.tvUserName);
        tvUserGrade = findViewById(R.id.tvUserGrade);
        ivUserAvatar = findViewById(R.id.ivUserAvatar);

        // Nhận dữ liệu ban đầu
        userName = getIntent().getStringExtra("USER_NAME");
        userGrade = getIntent().getIntExtra("USER_GRADE", 0);
        userId = getIntent().getStringExtra("USER_ID");

        // Hiển thị thông tin ban đầu
        tvUserName.setText(userName != null ? userName : "User");
        tvUserGrade.setText("Khối: " + userGrade);

        // Thiết lập thông báo và nhắc nhở
        checkNotificationPermission();
        AlarmScheduler.scheduleStudyReminder(this);
        AlarmScheduler.scheduleDailyTip(this);

        // Nút thông báo
        findViewById(R.id.btnNotifications).setOnClickListener(v -> {
            Toast.makeText(this, "Bạn sẽ nhận được lời khuyên mỗi 4 giờ!", Toast.LENGTH_LONG).show();
        });

        // Thiết lập các sự kiện click cho Dashboard
        findViewById(R.id.cardExam).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TopicListActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USER_GRADE", userGrade);
            startActivity(intent);
        });

        findViewById(R.id.cardHistory).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        findViewById(R.id.cardStats).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        findViewById(R.id.cardSettings).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Nút Đăng xuất
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Đã bật thông báo nhắc nhở!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tự động cập nhật lại thông tin mới nhất từ Database khi quay lại màn hình này
        if (userId != null) {
            Executors.newSingleThreadExecutor().execute(() -> {
                User user = AppDatabase.getInstance(this).userDao().getUserById(userId);
                if (user != null) {
                    userGrade = user.KhoiLop;
                    userName = user.HoTen != null ? user.HoTen : user.Username;
                    String avatarPath = user.AVT;
                    
                    runOnUiThread(() -> {
                        tvUserName.setText(userName);
                        tvUserGrade.setText("Khối: " + userGrade);
                        if (avatarPath != null && !avatarPath.isEmpty()) {
                            File file = new File(avatarPath);
                            Glide.with(this)
                                    .load(avatarPath)
                                    .signature(new ObjectKey(file.exists() ? file.lastModified() : System.currentTimeMillis()))
                                    .placeholder(android.R.drawable.ic_menu_gallery)
                                    .circleCrop()
                                    .into(ivUserAvatar);
                        }
                    });
                }
            });
        }
    }
}
