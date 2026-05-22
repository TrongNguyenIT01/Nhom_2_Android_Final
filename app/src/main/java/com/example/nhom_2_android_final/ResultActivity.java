package com.example.nhom_2_android_final;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        float score = getIntent().getFloatExtra("SCORE", 0);
        int correctCount = getIntent().getIntExtra("CORRECT_COUNT", 0);
        int totalCount = getIntent().getIntExtra("TOTAL_COUNT", 0);
        int timeSpent = getIntent().getIntExtra("TIME_SPENT", 0);

        TextView tvScore = findViewById(R.id.tvScore);
        TextView tvCorrectCount = findViewById(R.id.tvCorrectCount);
        TextView tvTimeResult = findViewById(R.id.tvTimeResult);
        MaterialButton btnBackHome = findViewById(R.id.btnBackHome);

        tvScore.setText(String.format(Locale.getDefault(), "%.1f", score));
        tvCorrectCount.setText(String.format(Locale.getDefault(), "Số câu đúng: %d/%d", correctCount, totalCount));

        int minutes = timeSpent / 60;
        int seconds = timeSpent % 60;
        tvTimeResult.setText(String.format(Locale.getDefault(), "Thời gian làm bài: %02d:%02d", minutes, seconds));

        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
