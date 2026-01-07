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

        // 使用ApiService从后端获取题库列表
        Thread thread = new Thread(() -> {
            ApiService apiService = new ApiService();
            List<QuestionBank> result = apiService.getQuestionBanks();
            
            // 在主线程更新UI
            requireActivity().runOnUiThread(() -> {
                if (result != null && !result.isEmpty()) {
                    questionBanks.clear();
                    questionBanks.addAll(result);
                    adapter.notifyDataSetChanged();
                } else {
                    // 如果API获取失败，使用默认题库
                    QuestionManager questionManager = new QuestionManager();
                    questionManager.loadQuestions(requireContext());

                    QuestionBank historyBank = new QuestionBank(
                            "history",
                            "历史题库",
                            "包含中国历史和世界历史的经典题目",
                            "历史",
                            questionManager.getQuestionList()
                    );

                    questionBanks.add(historyBank);
                    adapter.notifyDataSetChanged();
                }
            });
        });
        thread.start();
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