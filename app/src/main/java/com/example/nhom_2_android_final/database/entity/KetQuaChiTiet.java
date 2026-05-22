package com.example.nhom_2_android_final.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "KetQuaChiTiet",
        foreignKeys = {
                @ForeignKey(entity = KetQua.class,
                        parentColumns = "IDKetQua", childColumns = "IDKetQua",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = CauHoi.class,
                        parentColumns = "IDCauHoi", childColumns = "IDCauHoi",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("IDKetQua"), @Index("IDCauHoi")}
)
public class KetQuaChiTiet {
    @PrimaryKey
    @NonNull
    public String IDKQCT; // id sẽ có kiểu IDKetQua + "_" + IDCauHoi
    @NonNull
    @ColumnInfo(name = "IDKetQua")
    public String IDKetQua;
    @NonNull
    @ColumnInfo(name = "IDCauHoi")
    public String IDCauHoi;
    @NonNull
    @ColumnInfo(name = "DapAnID")
    public String DapAnID;     // dùng Integer (không phải int) để cho phép NULL (bỏ qua câu)
}
