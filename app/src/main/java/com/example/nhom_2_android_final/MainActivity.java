package com.example.nhom_2_android_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hiển thị tên người dùng mẫu
        TextView tvUserName = findViewById(R.id.tvUserName);
        // Tên sẽ được lấy trực tiếp từ file XML hoặc bạn có thể gán lại ở đây nếu cần
        // tvUserName.setText(getString(R.string.user_name_default));

        // Thiết lập các sự kiện click cho Dashboard
        MaterialCardView cardExam = findViewById(R.id.cardExam);
        cardExam.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng Chọn đề và thi đang phát triển", Toast.LENGTH_SHORT).show();
        });

        MaterialCardView cardHistory = findViewById(R.id.cardHistory);
        cardHistory.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng Lịch sử làm bài đang phát triển", Toast.LENGTH_SHORT).show();
        });

        MaterialCardView cardStats = findViewById(R.id.cardStats);
        cardStats.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng Thống kê kết quả đang phát triển", Toast.LENGTH_SHORT).show();
        });

        MaterialCardView cardSettings = findViewById(R.id.cardSettings);
        cardSettings.setOnClickListener(v -> {
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
}