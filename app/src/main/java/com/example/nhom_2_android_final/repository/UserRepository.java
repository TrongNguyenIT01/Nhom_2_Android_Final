package com.example.nhom_2_android_final.repository;

import android.content.Context;
import com.example.nhom_2_android_final.database.AppDatabase;
import com.example.nhom_2_android_final.database.UserDao;
import com.example.nhom_2_android_final.database.entity.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public interface OnResultListener<T> {
        void onResult(T result);
    }

    public void isUsernameTaken(String username, OnResultListener<Boolean> listener) {
        executorService.execute(() -> {
            User user = userDao.getUserByUsername(username);
            listener.onResult(user != null);
        });
    }

    public void generateNextId(OnResultListener<String> listener) {
        executorService.execute(() -> {
            User lastUser = userDao.getLastUser();
            String nextId;
            if (lastUser == null) {
                nextId = "US001";
            } else {
                String lastId = lastUser.UserID;
                int numericPart = Integer.parseInt(lastId.substring(2));
                nextId = String.format("US%03d", numericPart + 1);
            }
            listener.onResult(nextId);
        });
    }

    public void registerUser(User user, OnResultListener<Boolean> listener) {
        executorService.execute(() -> {
            try {
                userDao.insertUser(user);
                listener.onResult(true);
            } catch (Exception e) {
                listener.onResult(false);
            }
        });
    }

    public void login(String identifier, String password, OnResultListener<User> listener) {
        executorService.execute(() -> {
            User user = userDao.login(identifier, password);
            listener.onResult(user);
        });
    }
}
