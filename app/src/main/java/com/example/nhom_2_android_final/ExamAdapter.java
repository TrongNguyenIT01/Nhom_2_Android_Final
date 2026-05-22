package com.example.nhom_2_android_final;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nhom_2_android_final.database.entity.BaiKiemTra;
import java.util.ArrayList;
import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> implements Filterable {

    private List<BaiKiemTra> examList;
    private List<BaiKiemTra> examListFull; // Bản sao đầy đủ để tìm kiếm
    private OnExamClickListener listener;

    public interface OnExamClickListener {
        void onExamClick(BaiKiemTra exam);
    }

    public ExamAdapter(List<BaiKiemTra> examList, OnExamClickListener listener) {
        this.examList = examList;
        this.examListFull = new ArrayList<>(examList);
        this.listener = listener;
    }

    // Cập nhật danh sách khi load từ DB
    public void updateList(List<BaiKiemTra> newList) {
        this.examList = newList;
        this.examListFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        BaiKiemTra exam = examList.get(position);
        holder.tvExamTitle.setText(exam.TenBaiKT);
        holder.tvExamTime.setText("Thời gian: " + (exam.ThoiGian / 60) + " phút");
        holder.tvExamGrade.setText("Khối: " + exam.KhoiLop);
        holder.itemView.setOnClickListener(v -> listener.onExamClick(exam));
    }

    @Override
    public int getItemCount() {
        return examList != null ? examList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return examFilter;
    }

    private Filter examFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<BaiKiemTra> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(examListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (BaiKiemTra item : examListFull) {
                    if (item.TenBaiKT.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            examList = (List) results.values;
            notifyDataSetChanged();
        }
    };

    public static class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView tvExamTitle, tvExamTime, tvExamGrade;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExamTitle = itemView.findViewById(R.id.tvExamTitle);
            tvExamTime = itemView.findViewById(R.id.tvExamTime);
            tvExamGrade = itemView.findViewById(R.id.tvExamGrade);
        }
    }
}
