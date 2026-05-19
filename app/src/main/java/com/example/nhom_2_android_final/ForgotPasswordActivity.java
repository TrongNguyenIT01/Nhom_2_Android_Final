package com.example.nhom_2_android_final;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nhom_2_android_final.Model.ForgotPasswordModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Locale;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etOtp, etNewPassword, etConfirmPassword;
    private TextInputLayout tilOtp;
    private TextView tvCountdown;
    private ImageButton btnBack;
    private LinearLayout layoutStep1, layoutStep2;
    private MaterialButton btnAction;
    private ForgotPasswordModel model;
    private CountDownTimer countDownTimer;
    
    private int step = 1; // 1: Email/OTP, 2: Reset Password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDelegate().setLocalNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_forgot_password);

        model = new ForgotPasswordModel(this);
        initViews();

        btnAction.setOnClickListener(v -> {
            if (step == 1) {
                if (tilOtp.getVisibility() == View.GONE) {
                    handleSendOtp();
                } else {
                    handleVerifyOtp();
                }
            } else if (step == 2) {
                handleUpdatePassword();
            }
        });

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etOtp = findViewById(R.id.etOtp);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        
        tilOtp = findViewById(R.id.tilOtp);
        tvCountdown = findViewById(R.id.tvCountdown);
        btnBack = findViewById(R.id.btnBack);
        layoutStep1 = findViewById(R.id.layoutStep1);
        layoutStep2 = findViewById(R.id.layoutStep2);
        btnAction = findViewById(R.id.btnAction);
    }

    private void handleSendOtp() {
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        btnAction.setEnabled(false);
        btnAction.setText("Đang gửi...");

        model.sendOtp(email, new ForgotPasswordModel.ModelCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                    tilOtp.setVisibility(View.VISIBLE);
                    etEmail.setEnabled(false);
                    btnAction.setEnabled(true);
                    btnAction.setText("Xác nhận OTP");
                    startCountdown();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                    btnAction.setEnabled(true);
                    btnAction.setText("Gửi mã OTP");
                });
            }
        });
    }

    private void startCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        tvCountdown.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(10 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeLeft = String.format(Locale.getDefault(), "Mã hết hạn sau: %02d:%02d", minutes, seconds);
                tvCountdown.setText(timeLeft);
            }

            @Override
            public void onFinish() {
                tvCountdown.setText("Mã OTP đã hết hạn!");
                btnAction.setText("Gửi lại mã OTP");
                tilOtp.setVisibility(View.GONE);
                etEmail.setEnabled(true);
            }
        }.start();
    }

    private void handleVerifyOtp() {
        String otp = etOtp.getText().toString().trim();
        if (otp.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        int result = model.verifyOtp(otp);
        if (result == 1) {
            if (countDownTimer != null) countDownTimer.cancel();
            tvCountdown.setVisibility(View.GONE);
            
            // Chuyển sang bước 2
            layoutStep1.setVisibility(View.GONE);
            layoutStep2.setVisibility(View.VISIBLE);
            btnAction.setText("Đổi mật khẩu");
            step = 2;
        } else if (result == 0) {
            Toast.makeText(this, "Mã OTP đã hết hạn!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Mã OTP không chính xác!", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleUpdatePassword() {
        String email = etEmail.getText().toString().trim();
        String newPass = etNewPassword.getText().toString().trim();
        String confirmPass = etConfirmPassword.getText().toString().trim();

        if (newPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPass.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải từ 6 ký tự trở lên", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnAction.setEnabled(false);
        model.updatePassword(email, newPass, new ForgotPasswordModel.ModelCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(ForgotPasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại màn hình Login
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                    btnAction.setEnabled(true);
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
