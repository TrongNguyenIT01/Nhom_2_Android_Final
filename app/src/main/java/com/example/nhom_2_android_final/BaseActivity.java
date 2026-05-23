package com.example.nhom_2_android_final;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private String appliedFontSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lưu lại kích cỡ chữ lúc Activity được tạo
        appliedFontSize = getSharedPreferences("AppPrefs", MODE_PRIVATE).getString("FontSize", "medium");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Kiểm tra xem kích cỡ chữ trong cài đặt có thay đổi so với lúc tạo Activity không
        String currentFontSize = getSharedPreferences("AppPrefs", MODE_PRIVATE).getString("FontSize", "medium");
        if (appliedFontSize != null && !appliedFontSize.equals(currentFontSize)) {
            // Nếu thay đổi, nạp lại Activity để áp dụng kích cỡ mới
            recreate();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String fontSize = prefs.getString("FontSize", "medium");
        
        float scale;
        if (fontSize == null) fontSize = "medium";
        switch (fontSize) {
            case "small":
                scale = 0.85f;
                break;
            case "large":
                scale = 1.2f;
                break;
            default:
                scale = 1.0f;
                break;
        }

        Configuration configuration = new Configuration(newBase.getResources().getConfiguration());
        configuration.fontScale = scale;
        Context context = newBase.createConfigurationContext(configuration);
        super.attachBaseContext(context);
    }
}
