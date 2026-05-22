package com.example.nhom_2_android_final;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String fontSize = prefs.getString("FontSize", "medium");
        
        float scale;
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
