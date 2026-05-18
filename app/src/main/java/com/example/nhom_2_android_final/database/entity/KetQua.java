package com.example.nhom_2_android_final.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "KetQua",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "UserID", childColumns = "UserID",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = BaiKiemTra.class,
                        parentColumns = "IDBaiKT", childColumns = "IDBaiKT",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("UserID"), @Index("IDBaiKT")}
)
public class KetQua {
    @PrimaryKey
    @NonNull
    public String IDKetQua;

    @ColumnInfo(name = "UserID")
    public String UserID;

    @ColumnInfo(name = "IDBaiKT")
    public String IDBaiKT;

    @ColumnInfo(name = "Diem")
    public float Diem;          // VD: 8.5

    @ColumnInfo(name = "SoCauDung")
    public int SoCauDung;

    @ColumnInfo(name = "SoCauSai")
    public int SoCauSai;

    @ColumnInfo(name = "ThoiGianKT")
    public int ThoiGianKT;      // thời gian thực tế làm bài (giây)

    @NonNull
    @ColumnInfo(name = "NgayNop")
    public String NgayNop;      // "yyyy-MM-dd HH:mm:ss"
}