package com.example.nhom_2_android_final.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "CauHoi",
        foreignKeys = @ForeignKey(
                entity = BaiKiemTra.class,
                parentColumns = "IDBaiKT",
                childColumns = "IDBaiKT",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("IDBaiKT")}
)
public class CauHoi {
    @PrimaryKey
    @NonNull
    public String IDCauHoi;

    @ColumnInfo(name = "IDBaiKT")
    public String IDBaiKT;

    @NonNull
    @ColumnInfo(name = "NoiDung")
    public String NoiDung;

    @ColumnInfo(name = "HinhAnh")
    public String HinhAnh;   // có thể null

    @ColumnInfo(name = "GiaiThich")
    public String GiaiThich;
}