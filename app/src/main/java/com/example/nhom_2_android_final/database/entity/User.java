package com.example.nhom_2_android_final.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {

    @PrimaryKey
    @NonNull
    public String UserID;

    @NonNull
    @ColumnInfo(name = "Username")
    public String Username;

    @NonNull
    @ColumnInfo(name = "Password")
    public String Password;

    @ColumnInfo(name = "HoTen")
    public String HoTen;

    @ColumnInfo(name = "Email")
    public String Email;

    @ColumnInfo(name = "SDT")
    public String SDT;

    @ColumnInfo(name = "KhoiLop")
    public int KhoiLop;

    @ColumnInfo(name = "AVT")
    public String AVT;
}