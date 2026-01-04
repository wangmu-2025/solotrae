package com.example.examquestionbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.appbar.MaterialToolbar;
import android.widget.Button;

public class PracticeModeSelectActivity extends AppCompatActivity {

    // 练习模式常量
    public static final String EXTRA_MODE = "PRACTICE_MODE";
    public static final String MODE_SEQUENTIAL = "sequential";
    public static final String MODE_RANDOM = "random";
    public static final String MODE_SIMULATION = "simulation";

    private String questionBankId;
    private String questionBankTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_mode_select);

        // 初始化视图
        initViews();

        // 获取传递的题库信息
        Intent intent = getIntent();
        questionBankId = intent.getStringExtra("QUESTION_BANK_ID");
        questionBankTitle = intent.getStringExtra("QUESTION_BANK_TITLE");
    }

    // 初始化视图
    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        CardView cardSequential = findViewById(R.id.cardSequential);
        CardView cardRandom = findViewById(R.id.cardRandom);
        CardView cardSimulation = findViewById(R.id.cardSimulation);
        Button btnBack = findViewById(R.id.btnBack);

        // 设置返回按钮点击事件
        toolbar.setNavigationOnClickListener(v -> finish());

        // 设置顺序练习点击事件
        cardSequential.setOnClickListener(v -> startPractice(MODE_SEQUENTIAL));

        // 设置随机练习点击事件
        cardRandom.setOnClickListener(v -> startPractice(MODE_RANDOM));

        // 设置模拟考试点击事件
        cardSimulation.setOnClickListener(v -> startPractice(MODE_SIMULATION));

        // 设置返回题库列表按钮点击事件
        btnBack.setOnClickListener(v -> finish());
    }

    // 启动练习
    private void startPractice(String mode) {
        Intent intent;
        
        // 根据选择的模式跳转到对应的Activity
        switch (mode) {
            case MODE_SEQUENTIAL:
                // 跳转到顺序练习界面
                intent = new Intent(this, SequentialPracticeActivity.class);
                break;
            case MODE_RANDOM:
                // 跳转到随机练习界面
                intent = new Intent(this, RandomPracticeActivity.class);
                break;
            case MODE_SIMULATION:
                // 跳转到模拟考试界面
                intent = new Intent(this, SimulationExamActivity.class);
                break;
            default:
                // 默认跳转到顺序练习界面
                intent = new Intent(this, SequentialPracticeActivity.class);
                break;
        }
        
        // 传递题库信息
        intent.putExtra("QUESTION_BANK_ID", questionBankId);
        intent.putExtra("QUESTION_BANK_TITLE", questionBankTitle);
        
        // 传递练习模式
        intent.putExtra(EXTRA_MODE, mode);
        
        // 启动Activity
        startActivity(intent);
    }
}