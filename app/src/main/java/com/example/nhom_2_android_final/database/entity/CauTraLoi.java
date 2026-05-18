package com.example.nhom_2_android_final.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "CauTraLoi",
        foreignKeys = @ForeignKey(
                entity = CauHoi.class,
                parentColumns = "IDCauHoi",
                childColumns = "IDCauHoi",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("IDCauHoi")}
)
public class CauTraLoi {
    @PrimaryKey
    @NonNull
    public String DapAnID;

    @ColumnInfo(name = "IDCauHoi")
    public String IDCauHoi;

    @NonNull
    @ColumnInfo(name = "NhanCauHoi")
    public String NhanCauHoi;  // "A", "B", "C", "D"

    @NonNull
    @ColumnInfo(name = "NoiDung")
    public String NoiDung;

    @ColumnInfo(name = "HinhAnh")
    public String HinhAnh;     // có thể null

    @ColumnInfo(name = "DapAnDung")
    public int DapAnDung;      // 1 = đúng, 0 = sai
}
