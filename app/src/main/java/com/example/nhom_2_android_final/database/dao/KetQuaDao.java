package com.example.nhom_2_android_final.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.nhom_2_android_final.database.entity.KetQua;
import java.util.List;

@Dao
public interface KetQuaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(KetQua ketQua);

    @Query("SELECT * FROM KetQua WHERE UserID = :userId ORDER BY NgayNop DESC")
    List<KetQua> getHistoryByUserId(String userId);

    @androidx.room.Transaction
    @Query("SELECT * FROM KetQua WHERE UserID = :userId ORDER BY NgayNop DESC")
    List<HistoryResult> getFullHistoryByUserId(String userId);

    @Query("SELECT * FROM KetQua WHERE IDKetQua = :idKetQua")
    KetQua getById(String idKetQua);
}
