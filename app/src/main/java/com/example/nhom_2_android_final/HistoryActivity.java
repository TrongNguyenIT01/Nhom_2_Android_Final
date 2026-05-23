package com.example.nhom_2_android_final;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nhom_2_android_final.database.AppDatabase;
import com.example.nhom_2_android_final.database.dao.HistoryResult;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class HistoryActivity extends BaseActivity {

    private RecyclerView rvHistory;
    private HistoryAdapter adapter;
    private List<HistoryResult> historyList = new ArrayList<>();
    private TextView tvEmpty;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        userId = getIntent().getStringExtra("USER_ID");

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        tvEmpty = findViewById(R.id.tvEmpty);
        rvHistory = findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new HistoryAdapter(historyList);
        rvHistory.setAdapter(adapter);

        loadHistory();
    }

    private void loadHistory() {
        if (userId == null || userId.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            List<HistoryResult> list = db.ketQuaDao().getFullHistoryByUserId(userId);
            
            runOnUiThread(() -> {
                if (list == null || list.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    historyList.clear();
                    historyList.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }
}
