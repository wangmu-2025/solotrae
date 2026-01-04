package com.example.examquestionbank;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // 初始化按钮
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnAbout = findViewById(R.id.btnAbout);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnAccountManager = findViewById(R.id.btnAccountManager);
        Button btnNotificationSettings = findViewById(R.id.btnNotificationSettings);
        Button btnClearCache = findViewById(R.id.btnClearCache);

        // 设置点击事件
        btnLogout.setOnClickListener(v -> {
            // 退出登录，清除登录状态
            android.content.SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
            sp.edit().putBoolean("isLoggedIn", false).putString("username", "").apply();

            // 跳转到登录界面
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnAbout.setOnClickListener(v -> {
            // 显示关于信息，包含版本号、开发者信息和用户协议/隐私政策跳转
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("关于应用");
            builder.setMessage("考试题库 v1.0\n\n开发者：ExamBank Team\n版本号：1.0\n\n一款帮助用户进行考试复习的应用");
            builder.setPositiveButton("确定", null);
            builder.setNeutralButton("用户协议", (dialog, which) -> {
                // 跳转到用户协议页面
                Intent intent = new Intent(this, UserAgreementActivity.class);
                startActivity(intent);
            });
            builder.setNegativeButton("隐私政策", (dialog, which) -> {
                // 跳转到隐私政策页面
                Intent intent = new Intent(this, PrivacyPolicyActivity.class);
                startActivity(intent);
            });
            builder.show();
        });

        btnBack.setOnClickListener(v -> {
            // 返回上一个界面
            finish();
        });

        btnAccountManager.setOnClickListener(v -> {
            // 跳转到账号管理页面
            Intent intent = new Intent(this, AccountManagerActivity.class);
            startActivity(intent);
        });

        btnNotificationSettings.setOnClickListener(v -> {
            // 跳转到系统通知设置页面
            Intent intent = new Intent();
            
            // 适配不同Android版本
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                // Android 8.0及以上
                intent.setAction(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, getPackageName());
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                // Android 5.0-7.1
                intent.setAction(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra("app_package", getPackageName());
                intent.putExtra("app_uid", getApplicationInfo().uid);
            } else {
                // Android 4.4及以下
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(android.net.Uri.parse("package:" + getPackageName()));
            }
            
            try {
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                // 如果找不到相应的设置页面，跳转到应用详情页面
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(android.net.Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        });

        btnClearCache.setOnClickListener(v -> {
            // 清除缓存功能
            new android.app.AlertDialog.Builder(this)
                .setTitle("清除缓存")
                .setMessage("确定要清除应用缓存吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    // 计算清除前的缓存大小
                    long beforeSize = getCacheSize();
                    
                    // 执行清除缓存操作
                    clearCache();
                    
                    // 计算清除后的缓存大小
                    long afterSize = getCacheSize();
                    
                    // 计算清除的缓存大小
                    long clearedSize = beforeSize - afterSize;
                    
                    // 显示清除结果
                    android.widget.Toast.makeText(this, "缓存已清除，共清除 " + formatFileSize(clearedSize), android.widget.Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("取消", null)
                .show();
        });
    }

    /**
     * 计算应用缓存大小
     */
    private long getCacheSize() {
        long cacheSize = 0;
        
        // 计算内部缓存大小
        cacheSize += getFolderSize(getCacheDir());
        
        // 计算外部缓存大小
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(getExternalCacheDir());
        }
        
        return cacheSize;
    }

    /**
     * 清除应用缓存
     */
    private void clearCache() {
        // 清除内部缓存
        deleteDir(getCacheDir());
        
        // 清除外部缓存
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            deleteDir(getExternalCacheDir());
        }
    }

    /**
     * 删除文件夹
     */
    private boolean deleteDir(java.io.File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new java.io.File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    /**
     * 计算文件夹大小
     */
    private long getFolderSize(java.io.File file) {
        long size = 0;
        if (file != null && file.isDirectory()) {
            for (java.io.File child : file.listFiles()) {
                size += getFolderSize(child);
            }
        } else if (file != null && file.isFile()) {
            size = file.length();
        }
        return size;
    }

    /**
     * 格式化文件大小为可读形式
     */
    private String formatFileSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = {"B", "KB", "MB", "GB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.2f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }
}