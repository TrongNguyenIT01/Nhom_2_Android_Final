package com.example.nhom_2_android_final;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nhom_2_android_final.database.AppDatabase;
import com.example.nhom_2_android_final.database.entity.BaiKiemTra;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ExamListActivity extends BaseActivity {

    private RecyclerView rvExams;
    private ExamAdapter adapter;
    private List<BaiKiemTra> examList = new ArrayList<>();
    private AppDatabase db;
    private String topicId;
    private String userId;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_list);

        // Nhận thông tin từ TopicListActivity
        topicId = getIntent().getStringExtra("TOPIC_ID");
        userId = getIntent().getStringExtra("USER_ID");
        String topicName = getIntent().getStringExtra("TOPIC_NAME");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (topicName != null) {
                getSupportActionBar().setTitle(topicName);
            }
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        initViews();
        setupSearch();

        db = AppDatabase.getInstance(this);
        loadExams();
    }

    private void initViews() {
        searchView = findViewById(R.id.searchView);
        rvExams = findViewById(R.id.rvExams);
        rvExams.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ExamAdapter(examList, exam -> {
            Intent intent = new Intent(ExamListActivity.this, QuizActivity.class);
            intent.putExtra("EXAM_ID", exam.IDBaiKT);
            intent.putExtra("EXAM_NAME", exam.TenBaiKT);
            intent.putExtra("EXAM_TIME", exam.ThoiGian);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
        rvExams.setAdapter(adapter);
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void loadExams() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Lấy danh sách bài kiểm tra theo chủ đề đã chọn
            List<BaiKiemTra> list = db.baiKiemTraDao().getByChuDe(topicId);
            runOnUiThread(() -> {
                adapter.updateList(list);
            });
        });
    }
}
