package com.example.examquestionbank;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // 初始化按钮
        Button btnStartPractice = findViewById(R.id.btnStartPractice);
        Button btnWrongQuestions = findViewById(R.id.btnWrongQuestions);
        Button btnSettings = findViewById(R.id.btnSettings);

        // 设置点击事件
        btnStartPractice.setOnClickListener(v -> {
            // 跳转到题库选择界面
            Intent intent = new Intent(this, QuestionBankSelectActivity.class);
            startActivity(intent);
        });

        btnWrongQuestions.setOnClickListener(v -> {
            // 跳转到错题本
            Intent intent = new Intent(this, WrongQuestionsActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            // 跳转到设置界面
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}