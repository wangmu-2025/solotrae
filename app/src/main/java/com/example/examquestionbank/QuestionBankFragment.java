package com.example.examquestionbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class QuestionBankFragment extends Fragment {

    private RecyclerView rvQuestionBanks;
    private QuestionBankAdapter adapter;
    private List<QuestionBank> questionBanks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_bank, container, false);
        
        // 初始化视图
        rvQuestionBanks = view.findViewById(R.id.rvQuestionBanks);

        // 初始化题库数据
        initQuestionBanks();

        // 设置适配器
        adapter = new QuestionBankAdapter(questionBanks, this::onQuestionBankClick);
        rvQuestionBanks.setAdapter(adapter);
        
        return view;
    }

    // 初始化题库数据
    private void initQuestionBanks() {
        questionBanks = new ArrayList<>();

        // 创建默认题库（历史题库）
        QuestionManager questionManager = new QuestionManager();
        // 使用requireContext()确保上下文可用
        questionManager.loadQuestions(requireContext());

        QuestionBank historyBank = new QuestionBank(
                "history",
                "历史题库",
                "包含中国历史和世界历史的经典题目",
                "历史",
                questionManager.getQuestionList()
        );

        questionBanks.add(historyBank);

        // 可以添加更多题库
        // ...
    }

    // 题库点击事件处理
    private void onQuestionBankClick(QuestionBank questionBank) {
        // 跳转到答题界面，传递选中的题库
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("QUESTION_BANK_ID", questionBank.getId());
            intent.putExtra("QUESTION_BANK_TITLE", questionBank.getTitle());
            startActivity(intent);
        }
    }
}