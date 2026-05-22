package com.example.nhom_2_android_final.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.nhom_2_android_final.database.entity.CauTraLoi;
import java.util.List;

@Dao
public interface CauTraLoiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CauTraLoi> cauTraLois);

    @Query("SELECT * FROM CauTraLoi")
    List<CauTraLoi> getAll();

    @Query("SELECT * FROM CauTraLoi WHERE IDCauHoi = :idCauHoi")
    List<CauTraLoi> getByCauHoi(String idCauHoi);
}
