package com.example.nhom_2_android_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nhom_2_android_final.database.AppDatabase;
import com.example.nhom_2_android_final.database.entity.User;
import com.google.android.material.card.MaterialCardView;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity {

    private String userId;
    private int userGrade;
    private String userName;
    private TextView tvUserGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextView
        TextView tvUserName = findViewById(R.id.tvUserName);
        tvUserGrade = findViewById(R.id.tvUserGrade);

        // Nhận dữ liệu ban đầu
        userName = getIntent().getStringExtra("USER_NAME");
        userGrade = getIntent().getIntExtra("USER_GRADE", 0);
        userId = getIntent().getStringExtra("USER_ID");

        // Hiển thị thông tin ban đầu
        tvUserName.setText(userName != null ? userName : "User");
        tvUserGrade.setText("Khối: " + userGrade);

        // Thiết lập các sự kiện click cho Dashboard
        findViewById(R.id.cardExam).setOnClickListener(v -> {
            // Chuyển đến màn hình Chọn Chủ Đề thay vì chọn Bài kiểm tra trực tiếp
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
            Toast.makeText(this, "Chức năng Thống kê kết quả đang phát triển", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        // Tự động cập nhật lại khối lớp mới nhất từ Database khi quay lại màn hình này
        if (userId != null) {
            Executors.newSingleThreadExecutor().execute(() -> {
                User user = AppDatabase.getInstance(this).userDao().getUserById(userId);
                if (user != null) {
                    userGrade = user.KhoiLop;
                    runOnUiThread(() -> tvUserGrade.setText("Khối: " + userGrade));
                }
            });
        }
    }
}
