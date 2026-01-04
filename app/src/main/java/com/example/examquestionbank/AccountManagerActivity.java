package com.example.examquestionbank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

/**
 * 账号管理页面
 * 实现修改密码、绑定手机号、退出登录、切换账号等功能
 */
public class AccountManagerActivity extends AppCompatActivity {

    private TextView tvUsername;
    private Button btnChangePassword;
    private Button btnBindPhone;
    private Button btnSwitchAccount;
    private Button btnLogout;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);

        // 初始化视图
        initViews();

        // 初始化SharedPreferences
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        // 设置当前账号信息
        updateUsername();

        // 设置点击事件
        setClickListeners();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        tvUsername = findViewById(R.id.tvUsername);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnBindPhone = findViewById(R.id.btnBindPhone);
        btnSwitchAccount = findViewById(R.id.btnSwitchAccount);
        btnLogout = findViewById(R.id.btnLogout);
        toolbar = findViewById(R.id.toolbar);
    }

    /**
     * 设置点击事件
     */
    private void setClickListeners() {
        // 返回按钮点击事件
        toolbar.setNavigationOnClickListener(v -> finish());

        // 修改密码按钮点击事件
        btnChangePassword.setOnClickListener(v -> {
            // 跳转到修改密码页面或显示相关功能
            showChangePasswordDialog();
        });

        // 绑定手机号按钮点击事件
        btnBindPhone.setOnClickListener(v -> {
            // 跳转到绑定手机号页面或显示相关功能
            showBindPhoneDialog();
        });

        // 切换账号按钮点击事件
        btnSwitchAccount.setOnClickListener(v -> {
            // 切换账号功能
            switchAccount();
        });

        // 退出登录按钮点击事件
        btnLogout.setOnClickListener(v -> {
            // 退出登录功能
            logout();
        });
    }

    /**
     * 更新当前账号信息
     */
    private void updateUsername() {
        String username = sharedPreferences.getString("username", "");
        if (!username.isEmpty()) {
            tvUsername.setText(username);
        } else {
            tvUsername.setText("未登录");
        }
    }

    /**
     * 显示修改密码对话框
     */
    private void showChangePasswordDialog() {
        // 这里可以实现修改密码的逻辑
        // 例如：显示一个包含旧密码、新密码和确认新密码的对话框
        new android.app.AlertDialog.Builder(this)
                .setTitle("修改密码")
                .setMessage("修改密码功能开发中")
                .setPositiveButton("确定", null)
                .show();
    }

    /**
     * 显示绑定手机号对话框
     */
    private void showBindPhoneDialog() {
        // 这里可以实现绑定手机号的逻辑
        // 例如：显示一个输入手机号和验证码的对话框
        new android.app.AlertDialog.Builder(this)
                .setTitle("绑定手机号")
                .setMessage("绑定手机号功能开发中")
                .setPositiveButton("确定", null)
                .show();
    }

    /**
     * 切换账号功能
     */
    private void switchAccount() {
        // 清除当前登录状态
        sharedPreferences.edit().putBoolean("isLoggedIn", false).putString("username", "").apply();

        // 跳转到登录界面
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * 退出登录功能
     */
    private void logout() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("退出登录")
                .setMessage("确定要退出登录吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    // 清除当前登录状态
                    sharedPreferences.edit().putBoolean("isLoggedIn", false).putString("username", "").apply();

                    // 跳转到登录界面
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("取消", null)
                .show();
    }
}