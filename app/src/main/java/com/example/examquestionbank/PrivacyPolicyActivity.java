package com.example.examquestionbank;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

/**
 * 隐私政策页面
 */
public class PrivacyPolicyActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        // 初始化视图
        initViews();

        // 设置返回按钮点击事件
        toolbar.setNavigationOnClickListener(v -> finish());

        // 设置内容
        setContent();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvContent = findViewById(R.id.tvContent);
    }

    /**
     * 设置隐私政策内容
     */
    private void setContent() {
        String content = "隐私政策\n\n" +
                "欢迎使用考试题库应用！\n\n" +
                "1. 隐私政策的适用范围\n" +
                "1.1 本隐私政策适用于考试题库应用收集、使用、存储和披露用户信息的行为。\n\n" +
                "2. 信息收集\n" +
                "2.1 我们可能收集您的账号信息，包括用户名、密码等。\n" +
                "2.2 我们可能收集您的设备信息，包括设备型号、操作系统版本等。\n" +
                "2.3 我们可能收集您的使用信息，包括浏览记录、答题记录等。\n\n" +
                "3. 信息使用\n" +
                "3.1 我们使用收集的信息为您提供更好的服务。\n" +
                "3.2 我们可能使用您的信息进行数据分析和产品优化。\n" +
                "3.3 我们可能使用您的信息向您推送相关通知和广告。\n\n" +
                "4. 信息保护\n" +
                "4.1 我们采取各种安全措施保护您的信息安全。\n" +
                "4.2 我们不会向第三方泄露您的个人信息，除非获得您的明确同意或法律法规要求。\n\n" +
                "5. 信息共享\n" +
                "5.1 我们不会与任何第三方共享您的个人信息，除非获得您的明确同意。\n" +
                "5.2 在法律法规要求的情况下，我们可能会披露您的信息。\n\n" +
                "6. 隐私政策的变更\n" +
                "6.1 我们可能随时更新本隐私政策。\n" +
                "6.2 更新后的隐私政策将在应用内公告。\n\n" +
                "7. 联系方式\n" +
                "7.1 如您对本隐私政策有任何疑问，欢迎联系我们。\n" +
                "7.2 联系方式：exam_bank@example.com\n\n" +
                "8. 生效日期\n" +
                "8.1 本隐私政策自2026年1月1日起生效。";

        tvContent.setText(content);
    }
}