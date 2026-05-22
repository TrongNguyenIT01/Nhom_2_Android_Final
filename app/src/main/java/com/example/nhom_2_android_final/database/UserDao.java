package com.example.nhom_2_android_final.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nhom_2_android_final.database.entity.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM User WHERE UserID = :userId LIMIT 1")
    User getUserById(String userId);

    @Query("SELECT * FROM User ORDER BY UserID DESC LIMIT 1")
    User getLastUser();

    @Query("SELECT * FROM User WHERE Username = :username LIMIT 1")
    User getUserByUsername(String username);

    @Query("SELECT * FROM User WHERE Email = :email LIMIT 1")
    User findUserByEmail(String email);

    @Query("SELECT * FROM User WHERE (Email = :identifier OR UserID = :identifier) AND Password = :password LIMIT 1")
    User login(String identifier, String password);
}
