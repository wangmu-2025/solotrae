package com.example.examquestionbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PracticeFragment extends Fragment {

    private Button btnSequentialPractice;
    private Button btnRandomPractice;
    private Button btnSimulationExam;
    private Button btnSpecialPractice;
    private Button btnWrongQuestions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice, container, false);

        // 初始化视图
        initViews(view);
        // 设置各种答题模式的点击事件
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        btnSequentialPractice = view.findViewById(R.id.btn_sequential_practice);
        btnRandomPractice = view.findViewById(R.id.btn_random_practice);
        btnSimulationExam = view.findViewById(R.id.btn_simulation_exam);
        btnSpecialPractice = view.findViewById(R.id.btn_special_practice);
        btnWrongQuestions = view.findViewById(R.id.btn_wrong_questions);
    }

    private void setupClickListeners() {
        // 顺序练习
        btnSequentialPractice.setOnClickListener(v -> startPractice("sequential"));

        // 随机练习
        btnRandomPractice.setOnClickListener(v -> startPractice("random"));

        // 模拟考试
        btnSimulationExam.setOnClickListener(v -> startPractice("simulation"));

        // 专项练习
        btnSpecialPractice.setOnClickListener(v -> startPractice("special"));

        // 错题本练习
        btnWrongQuestions.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WrongQuestionsActivity.class);
            startActivity(intent);
        });
    }

    private void startPractice(String mode) {
        // 跳转到题库选择界面，让用户先选择题库，然后再跳转到答题界面
        Intent intent = new Intent(getActivity(), QuestionBankSelectActivity.class);
        intent.putExtra("PRACTICE_MODE", mode);
        startActivity(intent);
    }
}