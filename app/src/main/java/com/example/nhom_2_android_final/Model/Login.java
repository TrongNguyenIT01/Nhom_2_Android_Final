package com.example.nhom_2_android_final.Model;

import android.content.Context;
import android.text.TextUtils;

import com.example.nhom_2_android_final.database.entity.User;
import com.example.nhom_2_android_final.repository.UserRepository;

public class Login {
    private final UserRepository userRepository;

    public Login(Context context) {
        this.userRepository = new UserRepository(context);
    }

    public interface LoginCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    /**
     * Logic xử lý đăng nhập (Model)
     * @param identifier Email hoặc UserID
     * @param password Mật khẩu
     * @param callback Callback trả về kết quả
     */
    public void executeLogin(String identifier, String password, LoginCallback callback) {
        // 1. Kiểm tra đầu vào
        if (TextUtils.isEmpty(identifier) || TextUtils.isEmpty(password)) {
            callback.onError("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // 2. Gọi Repository để thực hiện truy vấn đăng nhập
        userRepository.login(identifier, password, user -> {
            if (user != null) {
                callback.onSuccess(user);
            } else {
                callback.onError("Sai tài khoản hoặc mật khẩu!");
            }
        });
    }
}
