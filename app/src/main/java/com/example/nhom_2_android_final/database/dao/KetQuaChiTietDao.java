package com.example.nhom_2_android_final.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.nhom_2_android_final.database.entity.KetQuaChiTiet;
import java.util.List;

@Dao
public interface KetQuaChiTietDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<KetQuaChiTiet> chiTiets);

    @Query("SELECT * FROM KetQuaChiTiet WHERE IDKetQua = :idKetQua")
    List<KetQuaChiTiet> getByKetQuaId(String idKetQua);

    @Transaction
    @Query("SELECT CauHoi.*, KetQuaChiTiet.DapAnID as selectedDapAnID " +
           "FROM CauHoi " +
           "INNER JOIN KetQuaChiTiet ON CauHoi.IDCauHoi = KetQuaChiTiet.IDCauHoi " +
           "WHERE KetQuaChiTiet.IDKetQua = :idKetQua")
    List<QuestionReview> getReviewByKetQuaId(String idKetQua);
}
