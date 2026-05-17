package com.example.nhom_2_android_final.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "BaiKiemTra",
        foreignKeys = @ForeignKey(
                entity = ChuDe.class,
                parentColumns = "IDChuDe",
                childColumns = "IDChuDe",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("IDChuDe")}
)
public class BaiKiemTra {
    @PrimaryKey
    @NonNull
    public String IDBaiKT;

    @NonNull
    @ColumnInfo(name = "TenBaiKT")
    public String TenBaiKT;

    @ColumnInfo(name = "IDChuDe")
    public String IDChuDe;

    @ColumnInfo(name = "ThoiGian")
    public int ThoiGian;   // đơn vị: giây

    @ColumnInfo(name = "TrangThai")
    public String TrangThai;  // 1 = hiện, 0 = ẩn

    @ColumnInfo(name = "KhoiLop")
    public int KhoiLop;
}