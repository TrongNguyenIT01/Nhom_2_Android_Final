package com.example.nhom_2_android_final;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.nhom_2_android_final.Model.Regis;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends BaseActivity {

    private TextInputEditText etFullName, etEmail, etPassword, etConfirmPassword;
    private AutoCompleteTextView actvGrade;
    private Regis regisModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_register);

        // Khởi tạo Model xử lý logic
        regisModel = new Regis(this);
        
        initViews();
        setupGradeSpinner();

        findViewById(R.id.btnRegister).setOnClickListener(v -> performRegistration());
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.tvLogin).setOnClickListener(v -> finish());
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        actvGrade = findViewById(R.id.actvGrade);
    }

    private void setupGradeSpinner() {
        String[] grades = {"Khối 10", "Khối 11", "Khối 12"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, grades);
        actvGrade.setAdapter(adapter);
    }

    /**
     * Controller thu thập dữ liệu và gọi Model xử lý
     */
    private void performRegistration() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPass = etConfirmPassword.getText().toString().trim();
        String gradeStr = actvGrade.getText().toString();

        // Đẩy toàn bộ logic cho Model thực hiện
        regisModel.executeRegistration(fullName, email, password, confirmPass, gradeStr, new Regis.RegistrationCallback() {
            @Override
            public void onSuccess(String userId) {
                // UI update phải chạy trên Main Thread
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công! ID: " + userId, Toast.LENGTH_LONG).show();
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
