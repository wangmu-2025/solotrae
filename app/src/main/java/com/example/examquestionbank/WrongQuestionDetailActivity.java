package com.example.examquestionbank;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * 错题详情页面，显示单个题库的错题列表
 */
public class WrongQuestionDetailActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private LinearLayout llWrongQuestionsList;
    private TextView tvEmptyState;
    private TextView tvBankTitle;
    private TextView tvBankWrongCount;
    private AnswerRecordManager answerRecordManager;
    private String questionBankId;
    private String questionBankTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_question_detail);

        // 初始化视图
        initViews();

        // 获取传递的参数
        questionBankId = getIntent().getStringExtra("QUESTION_BANK_ID");
        questionBankTitle = getIntent().getStringExtra("QUESTION_BANK_TITLE");

        // 设置toolbar标题
        String title = (questionBankTitle != null ? questionBankTitle : "未知题库") + " - 错题";
        toolbar.setTitle(title);

        // 初始化答题记录管理器
        answerRecordManager = new AnswerRecordManager(this);

        // 加载当前题库的错题
        loadWrongQuestions();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        llWrongQuestionsList = findViewById(R.id.llWrongQuestionsList);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        tvBankTitle = findViewById(R.id.tvBankTitle);
        tvBankWrongCount = findViewById(R.id.tvBankWrongCount);

        // 设置返回按钮点击事件
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * 加载当前题库的错题
     */
    private void loadWrongQuestions() {
        // 获取当前题库的所有错题
        List<AnswerRecord> wrongRecords = answerRecordManager.loadWrongAnswerRecordsByBankId(questionBankId);

        // 统计每道题的错误次数
        Map<String, Integer> wrongCountMap = new HashMap<>();
        Map<String, AnswerRecord> uniqueRecords = new HashMap<>();
        for (AnswerRecord record : wrongRecords) {
            String questionId = record.getQuestionId();
            if (uniqueRecords.containsKey(questionId)) {
                // 已存在，增加错误次数
                wrongCountMap.put(questionId, wrongCountMap.get(questionId) + 1);
            } else {
                // 第一次出现，添加到map
                uniqueRecords.put(questionId, record);
                wrongCountMap.put(questionId, 1);
            }
        }

        // 更新标题和错题数量
        tvBankTitle.setText(questionBankTitle != null ? questionBankTitle : "未知题库");
        tvBankWrongCount.setText("共 " + uniqueRecords.size() + " 道错题");

        // 清空列表
        llWrongQuestionsList.removeAllViews();

        if (uniqueRecords.isEmpty()) {
            // 没有错题，显示空状态
            tvEmptyState.setVisibility(View.VISIBLE);
            llWrongQuestionsList.setVisibility(View.GONE);
        } else {
            // 有错题，显示列表
            tvEmptyState.setVisibility(View.GONE);
            llWrongQuestionsList.setVisibility(View.VISIBLE);

            // 遍历所有唯一的错题记录
            for (Map.Entry<String, AnswerRecord> entry : uniqueRecords.entrySet()) {
                String questionId = entry.getKey();
                AnswerRecord record = entry.getValue();
                int wrongCount = wrongCountMap.get(questionId);

                // 创建错题项视图
                View wrongItemView = createWrongQuestionItem(record, wrongCount);
                llWrongQuestionsList.addView(wrongItemView);
            }
        }
    }

    /**
     * 创建错题项视图
     */
    private View createWrongQuestionItem(AnswerRecord record, int wrongCount) {
        // 加载错题项布局
        View view = LayoutInflater.from(this).inflate(R.layout.item_wrong_question, llWrongQuestionsList, false);

        // 绑定数据
        TextView tvQuestionText = view.findViewById(R.id.tvQuestionText);
        TextView tvQuestionOptions = view.findViewById(R.id.tvQuestionOptions);
        TextView tvCorrectAnswer = view.findViewById(R.id.tvCorrectAnswer);
        TextView tvUserAnswer = view.findViewById(R.id.tvUserAnswer);
        TextView tvWrongCount = view.findViewById(R.id.tvWrongCount);
        TextView tvExplanation = view.findViewById(R.id.tvExplanation);
        View btnMasterQuestion = view.findViewById(R.id.btnMasterQuestion);

        // 设置数据
        tvQuestionText.setText(record.getQuestionText() != null ? record.getQuestionText() : "");
        tvQuestionOptions.setText(formatOptions(record));
        tvCorrectAnswer.setText(record.getCorrectAnswer() != null ? record.getCorrectAnswer() : "");
        tvUserAnswer.setText(record.getSelectedAnswer() != null ? record.getSelectedAnswer() : "");
        tvWrongCount.setText(wrongCount + "次");
        tvExplanation.setText(record.getExplanation() != null ? record.getExplanation() : "");

        // 设置斩题按钮点击事件
        btnMasterQuestion.setOnClickListener(v -> {
            // 显示确认对话框
            new AlertDialog.Builder(this)
                    .setTitle("斩题确认")
                    .setMessage("确定要斩题吗？斩题后将从错题列表中移除该题，表明你已掌握。")
                    .setPositiveButton("确定", (dialog, which) -> {
                        // 执行斩题操作
                        masterQuestion(record);
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });

        return view;
    }

    /**
     * 格式化选项显示
     */
    private String formatOptions(AnswerRecord record) {
        StringBuilder options = new StringBuilder();
        
        // 按顺序添加选项A到E，只显示非空的选项
        if (record.getOptionA() != null && !record.getOptionA().isEmpty()) {
            options.append("A. ").append(record.getOptionA()).append("\n");
        }
        if (record.getOptionB() != null && !record.getOptionB().isEmpty()) {
            options.append("B. ").append(record.getOptionB()).append("\n");
        }
        if (record.getOptionC() != null && !record.getOptionC().isEmpty()) {
            options.append("C. ").append(record.getOptionC()).append("\n");
        }
        if (record.getOptionD() != null && !record.getOptionD().isEmpty()) {
            options.append("D. ").append(record.getOptionD()).append("\n");
        }
        if (record.getOptionE() != null && !record.getOptionE().isEmpty()) {
            options.append("E. ").append(record.getOptionE());
        }
        
        // 移除末尾的换行符
        String optionsStr = options.toString();
        if (optionsStr.endsWith("\n")) {
            optionsStr = optionsStr.substring(0, optionsStr.length() - 1);
        }
        
        return optionsStr;
    }

    /**
     * 斩题操作，将错题标记为已掌握
     */
    private void masterQuestion(AnswerRecord record) {
        // 标记该题所有记录为已掌握
        answerRecordManager.markQuestionAsMastered(record.getQuestionId());
        Toast.makeText(this, "斩题成功！", Toast.LENGTH_SHORT).show();
        // 重新加载错题列表
        loadWrongQuestions();
    }
}