package com.example.examquestionbank;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * 考试参数设置界面
 */
public class ExamConfigActivity extends AppCompatActivity {
    private EditText etExamTime;
    private EditText etPassingScore;
    private RadioGroup rgScoreType;
    private Button btnStartExam;
    
    private String questionBankId;
    private String questionBankTitle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_config);
        
        // 获取传递的题库信息
        questionBankId = getIntent().getStringExtra("QUESTION_BANK_ID");
        questionBankTitle = getIntent().getStringExtra("QUESTION_BANK_TITLE");
        
        // 初始化视图组件
        initViews();
        
        // 设置点击监听器
        setupClickListeners();
    }
    
    private void initViews() {
        etExamTime = findViewById(R.id.etExamTime);
        etPassingScore = findViewById(R.id.etPassingScore);
        rgScoreType = findViewById(R.id.rgScoreType);
        btnStartExam = findViewById(R.id.btnStartExam);
        
        // 设置默认值
        etExamTime.setText("45"); // 默认45分钟
        etPassingScore.setText("60"); // 默认60分/60%
    }
    
    private void setupClickListeners() {
        btnStartExam.setOnClickListener(v -> startExam());
    }
    
    private void startExam() {
        // 获取用户输入的考试参数
        String examTimeStr = etExamTime.getText().toString();
        String passingScoreStr = etPassingScore.getText().toString();
        
        if (examTimeStr.isEmpty() || passingScoreStr.isEmpty()) {
            Toast.makeText(this, R.string.please_fill_in_all_exam_params, Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            // 解析考试参数
            int examTime = Integer.parseInt(examTimeStr);
            int passingScore = Integer.parseInt(passingScoreStr);
            
            // 验证参数有效性
            if (examTime <= 0 || examTime > 300) {
                Toast.makeText(this, R.string.exam_time_range, Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (passingScore < 0 || passingScore > 100) {
                Toast.makeText(this, R.string.passing_score_range, Toast.LENGTH_SHORT).show();
                return;
            }
            
            // 创建考试配置
            ExamConfig examConfig = new ExamConfig();
            examConfig.setExamTime(examTime);
            examConfig.setPassingScore(passingScore);
            
            // 根据选择的分数类型设置百分比标志
            boolean isPercentage = (rgScoreType.getCheckedRadioButtonId() == R.id.rbPercentage);
            examConfig.setPercentage(isPercentage);
            
            // 创建意图，跳转到模拟考试Activity
            Intent intent = new Intent(this, SimulationExamActivity2.class);
            
            // 传递题库信息
            intent.putExtra("QUESTION_BANK_ID", questionBankId);
            intent.putExtra("QUESTION_BANK_TITLE", questionBankTitle);
            
            // 传递考试配置
            intent.putExtra("EXAM_CONFIG", examConfig);
            
            // 传递练习模式
            intent.putExtra(PracticeModeSelectActivity.EXTRA_MODE, PracticeModeSelectActivity.MODE_SIMULATION);
            
            // 启动Activity
            startActivity(intent);
            finish();
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.please_enter_valid_number, Toast.LENGTH_SHORT).show();
        }
    }
}