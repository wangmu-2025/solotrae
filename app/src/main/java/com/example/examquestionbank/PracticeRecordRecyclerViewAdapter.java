package com.example.examquestionbank;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * 练习记录RecyclerView适配器，用于显示练习记录列表并支持点击跳转和滑动删除
 */
public class PracticeRecordRecyclerViewAdapter extends RecyclerView.Adapter<PracticeRecordRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<PracticeRecord> records;
    private SimpleDateFormat dateFormat;
    private OnItemClickListener listener;

    /**
     * 定义点击事件接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public PracticeRecordRecyclerViewAdapter(Context context, List<PracticeRecord> records, OnItemClickListener listener) {
        this.context = context;
        this.records = records;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载列表项布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_practice_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 获取当前记录
        PracticeRecord record = records.get(position);

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


    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    /**
     * ViewHolder类，用于缓存列表项的视图组件
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvExamTime;
        TextView tvQuestionBankTitle;
        TextView tvScore;
        TextView tvCorrectRate;
        TextView tvUsedTime;
        TextView tvResult;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExamTime = itemView.findViewById(R.id.tvExamTime);
            tvQuestionBankTitle = itemView.findViewById(R.id.tvQuestionBankTitle);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvCorrectRate = itemView.findViewById(R.id.tvCorrectRate);
            tvUsedTime = itemView.findViewById(R.id.tvUsedTime);
            tvResult = itemView.findViewById(R.id.tvResult);
        }
    }

    /**
     * 删除指定位置的记录
     */
    public void removeItem(int position) {
        records.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, records.size());
    }

    /**
     * 获取指定位置的记录
     */
    public PracticeRecord getItem(int position) {
        return records.get(position);
    }
}