package com.example.nhom_2_android_final;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nhom_2_android_final.database.AppDatabase;
import com.example.nhom_2_android_final.database.dao.QuestionReview;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ReviewActivity extends BaseActivity {

    private RecyclerView rvReview;
    private ReviewAdapter adapter;
    private List<QuestionReview> reviewList = new ArrayList<>();
    private String idKetQua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        idKetQua = getIntent().getStringExtra("ID_KET_QUA");

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        rvReview = findViewById(R.id.rvReview);
        rvReview.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new ReviewAdapter(reviewList);
        rvReview.setAdapter(adapter);

        loadReviewData();
    }

    private void loadReviewData() {
        if (idKetQua == null) return;

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            List<QuestionReview> data = db.ketQuaChiTietDao().getReviewByKetQuaId(idKetQua);
            
            runOnUiThread(() -> {
                if (data != null) {
                    reviewList.clear();
                    reviewList.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }
}
