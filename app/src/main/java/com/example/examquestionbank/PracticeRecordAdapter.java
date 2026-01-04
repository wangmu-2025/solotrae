package com.example.examquestionbank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * 练习记录适配器，用于ListView显示练习记录
 */
public class PracticeRecordAdapter extends BaseAdapter {
    private final Context context;
    private final List<PracticeRecord> records;
    private final LayoutInflater inflater;
    private final SimpleDateFormat dateFormat;
    
    public PracticeRecordAdapter(Context context, List<PracticeRecord> records) {
        this.context = context;
        this.records = records;
        this.inflater = LayoutInflater.from(context);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }
    
    @Override
    public int getCount() {
        return records.size();
    }
    
    @Override
    public Object getItem(int position) {
        return records.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        
        if (convertView == null) {
            // 加载列表项布局
            convertView = inflater.inflate(R.layout.item_practice_record, parent, false);
            
            // 创建ViewHolder
            holder = new ViewHolder();
            holder.tvExamTime = convertView.findViewById(R.id.tvExamTime);
            holder.tvQuestionBankTitle = convertView.findViewById(R.id.tvQuestionBankTitle);
            holder.tvScore = convertView.findViewById(R.id.tvScore);
            holder.tvCorrectRate = convertView.findViewById(R.id.tvCorrectRate);
            holder.tvUsedTime = convertView.findViewById(R.id.tvUsedTime);
            holder.tvResult = convertView.findViewById(R.id.tvResult);
            
            // 将ViewHolder存储在View中
            convertView.setTag(holder);
        } else {
            // 从View中获取ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }
        
        // 获取当前记录
        final PracticeRecord record = records.get(position);
        
        // 设置数据
        holder.tvExamTime.setText(dateFormat.format(record.getExamTime()));
        holder.tvQuestionBankTitle.setText(record.getQuestionBankTitle());
        holder.tvScore.setText(String.valueOf(record.getScore()));
        
        // 计算正确率
        double correctRate = (double) record.getCorrectQuestions() / record.getTotalQuestions() * 100;
        holder.tvCorrectRate.setText(String.format(Locale.getDefault(), "%.1f%%", correctRate));
        
        // 设置用时
        holder.tvUsedTime.setText(record.getUsedTimeString());
        
        // 设置考试结果
        if (record.isPassed()) {
            holder.tvResult.setText("通过");
            holder.tvResult.setTextColor(context.getResources().getColor(R.color.success_color));
        } else {
            holder.tvResult.setText("未通过");
            holder.tvResult.setTextColor(context.getResources().getColor(R.color.error_color));
        }
        
        // 设置点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到详情页面
                android.content.Intent intent = new android.content.Intent(context, PracticeRecordDetailActivity.class);
                intent.putExtra("PRACTICE_RECORD", record);
                context.startActivity(intent);
            }
        });
        
        return convertView;
    }
    
    /**
     * ViewHolder类，用于缓存列表项的视图组件
     */
    private static class ViewHolder {
        TextView tvExamTime;
        TextView tvQuestionBankTitle;
        TextView tvScore;
        TextView tvCorrectRate;
        TextView tvUsedTime;
        TextView tvResult;
    }
}