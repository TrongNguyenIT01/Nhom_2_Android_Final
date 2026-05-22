package com.example.nhom_2_android_final;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nhom_2_android_final.database.entity.ChuDe;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<ChuDe> topicList;
    private OnTopicClickListener listener;

    public interface OnTopicClickListener {
        void onTopicClick(ChuDe topic);
    }

    public TopicAdapter(List<ChuDe> topicList, OnTopicClickListener listener) {
        this.topicList = topicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        ChuDe topic = topicList.get(position);
        holder.textView.setText(topic.TenChuDe);
        holder.itemView.setOnClickListener(v -> listener.onTopicClick(topic));
    }

    @Override
    public int getItemCount() {
        return topicList != null ? topicList.size() : 0;
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
