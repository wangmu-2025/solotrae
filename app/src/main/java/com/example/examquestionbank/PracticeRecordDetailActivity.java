package com.example.examquestionbank;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 练习记录详情页面，显示单条练习记录的详细信息
 */
public class PracticeRecordDetailActivity extends AppCompatActivity {

    private TextView tvExamTime;
    private TextView tvQuestionBankTitle;
    private TextView tvScore;
    private TextView tvTotalQuestions;
    private TextView tvCorrectQuestions;
    private TextView tvWrongQuestions;
    private TextView tvCorrectRate;
    private TextView tvUsedTime;
    private TextView tvResult;

    private PracticeRecord record;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_record_detail);

        // 初始化日期格式化器
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // 初始化视图组件
        initViews();

        // 获取传递的练习记录
        record = (PracticeRecord) getIntent().getSerializableExtra("PRACTICE_RECORD");

        // 显示记录详情
        if (record != null) {
            showRecordDetail();
        }
    }

    private void initViews() {
        tvExamTime = findViewById(R.id.tvExamTime);
        tvQuestionBankTitle = findViewById(R.id.tvQuestionBankTitle);
        tvScore = findViewById(R.id.tvScore);
        tvTotalQuestions = findViewById(R.id.tvTotalQuestions);
        tvCorrectQuestions = findViewById(R.id.tvCorrectQuestions);
        tvWrongQuestions = findViewById(R.id.tvWrongQuestions);
        tvCorrectRate = findViewById(R.id.tvCorrectRate);
        tvUsedTime = findViewById(R.id.tvUsedTime);
        tvResult = findViewById(R.id.tvResult);

        // 设置标题
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("考试详情");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void showRecordDetail() {
        // 设置考试时间
        tvExamTime.setText(dateFormat.format(record.getExamTime()));

        // 设置题库标题
        tvQuestionBankTitle.setText(record.getQuestionBankTitle());

        // 设置得分
        tvScore.setText(String.valueOf(record.getScore()));

        // 设置总题数
        tvTotalQuestions.setText(String.valueOf(record.getTotalQuestions()));

        // 设置正确题数
        tvCorrectQuestions.setText(String.valueOf(record.getCorrectQuestions()));

        // 计算并设置错误题数
        int wrongQuestions = record.getTotalQuestions() - record.getCorrectQuestions();
        tvWrongQuestions.setText(String.valueOf(wrongQuestions));

        // 计算并设置正确率
        double correctRate = (double) record.getCorrectQuestions() / record.getTotalQuestions() * 100;
        tvCorrectRate.setText(String.format(Locale.getDefault(), "%.1f%%", correctRate));

        // 设置用时
        tvUsedTime.setText(record.getUsedTimeString());

        // 设置考试结果
        if (record.isPassed()) {
            tvResult.setText("通过");
            tvResult.setTextColor(getResources().getColor(R.color.success_color));
        } else {
            tvResult.setText("未通过");
            tvResult.setTextColor(getResources().getColor(R.color.error_color));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // 处理返回按钮点击事件
        finish();
        return true;
    }
}