package com.example.examquestionbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;

public class SimulationExamActivity extends AppCompatActivity {

    private String questionBankId;
    private String questionBankTitle;
    private QuestionManager questionManager;
    private int singleCount = 0;
    private int multipleCount = 0;
    private int judgeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_exam);

        // 获取传递的题库信息
        Intent intent = getIntent();
        questionBankId = intent.getStringExtra("QUESTION_BANK_ID");
        questionBankTitle = intent.getStringExtra("QUESTION_BANK_TITLE");

        // 初始化题库管理器
        questionManager = new QuestionManager();
        // 加载题库
        questionManager.loadQuestions(this, questionBankId);
        // 统计题型数量
        countQuestionTypes();

        // 初始化视图
        initViews();
        // 更新题型数量显示
        updateQuestionTypeDisplay();
    }

    // 统计题型数量
    private void countQuestionTypes() {
        List<Question> questions = questionManager.getQuestions();
        for (Question question : questions) {
            if (Question.TYPE_SINGLE.equals(question.getType())) {
                singleCount++;
            } else if (Question.TYPE_MULTIPLE.equals(question.getType())) {
                multipleCount++;
            } else if (Question.TYPE_JUDGE.equals(question.getType())) {
                judgeCount++;
            }
        }
    }



    // 初始化视图
    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        Button btnStartExam = findViewById(R.id.btnStartExam);
        TextView tvSingleCount = findViewById(R.id.tvSingleCount);
        TextView tvMultipleCount = findViewById(R.id.tvMultipleCount);
        TextView tvJudgeCount = findViewById(R.id.tvJudgeCount);
        LinearLayout llHistoryRecords = findViewById(R.id.llHistoryRecords);

        // 设置返回按钮点击事件
        toolbar.setNavigationOnClickListener(v -> finish());

        // 设置开始考试按钮点击事件
        btnStartExam.setOnClickListener(v -> startExam());

        // 设置历史记录点击事件
        llHistoryRecords.setOnClickListener(v -> {
            // 跳转到练习记录页面
            Intent intent = new Intent(SimulationExamActivity.this, PracticeRecordsActivity.class);
            startActivity(intent);
        });
    }

    // 更新题型数量显示
    private void updateQuestionTypeDisplay() {
        TextView tvSingleCount = findViewById(R.id.tvSingleCount);
        TextView tvMultipleCount = findViewById(R.id.tvMultipleCount);
        TextView tvJudgeCount = findViewById(R.id.tvJudgeCount);
        TextView tvTotalQuestions = findViewById(R.id.tvTotalQuestions);
        TextView tvTotalScore = findViewById(R.id.tvTotalScore);

        // 更新各题型数量
        tvSingleCount.setText(String.valueOf(singleCount));
        tvMultipleCount.setText(String.valueOf(multipleCount));
        tvJudgeCount.setText(String.valueOf(judgeCount));

        // 更新总题数和总分
        int totalQuestions = singleCount + multipleCount + judgeCount;
        double totalScore = totalQuestions * 1.0;
        tvTotalQuestions.setText("总题" + totalQuestions + "题");
        tvTotalScore.setText("共" + (int)totalScore + "分");
    }

    // 开始考试
    private void startExam() {
        // 获取用户输入的考试参数
        EditText etExamTime = findViewById(R.id.etExamTime);
        EditText etPassingScore = findViewById(R.id.etPassingScore);
        
        // 解析输入值，设置默认值
        int examTime = 45;
        double passingScore = 6.0; // 默认及格分数为6分（10道题）
        
        try {
            examTime = Integer.parseInt(etExamTime.getText().toString());
        } catch (NumberFormatException e) {
            examTime = 45;
        }
        
        try {
            passingScore = Double.parseDouble(etPassingScore.getText().toString());
        } catch (NumberFormatException e) {
            passingScore = 6.0; // 默认及格分数为6分（10道题）
        }
        
        // 创建考试配置
        ExamConfig examConfig = new ExamConfig();
        
        // 设置考试参数
        examConfig.setExamTime(examTime); // 用户输入的考试时长
        examConfig.setPassingScore(passingScore); // 用户输入的及格分数
        examConfig.setPercentage(true); // 默认百分比
        
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
        
        // 显示开始考试提示
        Toast.makeText(this, "开始模拟考试", Toast.LENGTH_SHORT).show();
    }
}