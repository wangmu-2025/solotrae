package com.example.examquestionbank;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ManualImportActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private EditText etQuestionText;
    private EditText etOptionA;
    private EditText etOptionB;
    private EditText etOptionC;
    private EditText etOptionD;
    private RadioGroup rgAnswer;
    private RadioGroup rgQuestionType;
    private RadioButton rbSingleChoice;
    private RadioButton rbMultipleChoice;
    private RadioButton rbJudge;
    private LinearLayout llMultipleAnswers;
    private CheckBox cbAnswerA;
    private CheckBox cbAnswerB;
    private CheckBox cbAnswerC;
    private CheckBox cbAnswerD;
    private EditText etExplanation;
    private Button btnSaveQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_import);

        // 初始化视图
        initViews();

        // 设置点击事件
        setupClickListeners();
    }

    // 初始化视图组件
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etQuestionText = findViewById(R.id.etQuestionText);
        etOptionA = findViewById(R.id.etOptionA);
        etOptionB = findViewById(R.id.etOptionB);
        etOptionC = findViewById(R.id.etOptionC);
        etOptionD = findViewById(R.id.etOptionD);
        rgAnswer = findViewById(R.id.rgAnswer);
        rgQuestionType = findViewById(R.id.rgQuestionType);
        rbSingleChoice = findViewById(R.id.rbSingleChoice);
        rbMultipleChoice = findViewById(R.id.rbMultipleChoice);
        rbJudge = findViewById(R.id.rbJudge);
        llMultipleAnswers = findViewById(R.id.llMultipleAnswers);
        cbAnswerA = findViewById(R.id.cbAnswerA);
        cbAnswerB = findViewById(R.id.cbAnswerB);
        cbAnswerC = findViewById(R.id.cbAnswerC);
        cbAnswerD = findViewById(R.id.cbAnswerD);
        etExplanation = findViewById(R.id.etExplanation);
        btnSaveQuestion = findViewById(R.id.btnSaveQuestion);
    }

    // 设置点击事件
    private void setupClickListeners() {
        // 返回按钮点击事件
        toolbar.setNavigationOnClickListener(v -> finish());

        // 保存按钮点击事件
        btnSaveQuestion.setOnClickListener(v -> saveQuestion());
        
        // 题型选择点击事件
        rgQuestionType.setOnCheckedChangeListener((group, checkedId) -> {
            updateUIByQuestionType();
        });
    }
    
    // 根据题型更新UI
    private void updateUIByQuestionType() {
        int checkedId = rgQuestionType.getCheckedRadioButtonId();
        if (checkedId == R.id.rbMultipleChoice) {
            // 多选题：显示多选答案选择，隐藏单选答案选择
            llMultipleAnswers.setVisibility(View.VISIBLE);
            rgAnswer.setVisibility(View.GONE);
        } else {
            // 单选题或判断题：显示单选答案选择，隐藏多选答案选择
            llMultipleAnswers.setVisibility(View.GONE);
            rgAnswer.setVisibility(View.VISIBLE);
        }
        
        if (checkedId == R.id.rbJudge) {
            // 判断题：只显示选项A和B，隐藏选项C和D
            etOptionC.setVisibility(View.GONE);
            etOptionD.setVisibility(View.GONE);
        } else {
            // 单选题或多选题：显示所有选项
            etOptionC.setVisibility(View.VISIBLE);
            etOptionD.setVisibility(View.VISIBLE);
        }
    }

    // 保存题目
    private void saveQuestion() {
        // 获取用户输入
        String questionText = etQuestionText.getText().toString().trim();
        String optionA = etOptionA.getText().toString().trim();
        String optionB = etOptionB.getText().toString().trim();
        String optionC = etOptionC.getText().toString().trim();
        String optionD = etOptionD.getText().toString().trim();
        String explanation = etExplanation.getText().toString().trim();
        
        // 获取选中的题型
        int questionTypeId = rgQuestionType.getCheckedRadioButtonId();
        String type = Question.TYPE_SINGLE;
        if (questionTypeId == R.id.rbMultipleChoice) {
            type = Question.TYPE_MULTIPLE;
        } else if (questionTypeId == R.id.rbJudge) {
            type = Question.TYPE_JUDGE;
        }

        // 验证输入
        if (questionText.isEmpty() || optionA.isEmpty() || optionB.isEmpty()) {
            Toast.makeText(this, "请填写完整的题目信息", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (questionTypeId != R.id.rbJudge && (optionC.isEmpty() || optionD.isEmpty())) {
            Toast.makeText(this, "单选题和多选题需要填写所有选项", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取正确答案
        String answer;
        if (questionTypeId == R.id.rbMultipleChoice) {
            // 多选题：获取选中的答案，用逗号分隔
            StringBuilder selectedAnswers = new StringBuilder();
            if (cbAnswerA.isChecked()) selectedAnswers.append("A,");
            if (cbAnswerB.isChecked()) selectedAnswers.append("B,");
            if (cbAnswerC.isChecked()) selectedAnswers.append("C,");
            if (cbAnswerD.isChecked()) selectedAnswers.append("D,");
            
            if (selectedAnswers.length() == 0) {
                Toast.makeText(this, "请选择正确答案", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // 移除末尾的逗号
            answer = selectedAnswers.substring(0, selectedAnswers.length() - 1);
        } else {
            // 单选题或判断题：获取选中的答案
            int selectedAnswerId = rgAnswer.getCheckedRadioButtonId();
            if (selectedAnswerId == -1) {
                Toast.makeText(this, "请选择正确答案", Toast.LENGTH_SHORT).show();
                return;
            }
            
            RadioButton selectedRadioButton = findViewById(selectedAnswerId);
            answer = selectedRadioButton.getText().toString();
        }

        // 创建题目对象
        Question question = new Question(
                UUID.randomUUID().toString(),
                questionText,
                optionA,
                optionB,
                optionC,
                optionD,
                answer,
                explanation,
                "中等", // 默认难度
                new ArrayList<>(), // 默认无标签
                type
        );

        // 保存题目到JSON文件
        saveQuestionToJson(question);
    }

    // 将题目保存到JSON文件
    private void saveQuestionToJson(Question question) {
        try {
            // 读取现有题库或创建新题库
            File jsonFile = new File(getFilesDir(), "manual_questions.json");
            QuestionBank questionBank;
            Gson gson = new Gson();

            if (jsonFile.exists()) {
                // 读取现有题库
                try (FileReader reader = new FileReader(jsonFile)) {
                    questionBank = gson.fromJson(reader, QuestionBank.class);
                }
            } else {
                // 创建新题库
                questionBank = new QuestionBank(
                        "manual",
                        "手动导入题库",
                        "手动导入的题目集合",
                        "自定义",
                        new ArrayList<>()
                );
            }

            // 添加新题目
            List<Question> questions = questionBank.getQuestions();
            if (questions == null) {
                questions = new ArrayList<>();
            }
            questions.add(question);
            questionBank.setQuestions(questions);
            questionBank.setUpdatedAt(System.currentTimeMillis());

            // 保存到JSON文件
            try (FileWriter writer = new FileWriter(jsonFile)) {
                gson.toJson(questionBank, writer);
                Toast.makeText(this, "题目保存成功", Toast.LENGTH_SHORT).show();
                // 清空输入，准备下一题
                clearInputs();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "题目保存失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // 清空输入
    private void clearInputs() {
        etQuestionText.setText("");
        etOptionA.setText("");
        etOptionB.setText("");
        etOptionC.setText("");
        etOptionD.setText("");
        rgAnswer.clearCheck();
        etExplanation.setText("");
        
        // 重置题型选择为单选题
        rbSingleChoice.setChecked(true);
        updateUIByQuestionType();
        
        // 重置复选框
        cbAnswerA.setChecked(false);
        cbAnswerB.setChecked(false);
        cbAnswerC.setChecked(false);
        cbAnswerD.setChecked(false);
    }
}