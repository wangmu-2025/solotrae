package com.example.examquestionbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WrongQuestionsActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvTotalWrongQuestions;
    private LinearLayout llQuestionBankList;
    private LinearLayout llEmptyState;
    private AnswerRecordManager answerRecordManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_questions);

        // 初始化视图
        initViews();

        // 初始化答题记录管理器
        answerRecordManager = new AnswerRecordManager(this);

        // 加载所有错题
        loadWrongQuestions();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvTotalWrongQuestions = findViewById(R.id.tvTotalWrongQuestions);
        llQuestionBankList = findViewById(R.id.llQuestionBankList);
        llEmptyState = findViewById(R.id.llEmptyState);

        // 设置返回按钮点击事件
        toolbar.setNavigationOnClickListener(v -> finish());

        // 设置标题
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("我的错题");
        }
    }

    /**
     * 加载所有错题
     */
    private void loadWrongQuestions() {
        // 获取所有错题记录
        List<AnswerRecord> wrongRecords = answerRecordManager.loadWrongAnswerRecords();

        // 按题库分组统计错题
        Map<String, QuestionBankWrongInfo> bankWrongInfoMap = new HashMap<>();
        
        // 遍历所有错题记录
        for (AnswerRecord record : wrongRecords) {
            String bankId = record.getQuestionBankId();
            String bankTitle = record.getQuestionBankTitle();
            String questionId = record.getQuestionId();

            // 确保bankId和bankTitle不为null，否则使用默认值
            if (bankId == null) {
                bankId = "unknown_bank";
            }
            if (bankTitle == null) {
                bankTitle = "未知题库";
            }

            // 获取当前题库的错题统计信息
            QuestionBankWrongInfo info = bankWrongInfoMap.get(bankId);
            if (info == null) {
                info = new QuestionBankWrongInfo(bankId, bankTitle);
                bankWrongInfoMap.put(bankId, info);
            }

            // 只统计唯一的错题
            if (!info.questionIds.contains(questionId)) {
                info.questionIds.add(questionId);
                info.wrongCount++;
            }
        }

        // 更新总错题数量
        int totalWrongQuestions = 0;
        for (QuestionBankWrongInfo info : bankWrongInfoMap.values()) {
            totalWrongQuestions += info.wrongCount;
        }
        tvTotalWrongQuestions.setText("共 " + totalWrongQuestions + " 道错题");

        // 清空列表
        llQuestionBankList.removeAllViews();

        if (bankWrongInfoMap.isEmpty()) {
            // 没有错题，显示空状态
            llEmptyState.setVisibility(View.VISIBLE);
            llQuestionBankList.setVisibility(View.GONE);
        } else {
            // 有错题，显示题库分组列表
            llEmptyState.setVisibility(View.GONE);
            llQuestionBankList.setVisibility(View.VISIBLE);

            // 遍历每个题库，创建分组项
            for (QuestionBankWrongInfo info : bankWrongInfoMap.values()) {
                View bankGroupItem = createBankGroupItem(info);
                llQuestionBankList.addView(bankGroupItem);
            }
        }
    }

    /**
     * 创建题库分组项视图
     */
    private View createBankGroupItem(QuestionBankWrongInfo info) {
        // 加载题库分组项布局
        View view = LayoutInflater.from(this).inflate(R.layout.item_question_bank_group, llQuestionBankList, false);

        // 绑定数据
        TextView tvBankTitle = view.findViewById(R.id.tvBankTitle);
        TextView tvBankWrongCount = view.findViewById(R.id.tvBankWrongCount);
        View btnViewWrongQuestions = view.findViewById(R.id.btnViewWrongQuestions);

        // 设置数据
        tvBankTitle.setText(info.bankTitle);
        tvBankWrongCount.setText("错题：" + info.wrongCount + "道");

        // 设置查看错题按钮点击事件
        btnViewWrongQuestions.setOnClickListener(v -> {
            // 跳转到错题详情页面
            Intent intent = new Intent(WrongQuestionsActivity.this, WrongQuestionDetailActivity.class);
            intent.putExtra("QUESTION_BANK_ID", info.bankId);
            intent.putExtra("QUESTION_BANK_TITLE", info.bankTitle);
            startActivity(intent);
        });

        return view;
    }

    /**
     * 题库错题统计信息
     */
    private static class QuestionBankWrongInfo {
        String bankId;
        String bankTitle;
        int wrongCount;
        java.util.ArrayList<String> questionIds;

        QuestionBankWrongInfo(String bankId, String bankTitle) {
            this.bankId = bankId;
            this.bankTitle = bankTitle;
            this.wrongCount = 0;
            this.questionIds = new java.util.ArrayList<>();
        }
    }
}