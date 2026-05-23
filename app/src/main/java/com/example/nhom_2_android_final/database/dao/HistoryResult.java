package com.example.nhom_2_android_final.database.dao;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.example.nhom_2_android_final.database.entity.BaiKiemTra;
import com.example.nhom_2_android_final.database.entity.KetQua;

public class HistoryResult {
    @Embedded
    public KetQua ketQua;

    @Relation(
            parentColumn = "IDBaiKT",
            entityColumn = "IDBaiKT"
    )
    public BaiKiemTra baiKiemTra;
}
