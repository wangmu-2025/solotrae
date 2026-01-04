package com.example.examquestionbank;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

/**
 * 模拟考试主界面，实现完整的模拟考试功能
 */
public class SimulationExamActivity2 extends AppCompatActivity {
    private QuestionManager questionManager;
    private ExamConfig examConfig;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean isExamEnded = false;
    private AlertDialog answerCardDialog; // 答题卡对话框引用

    // UI组件
    private TextView tvQuestionNumber, tvQuestion, tvResult, tvQuestionType, tvCountdown;
    private Button btnPrevious, btnNext, btnSubmitAnswer, btnAnswerCard;
    private LinearLayout llOptions, llJudgeOptions;
    private RadioButton rbOptionA, rbOptionB, rbOptionC, rbOptionD, rbJudgeTrue, rbJudgeFalse;
    private CheckBox cbOptionA, cbOptionB, cbOptionC, cbOptionD;
    private TextView tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private LinearLayout llOptionA, llOptionB, llOptionC, llOptionD, llJudgeTrue, llJudgeFalse;
    private LinearLayout llCountdownContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_exam2);

        try {
            // 获取考试配置
            examConfig = (ExamConfig) getIntent().getSerializableExtra("EXAM_CONFIG");
            if (examConfig == null) {
                examConfig = new ExamConfig(); // 使用默认配置
            }

            // 初始化视图组件
            initViews();

            // 初始化题库管理器
            questionManager = new QuestionManager();

            // 获取从题库选择界面传递过来的题库ID
            String bankId = getIntent().getStringExtra("QUESTION_BANK_ID");
            String bankTitle = getIntent().getStringExtra("QUESTION_BANK_TITLE");

            // 加载对应题库的题目
            questionManager.loadQuestions(this, bankId);



            // 如果有题库标题，设置为ActionBar标题
            if (bankTitle != null && getSupportActionBar() != null) {
                getSupportActionBar().setTitle(bankTitle);
            }

            // 设置所有按钮的点击监听器
            setupClickListeners();

            // 显示第一道题目
            showCurrentQuestion();

            // 启动倒计时
            startCountdown();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "模拟考试启动失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        // 倒计时组件
        llCountdownContainer = findViewById(R.id.llCountdownContainer);
        tvCountdown = findViewById(R.id.tvCountdown);

        // 题目相关组件
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
        btnSubmitAnswer = findViewById(R.id.btnSubmitAnswer);
        btnAnswerCard = findViewById(R.id.btnAnswerCard);
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
                Toast.makeText(SimulationExamActivity2.this, R.string.already_first_question, Toast.LENGTH_SHORT).show();
            }
        });

        // “下一题”按钮
        btnNext.setOnClickListener(v -> {
            if (questionManager.nextQuestion()) {
                showCurrentQuestion();
            } else {
                Toast.makeText(SimulationExamActivity2.this, R.string.already_last_question, Toast.LENGTH_SHORT).show();
            }
        });

        // 答题卡按钮
        btnAnswerCard.setOnClickListener(v -> showAnswerCard());
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
                
                // 恢复之前的答案
                String selectedAnswer = currentQ.getSelectedAnswer();
                if (selectedAnswer != null) {
                    if ("A".equals(selectedAnswer)) {
                        rbJudgeTrue.setChecked(true);
                        rbJudgeFalse.setChecked(false);
                    } else if ("B".equals(selectedAnswer)) {
                        rbJudgeFalse.setChecked(true);
                        rbJudgeTrue.setChecked(false);
                    }
                }
            } else {
                // 单选题或多选题
                llOptions.setVisibility(View.VISIBLE);
                llJudgeOptions.setVisibility(View.GONE);

                // 设置选项文本
                tvOptionA.setText("A. " + (currentQ.getOptionA() != null ? currentQ.getOptionA() : ""));
                tvOptionB.setText("B. " + (currentQ.getOptionB() != null ? currentQ.getOptionB() : ""));
                tvOptionC.setText("C. " + (currentQ.getOptionC() != null ? currentQ.getOptionC() : ""));
                tvOptionD.setText("D. " + (currentQ.getOptionD() != null ? currentQ.getOptionD() : ""));

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
                    
                    // 恢复之前的答案
                    String selectedAnswer = currentQ.getSelectedAnswer();
                    if (selectedAnswer != null) {
                        switch (selectedAnswer) {
                            case "A":
                                rbOptionA.setChecked(true);
                                break;
                            case "B":
                                rbOptionB.setChecked(true);
                                break;
                            case "C":
                                rbOptionC.setChecked(true);
                                break;
                            case "D":
                                rbOptionD.setChecked(true);
                                break;
                        }
                    }
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
                    
                    // 恢复之前的答案
                    String selectedAnswer = currentQ.getSelectedAnswer();
                    if (selectedAnswer != null) {
                        String[] answers = selectedAnswer.split(",");
                        for (String answer : answers) {
                            switch (answer.trim()) {
                                case "A":
                                    cbOptionA.setChecked(true);
                                    break;
                                case "B":
                                    cbOptionB.setChecked(true);
                                    break;
                                case "C":
                                    cbOptionC.setChecked(true);
                                    break;
                                case "D":
                                    cbOptionD.setChecked(true);
                                    break;
                            }
                        }
                    }
                }
            }
        } else {
            // 如果没有题目，显示提示信息
            tvQuestionNumber.setText("第0题/共0题");
            tvQuestion.setText("当前题库中没有题目，请联系管理员添加题目");
            tvQuestionType.setText("单选题");
            tvResult.setText("请选择答案");
            
            // 隐藏选项区域
            llOptions.setVisibility(View.GONE);
            llJudgeOptions.setVisibility(View.GONE);
            
            // 隐藏提交答案按钮
            btnSubmitAnswer.setVisibility(View.GONE);
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
            Toast.makeText(this, R.string.please_select_answer_before_submit, Toast.LENGTH_SHORT).show();
            return;
        }
        
        // 保存用户选择的答案到Question对象
        currentQ.setSelectedAnswer(selectedAnswer);

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

    // 检查答案
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

    // 启动倒计时
    private void startCountdown() {
        // 将考试时长转换为毫秒
        timeLeftInMillis = examConfig.getExamTime() * 60 * 1000;

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountdownText();
                endExam();
            }
        }.start();
    }

    // 更新倒计时显示
    private void updateCountdownText() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        tvCountdown.setText(timeFormatted);

        // 时间不足5分钟时，显示视觉提醒
        if ((timeLeftInMillis / 1000) < 300) {
            tvCountdown.setTextColor(getResources().getColor(R.color.error_color));
            llCountdownContainer.setBackgroundColor(getResources().getColor(R.color.error_light));
        } else {
            tvCountdown.setTextColor(getResources().getColor(R.color.primary_color));
            llCountdownContainer.setBackgroundColor(getResources().getColor(R.color.primary_light));
        }
    }

    // 结束考试
    private void endExam() {
        isExamEnded = true;
        
        // 显示考试结束对话框
        new AlertDialog.Builder(this)
                .setTitle("考试结束")
                .setMessage("考试时间已结束，是否提交试卷？")
                .setPositiveButton("确认提交", (dialog, which) -> {
                    submitExam();
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    // 继续考试
                    isExamEnded = false;
                    // 重新启动倒计时（额外5分钟）
                    timeLeftInMillis = 5 * 60 * 1000;
                    startCountdown();
                })
                .setCancelable(false)
                .show();
    }

    // 提交考试
    private void submitExam() {
        // 停止倒计时
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // 重新计算所有题目的答题结果，确保数据准确性
        questionManager.recheckAllAnswers();
        
        // 计算成绩
        int totalQuestions = questionManager.getTotalQuestions();
        int correctCount = totalQuestions - questionManager.getWrongQuestions().size();
        // 每道题1分，满分根据实际题数计算
        double score = (double) correctCount * 1.0;
        
        // 计算用时（考试总时间 - 剩余时间）
        long totalExamTime = examConfig.getExamTime() * 60 * 1000;
        long usedTime = totalExamTime - timeLeftInMillis;
        
        // 计算是否通过
        boolean passed = score >= examConfig.getPassingScore();
        
        // 保存练习记录
        PracticeRecord record = new PracticeRecord();
        record.setQuestionBankTitle(getIntent().getStringExtra("QUESTION_BANK_TITLE"));
        record.setTotalQuestions(totalQuestions);
        record.setCorrectQuestions(correctCount);
        record.setScore((int) Math.round(score));
        record.setUsedTimeMillis(usedTime);
        record.setPassed(passed);
        
        PracticeRecordManager recordManager = new PracticeRecordManager(this);
        recordManager.savePracticeRecord(record);
        
        // 将错题同步到错题模块
        saveWrongQuestionsToAnswerRecord();

        // 显示成绩
        new AlertDialog.Builder(this)
                .setTitle("考试成绩")
                .setMessage("总题数：" + totalQuestions + "题\n" +
                        "答对：" + correctCount + "题\n" +
                        "答错：" + questionManager.getWrongQuestions().size() + "题\n" +
                        "得分：" + String.format("%.1f", score) + "分\n" +
                        "用时：" + String.format("%02d:%02d", usedTime / 60000, (usedTime % 60000) / 1000) + "\n" +
                        "结果：" + (passed ? "通过" : "未通过"))
                .setPositiveButton("查看详细结果", (dialog, which) -> {
                    // 跳转到成绩详情页
                    showExamResult();
                })
                .setNegativeButton("返回", (dialog, which) -> {
                    finish();
                })
                .setCancelable(false)
                .show();
    }
    
    /**
     * 将错题同步到答题记录中
     */
    private void saveWrongQuestionsToAnswerRecord() {
        AnswerRecordManager answerRecordManager = new AnswerRecordManager(this);
        String bankId = getIntent().getStringExtra("QUESTION_BANK_ID");
        String bankTitle = getIntent().getStringExtra("QUESTION_BANK_TITLE");
        
        // 获取所有错题
        List<Question> wrongQuestions = questionManager.getWrongQuestions();
        
        // 遍历所有错题，创建并保存答题记录
        for (Question question : wrongQuestions) {
            AnswerRecord answerRecord = new AnswerRecord();
            
            // 设置答题记录信息
            answerRecord.setQuestionId(question.getId());
            answerRecord.setQuestionText(question.getText());
            // 这里假设selectedAnswer在Question对象中已保存，实际需要根据实现调整
            answerRecord.setSelectedAnswer(question.getSelectedAnswer());
            answerRecord.setCorrectAnswer(question.getAnswer());
            answerRecord.setCorrect(false); // 错题
            answerRecord.setExplanation(question.getExplanation());
            answerRecord.setDifficulty(question.getDifficulty());
            answerRecord.setQuestionBankId(bankId);
            answerRecord.setQuestionBankTitle(bankTitle);
            // 设置选项信息
            answerRecord.setOptionA(question.getOptionA());
            answerRecord.setOptionB(question.getOptionB());
            answerRecord.setOptionC(question.getOptionC());
            answerRecord.setOptionD(question.getOptionD());
            answerRecord.setOptionE(null); // 目前题目只支持A-D选项
            
            // 保存答题记录
            answerRecordManager.saveAnswerRecord(answerRecord);
        }
    }

    // 显示考试结果
    private void showExamResult() {
        // 这里可以跳转到成绩详情页
        Toast.makeText(this, R.string.score_detail_page_under_development, Toast.LENGTH_SHORT).show();
        finish();
    }

    // 显示答题卡
    private void showAnswerCard() {
        // 创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("答题卡");
        
        // 创建答题卡内容
        LinearLayout answerCardLayout = new LinearLayout(this);
        answerCardLayout.setOrientation(LinearLayout.VERTICAL);
        answerCardLayout.setPadding(20, 20, 20, 20);
        answerCardLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
        
        // 获取题目列表
        List<Question> questions = questionManager.getQuestions();
        if (questions == null || questions.isEmpty()) {
            Toast.makeText(this, R.string.no_questions, Toast.LENGTH_SHORT).show();
            return;
        }
        
        // 统计各题型数量
        int singleCount = 0;
        int multipleCount = 0;
        int judgeCount = 0;
        for (Question question : questions) {
            if (Question.TYPE_SINGLE.equals(question.getType())) {
                singleCount++;
            } else if (Question.TYPE_MULTIPLE.equals(question.getType())) {
                multipleCount++;
            } else if (Question.TYPE_JUDGE.equals(question.getType())) {
                judgeCount++;
            }
        }
        
        // 添加题型统计
        if (singleCount > 0) {
            addQuestionTypeSummary(answerCardLayout, "单选题", questions);
        }
        if (multipleCount > 0) {
            addQuestionTypeSummary(answerCardLayout, "多选题", questions);
        }
        if (judgeCount > 0) {
            addQuestionTypeSummary(answerCardLayout, "判断题", questions);
        }
        
        // 添加题目序号按钮网格
        addQuestionNumberGrid(answerCardLayout, questions);
        
        // 设置对话框内容
        builder.setView(answerCardLayout);
        builder.setPositiveButton("关闭", null);
        builder.setNegativeButton("提前交卷", (dialog, which) -> {
            // 确认提前交卷
            new AlertDialog.Builder(SimulationExamActivity2.this)
                    .setTitle("提前交卷")
                    .setMessage("确定要提前交卷吗？交卷后将无法继续答题。")
                    .setPositiveButton("确定", (dialog1, which1) -> {
                        // 提交考试
                        submitExam();
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });
        
        // 显示对话框
        answerCardDialog = builder.create();
        answerCardDialog.show();
        
        // 设置对话框宽度为屏幕宽度的90%
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        answerCardDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
    
    // 添加题型统计
    private void addQuestionTypeSummary(LinearLayout layout, String typeName, List<Question> questions) {
        // 统计该题型的已答题数量和总题数
        int total = 0;
        int answered = 0;
        for (Question question : questions) {
            if ((typeName.equals("单选题") && Question.TYPE_SINGLE.equals(question.getType())) ||
                (typeName.equals("多选题") && Question.TYPE_MULTIPLE.equals(question.getType())) ||
                (typeName.equals("判断题") && Question.TYPE_JUDGE.equals(question.getType()))) {
                total++;
                if (question.getSelectedAnswer() != null && !question.getSelectedAnswer().isEmpty()) {
                    answered++;
                }
            }
        }
        
        TextView summaryTv = new TextView(this);
        summaryTv.setText(typeName + "：" + answered + "/" + total);
        summaryTv.setTextSize(14);
        summaryTv.setTextColor(getResources().getColor(android.R.color.black));
        summaryTv.setTypeface(null, Typeface.BOLD);
        summaryTv.setPadding(0, 10, 0, 10);
        layout.addView(summaryTv);
    }
    
    // 添加题目序号网格
    private void addQuestionNumberGrid(LinearLayout layout, List<Question> questions) {
        LinearLayout gridLayout = new LinearLayout(this);
        gridLayout.setOrientation(LinearLayout.HORIZONTAL);
        gridLayout.setWeightSum(5); // 每行显示5个按钮
        
        int currentLineLayout = -1;
        
        for (int i = 0; i < questions.size(); i++) {
            // 每5个按钮换一行
            if (i % 5 == 0) {
                gridLayout = new LinearLayout(this);
                gridLayout.setOrientation(LinearLayout.HORIZONTAL);
                gridLayout.setWeightSum(5);
                gridLayout.setPadding(0, 5, 0, 5);
                layout.addView(gridLayout);
            }
            
            // 创建题目序号按钮
            Button questionBtn = new Button(this);
            questionBtn.setText(String.valueOf(i + 1));
            questionBtn.setTextSize(12);
            questionBtn.setMinWidth(0);
            questionBtn.setMinimumWidth(0);
            questionBtn.setWidth(60);
            questionBtn.setHeight(60);
            questionBtn.setPadding(5, 5, 5, 5);
            questionBtn.setGravity(Gravity.CENTER);
            
            // 设置按钮样式
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1);
            params.setMargins(5, 5, 5, 5);
            questionBtn.setLayoutParams(params);
            
            // 设置按钮背景色
            Question question = questions.get(i);
            if (question.getSelectedAnswer() != null && !question.getSelectedAnswer().isEmpty()) {
                // 已答题，设置绿色背景
                questionBtn.setBackgroundColor(getResources().getColor(R.color.success_light));
                questionBtn.setTextColor(getResources().getColor(android.R.color.black));
            } else {
                // 未答题，设置默认背景色
                questionBtn.setBackgroundColor(getResources().getColor(R.color.primary_light));
                questionBtn.setTextColor(getResources().getColor(android.R.color.black));
            }
            
            // 设置点击事件
            final int questionIndex = i;
            questionBtn.setOnClickListener(v -> {
                // 跳转到对应题目
                questionManager.goToQuestion(questionIndex);
                showCurrentQuestion();
                // 关闭对话框
                if (answerCardDialog != null && answerCardDialog.isShowing()) {
                    answerCardDialog.dismiss();
                }
            });
            
            // 添加到网格
            gridLayout.addView(questionBtn);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}