package com.example.examquestionbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import android.widget.Button;
import android.widget.Toast;

public class SequentialPracticeActivity extends AppCompatActivity {

    private String questionBankId;
    private String questionBankTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequential_practice);

        // 获取传递的题库信息
        Intent intent = getIntent();
        questionBankId = intent.getStringExtra("QUESTION_BANK_ID");
        questionBankTitle = intent.getStringExtra("QUESTION_BANK_TITLE");

        // 初始化视图
        initViews();
    }

    // 初始化视图
    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        Button btnStartPractice = findViewById(R.id.btnStartPractice);

        // 设置返回按钮点击事件
        toolbar.setNavigationOnClickListener(v -> finish());

        // 设置开始练习按钮点击事件
        btnStartPractice.setOnClickListener(v -> startPractice());
    }

    // 开始练习
    private void startPractice() {
        // 创建意图，跳转到MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        
        // 传递题库信息
        intent.putExtra("QUESTION_BANK_ID", questionBankId);
        intent.putExtra("QUESTION_BANK_TITLE", questionBankTitle);
        
        // 传递练习模式
        intent.putExtra(PracticeModeSelectActivity.EXTRA_MODE, PracticeModeSelectActivity.MODE_SEQUENTIAL);
        
        // 启动Activity
        startActivity(intent);
        
        // 显示开始练习提示
        Toast.makeText(this, "开始顺序练习", Toast.LENGTH_SHORT).show();
    }
}