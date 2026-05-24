package com.example.nhom_2_android_final;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nhom_2_android_final.database.dao.QuestionReview;
import com.example.nhom_2_android_final.database.entity.CauTraLoi;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<QuestionReview> reviewList;

    public ReviewAdapter(List<QuestionReview> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_question, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        QuestionReview item = reviewList.get(position);
        holder.tvQuestionNo.setText("Câu " + (position + 1) + ":");
        holder.tvQuestionContent.setText(item.cauHoi.NoiDung);

        // --- Hiển thị hình ảnh SVG nếu có ---
        if (item.cauHoi.HinhAnh != null && !item.cauHoi.HinhAnh.trim().isEmpty()) {
            holder.flQuestionImage.setVisibility(View.VISIBLE);
            holder.flQuestionImage.removeAllViews();

            WebView webView = new WebView(holder.itemView.getContext());
            webView.setBackgroundColor(0); // Nền trong suốt
            webView.getSettings().setJavaScriptEnabled(false);

            // CSS để căn giữa và co giãn hình ảnh phù hợp
            String htmlData = "<html><head><style>body{margin:0;padding:0;display:flex;justify-content:center;align-items:center;height:100%;}svg{max-width:100%;max-height:100%;}</style></head><body>"
                    + item.cauHoi.HinhAnh + "</body></html>";

            webView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null);

            float density = holder.itemView.getContext().getResources().getDisplayMetrics().density;
            int heightPx = (int) (180 * density); // Chiều cao khung hình 180dp
            holder.flQuestionImage.addView(webView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
        } else {
            holder.flQuestionImage.setVisibility(View.GONE);
        }
        // ------------------------------------
        
        String userAnswerText = "Chưa trả lời";
        String correctAnswerText = "";

        for (CauTraLoi tl : item.dsCauTraLoi) {
            if (tl.DapAnID.equals(item.selectedDapAnID)) {
                userAnswerText = tl.NoiDung;
            }
            if (tl.DapAnDung == 1) {
                correctAnswerText = tl.NoiDung;
            }
        }

        holder.tvUserAnswer.setText("Đáp án của bạn: " + userAnswerText);
        holder.tvCorrectAnswer.setText("Đáp án đúng: " + correctAnswerText);
        holder.tvExplanation.setText("Giải thích: " + (item.cauHoi.GiaiThich != null ? item.cauHoi.GiaiThich : "Không có giải thích."));

        // Highlight correct/incorrect
        if (item.selectedDapAnID != null) {
            boolean isCorrect = false;
            for (CauTraLoi tl : item.dsCauTraLoi) {
                if (tl.DapAnID.equals(item.selectedDapAnID) && tl.DapAnDung == 1) {
                    isCorrect = true;
                    break;
                }
            }
            if (isCorrect) {
                holder.tvUserAnswer.setTextColor(Color.parseColor("#4CAF50")); // Green
            } else {
                holder.tvUserAnswer.setTextColor(Color.parseColor("#F44336")); // Red
            }
        } else {
            holder.tvUserAnswer.setTextColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNo, tvQuestionContent, tvUserAnswer, tvCorrectAnswer, tvExplanation;
        FrameLayout flQuestionImage;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNo = itemView.findViewById(R.id.tvQuestionNo);
            tvQuestionContent = itemView.findViewById(R.id.tvQuestionContent);
            tvUserAnswer = itemView.findViewById(R.id.tvUserAnswer);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            tvExplanation = itemView.findViewById(R.id.tvExplanation);
            flQuestionImage = itemView.findViewById(R.id.flQuestionImage);
        }
    }
}
