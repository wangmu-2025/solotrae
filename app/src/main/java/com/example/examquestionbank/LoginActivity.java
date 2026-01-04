package com.example.examquestionbank;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化视图
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // 设置点击事件
        btnLogin.setOnClickListener(v -> login());
        btnRegister.setOnClickListener(v -> register());
    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 简单的输入验证
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        // 从SharedPreferences中验证用户信息
        android.content.SharedPreferences sp = getSharedPreferences("users", MODE_PRIVATE);
        boolean userExists = sp.getBoolean("user_exists_" + username, false);
        String savedPassword = sp.getString("password_" + username, "");

        // 硬编码的admin账户仍然保留，方便测试
        if ((userExists && password.equals(savedPassword)) || (username.equals("admin") && password.equals("123456"))) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            
            // 保存登录状态
            android.content.SharedPreferences userSp = getSharedPreferences("user", MODE_PRIVATE);
            userSp.edit().putBoolean("isLoggedIn", true).putString("username", username).apply();
            
            // 跳转到主菜单界面
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        // 跳转到注册界面
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}