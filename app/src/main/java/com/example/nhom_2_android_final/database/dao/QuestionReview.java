package com.example.nhom_2_android_final.database.dao;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.example.nhom_2_android_final.database.entity.CauHoi;
import com.example.nhom_2_android_final.database.entity.CauTraLoi;
import java.util.List;

public class QuestionReview {
    @Embedded
    public CauHoi cauHoi;

    @Relation(
            parentColumn = "IDCauHoi",
            entityColumn = "IDCauHoi"
    )
    public List<CauTraLoi> dsCauTraLoi;

    // DapAnID người dùng đã chọn, lấy từ bảng KetQuaChiTiet qua câu lệnh Query
    public String selectedDapAnID;
}
