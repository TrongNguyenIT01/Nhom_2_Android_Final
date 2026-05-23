package com.example.nhom_2_android_final;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nhom_2_android_final.database.AppDatabase;
import com.example.nhom_2_android_final.database.entity.ChuDe;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class TopicListActivity extends BaseActivity {

    private RecyclerView rvTopics;
    private TopicAdapter adapter;
    private List<ChuDe> topicList = new ArrayList<>();
    private AppDatabase db;
    private int userGrade;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        userGrade = getIntent().getIntExtra("USER_GRADE", 10);
        userId = getIntent().getStringExtra("USER_ID");

        rvTopics = findViewById(R.id.rvTopics);
        rvTopics.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TopicAdapter(topicList, topic -> {
            Intent intent = new Intent(TopicListActivity.this, ExamListActivity.class);
            intent.putExtra("TOPIC_ID", topic.IDChuDe);
            intent.putExtra("TOPIC_NAME", topic.TenChuDe);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USER_GRADE", userGrade);
            startActivity(intent);
        });
        rvTopics.setAdapter(adapter);

        db = AppDatabase.getInstance(this);
        loadTopics();
    }

    private void loadTopics() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ChuDe> list = db.chuDeDao().getByKhoiLop(userGrade);
            runOnUiThread(() -> {
                topicList.clear();
                topicList.addAll(list);
                adapter.notifyDataSetChanged();
            });
        });
    }
}
