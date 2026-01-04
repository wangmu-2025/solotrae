package com.example.examquestionbank;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private QuestionManager questionManager;
    private TextView tvQuestionNumber, tvQuestion, tvResult, tvQuestionType;
    private Button btnPrevious, btnNext, btnShowWrong, btnSubmitAnswer, btnUpload;
    
    // 选项组件
    private LinearLayout llOptions, llJudgeOptions;
    private RadioButton rbOptionA, rbOptionB, rbOptionC, rbOptionD, rbJudgeTrue, rbJudgeFalse;
    private CheckBox cbOptionA, cbOptionB, cbOptionC, cbOptionD;
    private TextView tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private LinearLayout llOptionA, llOptionB, llOptionC, llOptionD, llJudgeTrue, llJudgeFalse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. 初始化视图组件
        initViews();

        // 2. 初始化题库管理器
        questionManager = new QuestionManager();

        // 3. 获取从题库选择界面传递过来的题库ID
        String bankId = getIntent().getStringExtra("QUESTION_BANK_ID");
        String bankTitle = getIntent().getStringExtra("QUESTION_BANK_TITLE");

        // 4. 加载对应题库的题目
        questionManager.loadQuestions(this, bankId);

        // 5. 如果有题库标题，设置为ActionBar标题
        if (bankTitle != null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(bankTitle);
            }
        }

        // 6. 显示第一道题目
        showCurrentQuestion();

        // 7. 设置所有按钮的点击监听器
        setupClickListeners();
    }

    private void initViews() {
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvResult = findViewById(R.id.tvResult);
        tvQuestionType = findViewById(R.id.tvQuestionType);
        
        // 选项容器
        llOptions = findViewById(R.id.llOptions);
        llJudgeOptions = findViewById(R.id.llJudgeOptions);
        
        // 单选题/多选题组件
        rbOptionA = findViewById(R.id.rbOptionA);
        rbOptionB = findViewById(R.id.rbOptionB);
        rbOptionC = findViewById(R.id.rbOptionC);
        rbOptionD = findViewById(R.id.rbOptionD);
        cbOptionA = findViewById(R.id.cbOptionA);
        cbOptionB = findViewById(R.id.cbOptionB);
        cbOptionC = findViewById(R.id.cbOptionC);
        cbOptionD = findViewById(R.id.cbOptionD);
        tvOptionA = findViewById(R.id.tvOptionA);
        tvOptionB = findViewById(R.id.tvOptionB);
        tvOptionC = findViewById(R.id.tvOptionC);
        tvOptionD = findViewById(R.id.tvOptionD);
        llOptionA = findViewById(R.id.llOptionA);
        llOptionB = findViewById(R.id.llOptionB);
        llOptionC = findViewById(R.id.llOptionC);
        llOptionD = findViewById(R.id.llOptionD);
        
        // 判断题组件
        rbJudgeTrue = findViewById(R.id.rbJudgeTrue);
        rbJudgeFalse = findViewById(R.id.rbJudgeFalse);
        llJudgeTrue = findViewById(R.id.llJudgeTrue);
        llJudgeFalse = findViewById(R.id.llJudgeFalse);
        
        // 按钮
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnShowWrong = findViewById(R.id.btnShowWrong);
        btnSubmitAnswer = findViewById(R.id.btnSubmitAnswer);
        btnUpload = findViewById(R.id.btnUpload);
    }

    private void showCurrentQuestion() {
        Question currentQ = questionManager.getCurrentQuestion();
        if (currentQ != null) {
            // 更新题号、题目文本
            tvQuestionNumber.setText("第" + questionManager.getCurrentQuestionNumber() + "题/共" + questionManager.getTotalQuestions() + "题");
            tvQuestion.setText(currentQ.getText());
            
            // 更新题目类型
            String typeText = "单选题";
            if (Question.TYPE_MULTIPLE.equals(currentQ.getType())) {
                typeText = "多选题";
            } else if (Question.TYPE_JUDGE.equals(currentQ.getType())) {
                typeText = "判断题";
            }
            tvQuestionType.setText(typeText);
            
            // 清空上一题的答题结果提示
            tvResult.setText("请选择答案");
            
            // 根据题目类型显示不同的选项UI
            resetOptions();
            
            if (Question.TYPE_JUDGE.equals(currentQ.getType())) {
                // 判断题
                llOptions.setVisibility(View.GONE);
                llJudgeOptions.setVisibility(View.VISIBLE);
            } else {
                // 单选题或多选题
                llOptions.setVisibility(View.VISIBLE);
                llJudgeOptions.setVisibility(View.GONE);
                
                // 设置选项文本
                tvOptionA.setText("A. " + currentQ.getOptionA());
                tvOptionB.setText("B. " + currentQ.getOptionB());
                tvOptionC.setText("C. " + currentQ.getOptionC());
                tvOptionD.setText("D. " + currentQ.getOptionD());
                
                // 根据题型显示对应的选择组件
                if (Question.TYPE_SINGLE.equals(currentQ.getType())) {
                    // 单选题：显示单选按钮，隐藏复选框
                    rbOptionA.setVisibility(View.VISIBLE);
                    rbOptionB.setVisibility(View.VISIBLE);
                    rbOptionC.setVisibility(View.VISIBLE);
                    rbOptionD.setVisibility(View.VISIBLE);
                    cbOptionA.setVisibility(View.GONE);
                    cbOptionB.setVisibility(View.GONE);
                    cbOptionC.setVisibility(View.GONE);
                    cbOptionD.setVisibility(View.GONE);
                } else if (Question.TYPE_MULTIPLE.equals(currentQ.getType())) {
                    // 多选题：显示复选框，隐藏单选按钮
                    rbOptionA.setVisibility(View.GONE);
                    rbOptionB.setVisibility(View.GONE);
                    rbOptionC.setVisibility(View.GONE);
                    rbOptionD.setVisibility(View.GONE);
                    cbOptionA.setVisibility(View.VISIBLE);
                    cbOptionB.setVisibility(View.VISIBLE);
                    cbOptionC.setVisibility(View.VISIBLE);
                    cbOptionD.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    
    // 重置选项状态
    private void resetOptions() {
        // 重置单选题/多选题选项
        rbOptionA.setChecked(false);
        rbOptionB.setChecked(false);
        rbOptionC.setChecked(false);
        rbOptionD.setChecked(false);
        cbOptionA.setChecked(false);
        cbOptionB.setChecked(false);
        cbOptionC.setChecked(false);
        cbOptionD.setChecked(false);
        
        // 重置判断题选项
        rbJudgeTrue.setChecked(false);
        rbJudgeFalse.setChecked(false);
    }

    private void setupClickListeners() {
        // 提交答案按钮
        btnSubmitAnswer.setOnClickListener(v -> submitAnswer());
        
        // 选项点击事件（容器点击）
        llOptionA.setOnClickListener(v -> toggleOption("A"));
        llOptionB.setOnClickListener(v -> toggleOption("B"));
        llOptionC.setOnClickListener(v -> toggleOption("C"));
        llOptionD.setOnClickListener(v -> toggleOption("D"));
        
        // 判断题选项点击事件
        llJudgeTrue.setOnClickListener(v -> {
            rbJudgeTrue.setChecked(true);
            rbJudgeFalse.setChecked(false);
        });
        llJudgeFalse.setOnClickListener(v -> {
            rbJudgeFalse.setChecked(true);
            rbJudgeTrue.setChecked(false);
        });

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
        
        // 上传题库按钮
        btnUpload.setOnClickListener(v -> showUploadDialog());
    }
    
    // 切换选项（单选或多选）
    private void toggleOption(String option) {
        Question currentQ = questionManager.getCurrentQuestion();
        if (currentQ == null) return;
        
        if (Question.TYPE_SINGLE.equals(currentQ.getType())) {
            // 单选题：只能选择一个选项
            resetRadioButtons();
            switch (option) {
                case "A": rbOptionA.setChecked(true); break;
                case "B": rbOptionB.setChecked(true); break;
                case "C": rbOptionC.setChecked(true); break;
                case "D": rbOptionD.setChecked(true); break;
            }
        } else if (Question.TYPE_MULTIPLE.equals(currentQ.getType())) {
            // 多选题：可以选择多个选项
            switch (option) {
                case "A": cbOptionA.setChecked(!cbOptionA.isChecked()); break;
                case "B": cbOptionB.setChecked(!cbOptionB.isChecked()); break;
                case "C": cbOptionC.setChecked(!cbOptionC.isChecked()); break;
                case "D": cbOptionD.setChecked(!cbOptionD.isChecked()); break;
            }
        }
    }
    
    // 重置单选按钮
    private void resetRadioButtons() {
        rbOptionA.setChecked(false);
        rbOptionB.setChecked(false);
        rbOptionC.setChecked(false);
        rbOptionD.setChecked(false);
    }
    
    // 提交答案
    private void submitAnswer() {
        Question currentQ = questionManager.getCurrentQuestion();
        if (currentQ == null) return;
        
        String selectedAnswer = getSelectedAnswer();
        if (selectedAnswer.isEmpty()) {
            Toast.makeText(this, "请选择答案后再提交", Toast.LENGTH_SHORT).show();
            return;
        }
        
        checkAnswer(selectedAnswer);
    }
    
    // 获取当前选择的答案
    private String getSelectedAnswer() {
        Question currentQ = questionManager.getCurrentQuestion();
        if (currentQ == null) return "";
        
        if (Question.TYPE_JUDGE.equals(currentQ.getType())) {
            // 判断题：返回A（对）或B（错）
            if (rbJudgeTrue.isChecked()) {
                return "A";
            } else if (rbJudgeFalse.isChecked()) {
                return "B";
            }
        } else if (Question.TYPE_SINGLE.equals(currentQ.getType())) {
            // 单选题：返回选中的选项
            if (rbOptionA.isChecked()) return "A";
            if (rbOptionB.isChecked()) return "B";
            if (rbOptionC.isChecked()) return "C";
            if (rbOptionD.isChecked()) return "D";
        } else if (Question.TYPE_MULTIPLE.equals(currentQ.getType())) {
            // 多选题：返回选中的选项，用逗号分隔
            StringBuilder selected = new StringBuilder();
            if (cbOptionA.isChecked()) selected.append("A,");
            if (cbOptionB.isChecked()) selected.append("B,");
            if (cbOptionC.isChecked()) selected.append("C,");
            if (cbOptionD.isChecked()) selected.append("D,");
            
            // 移除末尾的逗号
            if (selected.length() > 0) {
                selected.setLength(selected.length() - 1);
            }
            return selected.toString();
        }
        
        return ""; // 未选择任何答案
    }

    // 检查答案的核心方法
    private void checkAnswer(String selectedOption) {
        Question currentQ = questionManager.getCurrentQuestion();
        if (currentQ == null) return;
        
        boolean isCorrect = questionManager.checkAnswer(selectedOption);
        String resultText;

        if (isCorrect) {
            resultText = "✅ 回答正确！";
            questionManager.recordAnswer(true, currentQ);
        } else {
            resultText = "❌ 回答错误！正确答案是：" + currentQ.getAnswer();
        }
        tvResult.setText(resultText);
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
    
    // 显示上传题库的对话框
    private void showUploadDialog() {
        new AlertDialog.Builder(this)
                .setTitle("上传题库")
                .setMessage("点击确认后，将打开文件选择器，您可以选择JSON格式的题库文件进行上传。")
                .setPositiveButton("确认", (dialog, which) -> {
                    // 这里可以添加打开文件选择器的代码
                    Toast.makeText(this, "上传功能开发中，敬请期待！", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("取消", null)
                .show();
    }
}