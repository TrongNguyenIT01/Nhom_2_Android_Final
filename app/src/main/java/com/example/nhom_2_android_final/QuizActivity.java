package com.example.nhom_2_android_final;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nhom_2_android_final.database.AppDatabase;
import com.example.nhom_2_android_final.database.entity.CauHoi;
import com.example.nhom_2_android_final.database.entity.CauTraLoi;
import com.example.nhom_2_android_final.database.entity.KetQua;
import com.example.nhom_2_android_final.database.entity.KetQuaChiTiet;
import com.example.nhom_2_android_final.utils.NotificationHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;

public class QuizActivity extends BaseActivity {

    private TextView tvQuestionCount, tvTimer, tvQuestionContent;
    private ProgressBar progressBar;
    private RadioGroup rgAnswers;
    private RadioButton rbA, rbB, rbC, rbD;
    private Button btnPrev, btnNext;
    private FrameLayout flQuestionImage; // Thêm biến này

    private AppDatabase db;
    private String examId, userId;
    private int totalTimeInSeconds;
    private int timeRemainingInSeconds;
    private List<CauHoi> questionList = new ArrayList<>();
    private final Map<String, List<CauTraLoi>> answerMap = new HashMap<>();
    private final Map<String, String> userAnswers = new HashMap<>(); // QuestionID -> AnswerID

    private int currentIndex = 0;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();

        examId = getIntent().getStringExtra("EXAM_ID");
        userId = getIntent().getStringExtra("USER_ID");
        totalTimeInSeconds = getIntent().getIntExtra("EXAM_TIME", 0);
        String examName = getIntent().getStringExtra("EXAM_NAME");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(examName);
        }

        db = AppDatabase.getInstance(this);
        loadData();

        btnNext.setOnClickListener(v -> {
            saveCurrentAnswer();
            if (currentIndex < questionList.size() - 1) {
                currentIndex++;
                displayQuestion();
            } else {
                showSubmitConfirmDialog();
            }
        });

        btnPrev.setOnClickListener(v -> {
            saveCurrentAnswer();
            if (currentIndex > 0) {
                currentIndex--;
                displayQuestion();
            }
        });
    }

    private void initViews() {
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestionContent = findViewById(R.id.tvQuestionContent);
        progressBar = findViewById(R.id.progressBar);
        rgAnswers = findViewById(R.id.rgAnswers);
        rbA = findViewById(R.id.rbOptionA);
        rbB = findViewById(R.id.rbOptionB);
        rbC = findViewById(R.id.rbOptionC);
        rbD = findViewById(R.id.rbOptionD);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        flQuestionImage = findViewById(R.id.flQuestionImage); // Khởi tạo FrameLayout
    }

    private void loadData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            questionList = db.cauHoiDao().getByBaiKiemTra(examId);
            for (CauHoi ch : questionList) {
                List<CauTraLoi> ctl = db.cauTraLoiDao().getByCauHoi(ch.IDCauHoi);
                // Xáo trộn vị trí các câu trả lời
                Collections.shuffle(ctl);
                answerMap.put(ch.IDCauHoi, ctl);
            }
            runOnUiThread(() -> {
                if (!questionList.isEmpty()) {
                    progressBar.setMax(questionList.size());
                    displayQuestion();
                    startTimer();
                } else {
                    Toast.makeText(this, "Không có câu hỏi cho đề này", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }

    private void displayQuestion() {
        CauHoi currentQuestion = questionList.get(currentIndex);
        tvQuestionCount.setText(String.format(Locale.getDefault(), "Câu hỏi: %d/%d", (currentIndex + 1), questionList.size()));
        tvQuestionContent.setText(currentQuestion.NoiDung);
        progressBar.setProgress(currentIndex + 1);

        // --- Hiển thị hình ảnh SVG nếu có ---
        if (currentQuestion.HinhAnh != null && !currentQuestion.HinhAnh.trim().isEmpty()) {
            flQuestionImage.setVisibility(View.VISIBLE);
            flQuestionImage.removeAllViews();

            WebView webView = new WebView(this);
            webView.setBackgroundColor(0); // Nền trong suốt
            webView.getSettings().setJavaScriptEnabled(false);

            // CSS để căn giữa và co giãn hình ảnh phù hợp màn hình
            String htmlData = "<html><head><style>body{margin:0;padding:0;display:flex;justify-content:center;align-items:center;height:100%;}svg{max-width:100%;max-height:100%;}</style></head><body>"
                    + currentQuestion.HinhAnh + "</body></html>";

            webView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null);

            float density = getResources().getDisplayMetrics().density;
            int heightPx = (int) (180 * density); // Chiều cao khung hình 180dp
            flQuestionImage.addView(webView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
        } else {
            flQuestionImage.setVisibility(View.GONE);
        }
        // ------------------------------------

        List<CauTraLoi> answers = answerMap.get(currentQuestion.IDCauHoi);
        rgAnswers.clearCheck();

        if (answers != null && answers.size() >= 4) {
            // Sử dụng nhãn A, B, C, D cố định theo thứ tự danh sách đã xáo trộn
            rbA.setText(String.format("A. %s", answers.get(0).NoiDung));
            rbA.setTag(answers.get(0).DapAnID);

            rbB.setText(String.format("B. %s", answers.get(1).NoiDung));
            rbB.setTag(answers.get(1).DapAnID);

            rbC.setText(String.format("C. %s", answers.get(2).NoiDung));
            rbC.setTag(answers.get(2).DapAnID);

            rbD.setText(String.format("D. %s", answers.get(3).NoiDung));
            rbD.setTag(answers.get(3).DapAnID);
        }

        // Khôi phục đáp án đã chọn
        String savedAnswerId = userAnswers.get(currentQuestion.IDCauHoi);
        if (savedAnswerId != null) {
            if (savedAnswerId.equals(rbA.getTag())) rbA.setChecked(true);
            else if (savedAnswerId.equals(rbB.getTag())) rbB.setChecked(true);
            else if (savedAnswerId.equals(rbC.getTag())) rbC.setChecked(true);
            else if (savedAnswerId.equals(rbD.getTag())) rbD.setChecked(true);
        }

        btnPrev.setEnabled(currentIndex > 0);
        btnNext.setText(currentIndex == questionList.size() - 1 ? "Nộp bài" : "Tiếp theo");
    }

    private void saveCurrentAnswer() {
        int checkedId = rgAnswers.getCheckedRadioButtonId();
        if (checkedId != -1) {
            RadioButton selected = findViewById(checkedId);
            if (selected != null && selected.getTag() != null) {
                userAnswers.put(questionList.get(currentIndex).IDCauHoi, (String) selected.getTag());
            }
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer((long) totalTimeInSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingInSeconds = (int) (millisUntilFinished / 1000);
                int minutes = timeRemainingInSeconds / 60;
                int secs = timeRemainingInSeconds % 60;
                tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, secs));
            }

            @Override
            public void onFinish() {
                timeRemainingInSeconds = 0;
                tvTimer.setText("00:00");
                Toast.makeText(QuizActivity.this, "Hết giờ! Đang tự động nộp bài.", Toast.LENGTH_LONG).show();
                submitQuiz();
            }
        }.start();
    }

    private void showSubmitConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Nộp bài")
                .setMessage("Bạn có chắc chắn muốn nộp bài không?")
                .setPositiveButton("Nộp", (dialog, which) -> submitQuiz())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void submitQuiz() {
        if (countDownTimer != null) countDownTimer.cancel();
        saveCurrentAnswer();

        Executors.newSingleThreadExecutor().execute(() -> {
            int correctCount = 0;
            String resultId = UUID.randomUUID().toString();
            List<KetQuaChiTiet> details = new ArrayList<>();

            for (CauHoi q : questionList) {
                String selectedId = userAnswers.get(q.IDCauHoi);
                List<CauTraLoi> answers = answerMap.get(q.IDCauHoi);

                String correctId = null;
                if (answers != null) {
                    for (CauTraLoi a : answers) {
                        if (a.DapAnDung == 1) {
                            correctId = a.DapAnID;
                            break;
                        }
                    }
                }

                if (selectedId != null && selectedId.equals(correctId)) {
                    correctCount++;
                }

                KetQuaChiTiet detail = new KetQuaChiTiet();
                detail.IDKQCT = UUID.randomUUID().toString();
                detail.IDKetQua = resultId;
                detail.IDCauHoi = q.IDCauHoi;
                detail.DapAnID = selectedId != null ? selectedId : "";
                details.add(detail);
            }

            float score = (float) correctCount * 10 / questionList.size();
            int timeSpent = totalTimeInSeconds - timeRemainingInSeconds;

            KetQua result = new KetQua();
            result.IDKetQua = resultId;
            result.UserID = userId;
            result.IDBaiKT = examId;
            result.Diem = score;
            result.SoCauDung = correctCount;
            result.SoCauSai = questionList.size() - correctCount;
            result.ThoiGianKT = timeSpent;
            result.NgayNop = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            db.ketQuaDao().insert(result);
            db.ketQuaChiTietDao().insertAll(details);

            final int finalCorrectCount = correctCount;
            final float finalScore = score;
            final int finalTimeSpent = timeSpent;
            final int totalCount = questionList.size();

            // Hiển thị thông báo kết quả ra thanh thông báo hệ thống
            String message = String.format(Locale.getDefault(), "Bạn đạt %.1f điểm (%d/%d câu đúng).", finalScore, finalCorrectCount, totalCount);
            NotificationHelper.showNotification(this, "Kết quả bài kiểm tra", message);

            runOnUiThread(() -> {
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                intent.putExtra("SCORE", finalScore);
                intent.putExtra("CORRECT_COUNT", finalCorrectCount);
                intent.putExtra("TOTAL_COUNT", totalCount);
                intent.putExtra("TIME_SPENT", finalTimeSpent);
                startActivity(intent);
                finish();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
