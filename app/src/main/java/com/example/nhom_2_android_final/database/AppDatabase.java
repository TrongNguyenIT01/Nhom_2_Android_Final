package com.example.nhom_2_android_final.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.nhom_2_android_final.database.dao.BaiKiemTraDao;
import com.example.nhom_2_android_final.database.dao.CauHoiDao;
import com.example.nhom_2_android_final.database.dao.CauTraLoiDao;
import com.example.nhom_2_android_final.database.dao.ChuDeDao;
import com.example.nhom_2_android_final.database.dao.DataWrapper;
import com.example.nhom_2_android_final.database.dao.KetQuaChiTietDao;
import com.example.nhom_2_android_final.database.dao.KetQuaDao;
import com.example.nhom_2_android_final.database.UserDao;
import com.example.nhom_2_android_final.database.entity.BaiKiemTra;
import com.example.nhom_2_android_final.database.entity.CauHoi;
import com.example.nhom_2_android_final.database.entity.CauTraLoi;
import com.example.nhom_2_android_final.database.entity.ChuDe;
import com.example.nhom_2_android_final.database.entity.KetQua;
import com.example.nhom_2_android_final.database.entity.KetQuaChiTiet;
import com.example.nhom_2_android_final.database.entity.User;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

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
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract ChuDeDao chuDeDao();
    public abstract BaiKiemTraDao baiKiemTraDao();
    public abstract CauHoiDao cauHoiDao();
    public abstract CauTraLoiDao cauTraLoiDao();
    public abstract KetQuaDao ketQuaDao();
    public abstract KetQuaChiTietDao ketQuaChiTietDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "LuyenThiToan.db"
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        fillWithStartingData(context.getApplicationContext(), getInstance(context));
                                    });
                                }

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

    private static void fillWithStartingData(Context context, AppDatabase db) {
        try {
            InputStream is = context.getAssets().open("data.json");
            InputStreamReader reader = new InputStreamReader(is);
            
            Gson gson = new Gson();
            DataWrapper data = gson.fromJson(reader, DataWrapper.class);

            if (data != null) {
                db.runInTransaction(() -> {
                    // 1. Nạp Chủ đề
                    if (data.chuDes != null && !data.chuDes.isEmpty()) {
                        db.chuDeDao().insertAll(data.chuDes);
                    }
                    
                    // 2. Nạp Bài kiểm tra
                    if (data.baiKiemTras != null && !data.baiKiemTras.isEmpty()) {
                        db.baiKiemTraDao().insertAll(data.baiKiemTras);
                    }
                    
                    // 3. Nạp Câu hỏi
                    if (data.cauHois != null && !data.cauHois.isEmpty()) {
                        db.cauHoiDao().insertAll(data.cauHois);
                    }

                    // 4. Nạp Câu trả lời
                    if (data.cauTraLois != null && !data.cauTraLois.isEmpty()) {
                        db.cauTraLoiDao().insertAll(data.cauTraLois);
                    }
                });
                
                Log.d("AppDatabase", "Seeding success! Data loaded directly from JSON.");
            }
        } catch (Exception e) {
            Log.e("AppDatabase", "Seeding failed", e);
        }
    }
}
