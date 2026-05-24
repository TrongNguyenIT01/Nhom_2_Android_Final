package com.example.nhom_2_android_final;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nhom_2_android_final.database.dao.HistoryResult;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryResult> historyList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(HistoryResult item);
    }

    public HistoryAdapter(List<HistoryResult> historyList, OnItemClickListener listener) {
        this.historyList = historyList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryResult item = historyList.get(position);
        
        if (item.baiKiemTra != null) {
            holder.tvExamTitle.setText(item.baiKiemTra.TenBaiKT);
        } else {
            holder.tvExamTitle.setText("Bài thi đã bị xóa");
        }

        holder.tvScore.setText(String.format(Locale.getDefault(), "%.1f", item.ketQua.Diem));
        holder.tvCorrectCount.setText(String.format(Locale.getDefault(), "%d câu", item.ketQua.SoCauDung));
        
        // Format thời gian làm bài (giây -> phút:giây)
        int totalSeconds = item.ketQua.ThoiGianKT;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        holder.tvTime.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
        
        // Ngày nộp (giả sử định dạng yyyy-MM-dd HH:mm:ss, lấy phần ngày)
        String fullDate = item.ketQua.NgayNop;
        if (fullDate != null && fullDate.contains(" ")) {
            holder.tvDate.setText(fullDate.split(" ")[0]);
        } else {
            holder.tvDate.setText(fullDate);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList != null ? historyList.size() : 0;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvExamTitle, tvScore, tvCorrectCount, tvTime, tvDate;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExamTitle = itemView.findViewById(R.id.tvExamTitle);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvCorrectCount = itemView.findViewById(R.id.tvCorrectCount);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
