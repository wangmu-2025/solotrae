package com.example.examquestionbank;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AnswerRecordManager {
    private static final String FILE_NAME = "answer_records.json";
    private final Gson gson = new Gson();
    private final Context context;

    public AnswerRecordManager(Context context) {
        this.context = context;
    }

    // 保存答题记录
    public void saveAnswerRecord(AnswerRecord record) {
        List<AnswerRecord> records = loadAllAnswerRecords();
        records.add(record);
        saveAllAnswerRecords(records);
    }

    // 保存所有答题记录
    private void saveAllAnswerRecords(List<AnswerRecord> records) {
        try (FileWriter writer = new FileWriter(new File(context.getFilesDir(), FILE_NAME))) {
            gson.toJson(records, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 加载所有答题记录
    public List<AnswerRecord> loadAllAnswerRecords() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<AnswerRecord>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 加载错题记录
    public List<AnswerRecord> loadWrongAnswerRecords() {
        List<AnswerRecord> allRecords = loadAllAnswerRecords();
        // 过滤出错误的记录
        return allRecords.stream()
                .filter(record -> !record.isCorrect())
                .collect(Collectors.toList());
    }

    // 加载特定题库的错题记录
    public List<AnswerRecord> loadWrongAnswerRecordsByBankId(String bankId) {
        List<AnswerRecord> wrongRecords = loadWrongAnswerRecords();
        return wrongRecords.stream()
                .filter(record -> java.util.Objects.equals(bankId, record.getQuestionBankId()))
                .collect(Collectors.toList());
    }

    // 标记错题为已掌握（从错题本中移除）
    public void markQuestionAsMastered(String questionId) {
        List<AnswerRecord> allRecords = loadAllAnswerRecords();
        // 移除特定题目的所有错题记录
        List<AnswerRecord> updatedRecords = allRecords.stream()
                .filter(record -> !java.util.Objects.equals(questionId, record.getQuestionId()) || record.isCorrect())
                .collect(Collectors.toList());
        saveAllAnswerRecords(updatedRecords);
    }

    // 获取特定题目的错题记录
    public List<AnswerRecord> getRecordsByQuestionId(String questionId) {
        List<AnswerRecord> allRecords = loadAllAnswerRecords();
        return allRecords.stream()
                .filter(record -> java.util.Objects.equals(questionId, record.getQuestionId()))
                .collect(Collectors.toList());
    }

    // 清空所有记录
    public void clearAllRecords() {
        saveAllAnswerRecords(new ArrayList<>());
    }

    // 获取答题统计
    public AnswerStatistics getAnswerStatistics() {
        List<AnswerRecord> allRecords = loadAllAnswerRecords();
        int total = allRecords.size();
        int correct = (int) allRecords.stream().filter(AnswerRecord::isCorrect).count();
        int wrong = total - correct;
        double correctRate = total > 0 ? (double) correct / total * 100 : 0;

        return new AnswerStatistics(total, correct, wrong, correctRate);
    }

    // 答题统计数据类
    public static class AnswerStatistics {
        private final int total;
        private final int correct;
        private final int wrong;
        private final double correctRate;

        public AnswerStatistics(int total, int correct, int wrong, double correctRate) {
            this.total = total;
            this.correct = correct;
            this.wrong = wrong;
            this.correctRate = correctRate;
        }

        public int getTotal() {
            return total;
        }

        public int getCorrect() {
            return correct;
        }

        public int getWrong() {
            return wrong;
        }

        public double getCorrectRate() {
            return correctRate;
        }
    }
}