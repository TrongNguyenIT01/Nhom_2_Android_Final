package com.example.nhom_2_android_final.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.nhom_2_android_final.database.entity.ChuDe;
import java.util.List;

@Dao
public interface ChuDeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ChuDe> chuDes);

    @Query("SELECT * FROM ChuDe")
    List<ChuDe> getAll();
}
