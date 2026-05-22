package com.example.nhom_2_android_final.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.nhom_2_android_final.database.entity.CauHoi;
import java.util.List;

@Dao
public interface CauHoiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CauHoi> cauHois);

    @Query("SELECT * FROM CauHoi")
    List<CauHoi> getAll();

    @Query("SELECT * FROM CauHoi WHERE IDBaiKT = :idBaiKT")
    List<CauHoi> getByBaiKiemTra(String idBaiKT);
}
