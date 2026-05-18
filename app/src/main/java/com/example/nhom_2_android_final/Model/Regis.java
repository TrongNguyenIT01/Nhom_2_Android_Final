package com.example.nhom_2_android_final.Model;

import android.content.Context;
import android.text.TextUtils;

import com.example.nhom_2_android_final.database.entity.User;
import com.example.nhom_2_android_final.repository.UserRepository;

public class Regis {
    private final UserRepository userRepository;

    public Regis(Context context) {
        this.userRepository = new UserRepository(context);
    }

    public interface RegistrationCallback {
        void onSuccess(String userId);
        void onError(String message);
    }

    /**
     * Logic xử lý chính cho việc đăng ký tài khoản (Model)
     */
    public void executeRegistration(String fullName, String email, String password, String confirmPassword, String gradeStr, RegistrationCallback callback) {
        // 1. Logic Validate
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            callback.onError("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            callback.onError("Mật khẩu xác nhận không khớp!");
            return;
        }

        // 2. Kiểm tra trùng lặp và tạo ID thông qua Repository
        userRepository.isUsernameTaken(email, isTaken -> {
            if (isTaken) {
                callback.onError("Email/Username đã tồn tại!");
            } else {
                userRepository.generateNextId(nextId -> {
                    User newUser = new User();
                    newUser.UserID = nextId;
                    newUser.Username = email;
                    newUser.Password = password;
                    newUser.HoTen = fullName;
                    newUser.Email = email;
                    newUser.KhoiLop = extractGrade(gradeStr);

                    // 3. Thực hiện lưu vào Database
                    userRepository.registerUser(newUser, success -> {
                        if (success) {
                            callback.onSuccess(nextId);
                        } else {
                            callback.onError("Lỗi lưu dữ liệu, vui lòng thử lại!");
                        }
                    });
                });
            }
        });
    }

    private int extractGrade(String gradeStr) {
        if (gradeStr != null) {
            if (gradeStr.contains("10")) return 10;
            if (gradeStr.contains("11")) return 11;
            if (gradeStr.contains("12")) return 12;
        }
        return 10;
    }
}
