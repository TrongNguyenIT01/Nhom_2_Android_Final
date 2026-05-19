package com.example.nhom_2_android_final.Model;

import android.content.Context;
import com.example.nhom_2_android_final.database.AppDatabase;
import com.example.nhom_2_android_final.database.entity.User;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class ForgotPasswordModel {
    private Context context;
    private String generatedOtp;
    private long otpExpiryTime; // Thời điểm hết hạn mã OTP (ms)
    private static final long OTP_VALID_DURATION = 10 * 60 * 1000; // 10 phút

    public ForgotPasswordModel(Context context) {
        this.context = context;
    }

    public interface ModelCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    public void sendOtp(String email, ModelCallback callback) {
        new Thread(() -> {
            User user = AppDatabase.getInstance(context).userDao().findUserByEmail(email);
            if (user == null) {
                callback.onError("Email không tồn tại trong hệ thống!");
                return;
            }

            generatedOtp = String.valueOf(new Random().nextInt(899999) + 100000);
            otpExpiryTime = System.currentTimeMillis() + OTP_VALID_DURATION;
            
            if (sendEmail(email, generatedOtp)) {
                callback.onSuccess("Mã OTP đã được gửi đến email của bạn.");
            } else {
                callback.onError("Lỗi khi gửi email. Hãy kiểm tra kết nối mạng!");
            }
        }).start();
    }

    private boolean sendEmail(String toEmail, String otp) {
        final String from = "hoangphu24122005@gmail.com"; 
        final String password = "ebfq hwtx gkxo hath"; 

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Ma xac thuc doi mat khau");
            message.setText("Ma OTP cua ban la: " + otp + "\nMa co hieu luc trong 10 phut.");
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int verifyOtp(String inputOtp) {
        if (generatedOtp == null || !generatedOtp.equals(inputOtp)) {
            return -1; // Sai mã
        }
        if (System.currentTimeMillis() > otpExpiryTime) {
            return 0; // Hết hạn
        }
        return 1; // Thành công
    }

    public void updatePassword(String email, String newPass, ModelCallback callback) {
        new Thread(() -> {
            User user = AppDatabase.getInstance(context).userDao().findUserByEmail(email);
            if (user != null) {
                user.Password = newPass;
                AppDatabase.getInstance(context).userDao().update(user);
                callback.onSuccess("Đổi mật khẩu thành công!");
            } else {
                callback.onError("Đã có lỗi xảy ra!");
            }
        }).start();
    }
}
