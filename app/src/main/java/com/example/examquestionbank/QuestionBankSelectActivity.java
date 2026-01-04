package com.example.examquestionbank;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class QuestionBankSelectActivity extends AppCompatActivity {

    private RecyclerView rvQuestionBanks;
    private QuestionBankAdapter adapter;
    private List<QuestionBank> questionBanks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_bank_select);

        // 初始化视图
        rvQuestionBanks = findViewById(R.id.rvQuestionBanks);

        // 初始化题库数据
        initQuestionBanks();

        // 设置适配器
        adapter = new QuestionBankAdapter(questionBanks, this::onQuestionBankClick);
        rvQuestionBanks.setAdapter(adapter);
    }

    // 初始化题库数据
    private void initQuestionBanks() {
        questionBanks = new ArrayList<>();

        // 创建默认题库（历史题库）
        QuestionManager questionManager = new QuestionManager();
        questionManager.loadQuestions(this);

        QuestionBank historyBank = new QuestionBank(
                "history",
                "历史题库",
                "包含中国历史和世界历史的经典题目",
                "历史",
                questionManager.getQuestionList() // 假设QuestionManager有getQuestionList方法
        );

        questionBanks.add(historyBank);

        // 可以添加更多题库
        // ...
    }

    // 题库点击事件处理
    private void onQuestionBankClick(QuestionBank questionBank) {
        // 跳转到练习模式选择界面，传递选中的题库
        Intent intent = new Intent(this, PracticeModeSelectActivity.class);
        intent.putExtra("QUESTION_BANK_ID", questionBank.getId());
        intent.putExtra("QUESTION_BANK_TITLE", questionBank.getTitle());
        startActivity(intent);
    }
}