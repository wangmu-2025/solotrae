package com.example.examquestionbank;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StatisticsFragment extends Fragment {

    private TextView tvTotalQuestions;
    private TextView tvCorrectAnswers;
    private TextView tvWrongAnswers;
    private TextView tvCorrectRate;
    private TextView tvStudyTime;
    private TextView tvHistoryProgress;
    private ProgressBar pbHistory;
    private TextView tvMathProgress;
    private ProgressBar pbMath;
    private TextView tvEnglishProgress;
    private ProgressBar pbEnglish;
    private TextView tvScienceProgress;
    private ProgressBar pbScience;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // 初始化视图
        initViews(view);
        // 加载统计数据
        loadStatisticsData();

        return view;
    }

    private void initViews(View view) {
        tvTotalQuestions = view.findViewById(R.id.tv_total_questions);
        tvCorrectAnswers = view.findViewById(R.id.tv_correct_answers);
        tvWrongAnswers = view.findViewById(R.id.tv_wrong_answers);
        tvCorrectRate = view.findViewById(R.id.tv_correct_rate);
        tvStudyTime = view.findViewById(R.id.tv_study_time);
        tvHistoryProgress = view.findViewById(R.id.tv_history_progress);
        pbHistory = view.findViewById(R.id.pb_history);
        tvMathProgress = view.findViewById(R.id.tv_math_progress);
        pbMath = view.findViewById(R.id.pb_math);
        tvEnglishProgress = view.findViewById(R.id.tv_english_progress);
        pbEnglish = view.findViewById(R.id.pb_english);
        tvScienceProgress = view.findViewById(R.id.tv_science_progress);
        pbScience = view.findViewById(R.id.pb_science);
    }

    private void loadStatisticsData() {
        // 这里应该从数据库或文件中加载实际的统计数据
        // 目前使用模拟数据
        
        // 总答题数
        tvTotalQuestions.setText("120");
        
        // 正确数
        tvCorrectAnswers.setText("95");
        
        // 错误数
        tvWrongAnswers.setText("25");
        
        // 正确率
        tvCorrectRate.setText("79%");
        
        // 学习时长
        tvStudyTime.setText("12小时30分钟");
        
        // 各题库完成进度（模拟数据）
        tvHistoryProgress.setText("85%");
        pbHistory.setProgress(85);
        
        tvMathProgress.setText("60%");
        pbMath.setProgress(60);
        
        tvEnglishProgress.setText("90%");
        pbEnglish.setProgress(90);
        
        tvScienceProgress.setText("50%");
        pbScience.setProgress(50);
    }
}