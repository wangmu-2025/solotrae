package com.example.examquestionbank;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private QuestionManager questionManager;
    private TextView tvQuestionNumber, tvQuestion, tvResult, tvStatistics;
    private Button btnOptionA, btnOptionB, btnOptionC, btnOptionD;
    private Button btnPrevious, btnNext, btnShowWrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. 初始化视图组件
        private Button btnPrevious, btnNext, btnShowWrong, btnUpload;
        initViews();

        // 2. 初始化题库管理器并加载题目
        questionManager = new QuestionManager();
        questionManager.loadQuestions(this);
        // 3. 显示第一道题目
        showCurrentQuestion();
        // 4. 设置所有按钮的点击监听器
        setupClickListeners();
    }

    private void initViews() {
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvResult = findViewById(R.id.tvResult);
        tvStatistics = findViewById(R.id.tvStatistics);
        btnOptionA = findViewById(R.id.btnOptionA);
        btnOptionB = findViewById(R.id.btnOptionB);
        btnOptionC = findViewById(R.id.btnOptionC);
        btnOptionD = findViewById(R.id.btnOptionD);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnShowWrong = findViewById(R.id.btnShowWrong);
    }

    private void showCurrentQuestion() {
        Question currentQ = questionManager.getCurrentQuestion();
        if (currentQ != null) {
            // 更新题号、题目文本、选项按钮文字
            tvQuestionNumber.setText("第" + questionManager.getCurrentQuestionNumber() + "题/共" + questionManager.getTotalQuestions() + "题");
            tvQuestion.setText(currentQ.getText());
            btnOptionA.setText("A. " + currentQ.getOptionA());
            btnOptionB.setText("B. " + currentQ.getOptionB());
            btnOptionC.setText("C. " + currentQ.getOptionC());
            btnOptionD.setText("D. " + currentQ.getOptionD());
            // 清空上一题的答题结果提示
            tvResult.setText("请选择一个答案");
            // 更新顶部的答题统计
            updateStatistics();
        }
    }

    private void setupClickListeners() {
        // 选项A的点击事件
        btnOptionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer("A");
            }
        });
        // 为选项B、C、D设置类似的监听器（使用Lambda简化）
        btnOptionB.setOnClickListener(v -> checkAnswer("B"));
        btnOptionC.setOnClickListener(v -> checkAnswer("C"));
        btnOptionD.setOnClickListener(v -> checkAnswer("D"));

        // “上一题”按钮
        btnPrevious.setOnClickListener(v -> {
            if (questionManager.previousQuestion()) {
                showCurrentQuestion();
            } else {
                Toast.makeText(MainActivity.this, "已经是第一题了", Toast.LENGTH_SHORT).show();
            }
        });

        // “下一题”按钮
        btnNext.setOnClickListener(v -> {
            if (questionManager.nextQuestion()) {
                showCurrentQuestion();
            } else {
                Toast.makeText(MainActivity.this, "已经是最后一题了", Toast.LENGTH_SHORT).show();
            }
        });

        // “错题本”按钮
        btnShowWrong.setOnClickListener(v -> showWrongQuestionsDialog());
    }

    // 检查答案的核心方法
    private void checkAnswer(String selectedOption) {
        boolean isCorrect = questionManager.checkAnswer(selectedOption);
        Question currentQ = questionManager.getCurrentQuestion();
        String resultText;

        if (isCorrect) {
            resultText = "✅ 回答正确！";
            questionManager.recordAnswer(true, currentQ);
        } else {
            resultText = "❌ 回答错误！正确答案是：" + currentQ.getAnswer();
            questionManager.recordAnswer(false, currentQ);
        }
        tvResult.setText(resultText);
        updateStatistics(); // 每次答题后更新统计
    }

    // 更新顶部的统计信息
    private void updateStatistics() {
        tvStatistics.setText(questionManager.getStatistics());
    }

    // 显示错题本的弹窗
    private void showWrongQuestionsDialog() {
        List<Question> wrongList = questionManager.getWrongQuestions();
        if (wrongList.isEmpty()) {
            Toast.makeText(this, "还没有错题哦，继续保持！", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder message = new StringBuilder("共收集到 " + wrongList.size() + " 道错题：\n\n");
        for (int i = 0; i < wrongList.size(); i++) {
            Question q = wrongList.get(i);
            message.append(i + 1).append(". ").append(q.getText()).append("\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("错题本")
                .setMessage(message.toString())
                .setPositiveButton("知道了", null)
                .show();
    }
}