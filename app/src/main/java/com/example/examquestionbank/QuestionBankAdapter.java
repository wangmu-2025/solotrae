package com.example.examquestionbank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class QuestionBankAdapter extends RecyclerView.Adapter<QuestionBankAdapter.ViewHolder> {

    private List<QuestionBank> questionBanks;
    private OnItemClickListener listener;

    // 点击事件接口
    public interface OnItemClickListener {
        void onItemClick(QuestionBank questionBank);
    }

    public QuestionBankAdapter(List<QuestionBank> questionBanks, OnItemClickListener listener) {
        this.questionBanks = questionBanks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question_bank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionBank questionBank = questionBanks.get(position);
        holder.bind(questionBank);
    }

    @Override
    public int getItemCount() {
        return questionBanks != null ? questionBanks.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBankTitle;
        private TextView tvBankDescription;
        private TextView tvBankCategory;
        private TextView tvQuestionCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBankTitle = itemView.findViewById(R.id.tvBankTitle);
            tvBankDescription = itemView.findViewById(R.id.tvBankDescription);
            tvBankCategory = itemView.findViewById(R.id.tvBankCategory);
            tvQuestionCount = itemView.findViewById(R.id.tvQuestionCount);

            // 设置点击事件
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(questionBanks.get(position));
                }
            });
        }

        public void bind(QuestionBank questionBank) {
            tvBankTitle.setText(questionBank.getTitle());
            tvBankDescription.setText(questionBank.getDescription());
            tvBankCategory.setText("分类：" + questionBank.getCategory());
            tvQuestionCount.setText(questionBank.getQuestionCount() + "道题");
        }
    }
}