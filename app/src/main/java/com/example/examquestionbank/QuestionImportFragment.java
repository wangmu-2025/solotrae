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

public class QuestionImportFragment extends Fragment {

    private Button btnManualImport;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_import, container, false);
        
        // 初始化视图
        btnManualImport = view.findViewById(R.id.btnManualImport);

        // 设置点击事件
        btnManualImport.setOnClickListener(v -> {
            // 跳转到手动导入题目界面
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), ManualImportActivity.class);
                startActivity(intent);
            }
        });
        
        return view;
    }
}