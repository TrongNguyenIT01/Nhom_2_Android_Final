package com.example.nhom_2_android_final;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import com.example.nhom_2_android_final.database.AppDatabase;
import com.example.nhom_2_android_final.database.entity.KetQua;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class StatisticsActivity extends BaseActivity {

    private LineChart lineChart;
    private PieChart pieChart;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        userId = getIntent().getStringExtra("USER_ID");

        lineChart = findViewById(R.id.lineChart);
        pieChart = findViewById(R.id.pieChart);
        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        loadStatistics();
    }

    private void loadStatistics() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<KetQua> list = AppDatabase.getInstance(this).ketQuaDao().getHistoryByUserId(userId);
            if (list == null || list.isEmpty()) return;

            // Đảo ngược danh sách để vẽ từ cũ đến mới
            List<KetQua> sortedList = new ArrayList<>(list);
            Collections.reverse(sortedList);
            
            // Giới hạn 10 bài gần nhất cho biểu đồ đường
            int start = Math.max(0, sortedList.size() - 10);
            List<KetQua> recentResults = sortedList.subList(start, sortedList.size());

            runOnUiThread(() -> {
                setupLineChart(recentResults);
                setupPieChart(list);
            });
        });
    }

    private void setupLineChart(List<KetQua> results) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            entries.add(new Entry(i, results.get(i).Diem));
            // Cắt bớt ngày để hiển thị label ngắn gọn (vd: 2023-10-25)
            String date = results.get(i).NgayNop;
            if (date.length() > 10) date = date.substring(5, 10);
            labels.add(date);
        }

        LineDataSet dataSet = new LineDataSet(entries, "Điểm số");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.RED);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawValues(true);
        dataSet.setValueTextSize(10f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.animateX(1000);
        lineChart.invalidate();
    }

    private void setupPieChart(List<KetQua> allResults) {
        int totalCorrect = 0;
        int totalWrong = 0;

        for (KetQua kq : allResults) {
            totalCorrect += kq.SoCauDung;
            totalWrong += kq.SoCauSai;
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(totalCorrect, "Câu Đúng"));
        entries.add(new PieEntry(totalWrong, "Câu Sai"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{Color.GREEN, Color.RED});
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Tỷ lệ Đúng/Sai");
        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}
