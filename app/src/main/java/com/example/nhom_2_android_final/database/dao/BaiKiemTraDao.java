package com.example.nhom_2_android_final.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.nhom_2_android_final.database.entity.BaiKiemTra;
import java.util.List;

@Dao
public interface BaiKiemTraDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BaiKiemTra> baiKiemTras);

    @Query("SELECT * FROM BaiKiemTra")
    List<BaiKiemTra> getAll();

    @Query("SELECT * FROM BaiKiemTra WHERE KhoiLop = :khoiLop AND TrangThai = '1'")
    List<BaiKiemTra> getByKhoiLop(int khoiLop);

    @Query("SELECT * FROM BaiKiemTra WHERE IDChuDe = :idChuDe AND TrangThai = '1'")
    List<BaiKiemTra> getByChuDe(String idChuDe);
    
    @Query("SELECT * FROM BaiKiemTra WHERE IDBaiKT = :idBaiKT")
    BaiKiemTra getById(String idBaiKT);
}
