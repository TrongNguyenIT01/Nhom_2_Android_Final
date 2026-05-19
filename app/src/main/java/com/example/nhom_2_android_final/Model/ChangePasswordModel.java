package com.example.nhom_2_android_final.Model;

import android.content.Context;
import com.example.nhom_2_android_final.database.AppDatabase;
import com.example.nhom_2_android_final.database.entity.User;

public class ChangePasswordModel {
    private Context context;

    public ChangePasswordModel(Context context) {
        this.context = context;
    }

    public interface ChangePasswordCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    public void changePassword(String userId, String oldPass, String newPass, ChangePasswordCallback callback) {
        new Thread(() -> {
            // Kiểm tra mật khẩu cũ
            User user = AppDatabase.getInstance(context).userDao().login(userId, oldPass);
            if (user == null) {
                // Trường hợp userId lưu trong prefs là email
                User userByEmail = AppDatabase.getInstance(context).userDao().findUserByEmail(userId);
                if (userByEmail != null && userByEmail.Password.equals(oldPass)) {
                    user = userByEmail;
                }
            }

            if (user != null) {
                user.Password = newPass;
                AppDatabase.getInstance(context).userDao().update(user);
                callback.onSuccess("Đổi mật khẩu thành công!");
            } else {
                callback.onError("Mật khẩu cũ không chính xác!");
            }
        }).start();
    }
}