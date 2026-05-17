package com.example.nhom_2_android_final.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.nhom_2_android_final.database.entity.BaiKiemTra;
import com.example.nhom_2_android_final.database.entity.CauHoi;
import com.example.nhom_2_android_final.database.entity.CauTraLoi;
import com.example.nhom_2_android_final.database.entity.ChuDe;
import com.example.nhom_2_android_final.database.entity.KetQua;
import com.example.nhom_2_android_final.database.entity.KetQuaChiTiet;
import com.example.nhom_2_android_final.database.entity.User;

@Database(
        entities = {
                User.class,
                ChuDe.class,
                BaiKiemTra.class,
                CauHoi.class,
                CauTraLoi.class,
                KetQua.class,
                KetQuaChiTiet.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "LuyenThiToan.db"
                            )
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    db.execSQL("PRAGMA foreign_keys=ON");
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}