package com.example.nhom_2_android_final.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "ChuDe")
public class ChuDe {
    @PrimaryKey
    @NonNull
    public String IDChuDe;

    @NonNull
    @ColumnInfo(name = "TenChuDe")
    public String TenChuDe;

    @ColumnInfo(name = "KhoiLop")
    public int KhoiLop;
}