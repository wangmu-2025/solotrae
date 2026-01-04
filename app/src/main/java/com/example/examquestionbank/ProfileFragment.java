package com.example.examquestionbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private Button btnLogin;
    private LinearLayout llPracticeRecords;
    private LinearLayout llWrongQuestions;
    private LinearLayout llSettings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        // 初始化视图
        btnLogin = view.findViewById(R.id.btnLogin);
        llPracticeRecords = view.findViewById(R.id.llPracticeRecords);
        llWrongQuestions = view.findViewById(R.id.llWrongQuestions);
        llSettings = view.findViewById(R.id.llSettings);

        // 设置点击事件
        btnLogin.setOnClickListener(v -> {
            // 跳转到登录界面
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        llPracticeRecords.setOnClickListener(v -> {
            // 跳转到练习记录界面
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), PracticeRecordsActivity.class);
                startActivity(intent);
            }
        });

        llWrongQuestions.setOnClickListener(v -> {
            // 跳转到错题汇总界面
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), WrongQuestionsActivity.class);
                startActivity(intent);
            }
        });

        llSettings.setOnClickListener(v -> {
            // 跳转到设置界面
            // Intent intent = new Intent(getActivity(), SettingsActivity.class);
            // startActivity(intent);
        });
        
        return view;
    }
}