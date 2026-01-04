package com.example.examquestionbank;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 练习记录管理器，用于保存和查询模拟考试的历史记录
 */
public class PracticeRecordManager {
    private static final String TAG = "PracticeRecordManager";
    private static final String FILE_NAME = "practice_records.json";
    private final Gson gson = new Gson();
    private final Context context;
    
    public PracticeRecordManager(Context context) {
        this.context = context;
    }
    
    /**
     * 保存练习记录
     */
    public void savePracticeRecord(PracticeRecord record) {
        try {
            List<PracticeRecord> records = getAllPracticeRecords();
            records.add(record);
            
            // 按时间倒序排序
            Collections.sort(records, new Comparator<PracticeRecord>() {
                @Override
                public int compare(PracticeRecord r1, PracticeRecord r2) {
                    // 使用Long.compare比较long类型的时间戳
                    return Long.compare(r2.getExamTime(), r1.getExamTime());
                }
            });
            
            // 保存到文件
            String json = gson.toJson(records);
            File file = new File(context.getFilesDir(), FILE_NAME);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(json);
                Log.d(TAG, "Practice record saved successfully");
            }
        } catch (IOException e) {
            Log.e(TAG, "Error saving practice record: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取所有练习记录，按时间倒序排列
     */
    public List<PracticeRecord> getAllPracticeRecords() {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            
            try (FileReader reader = new FileReader(file)) {
                Type type = new TypeToken<List<PracticeRecord>>() {}.getType();
                List<PracticeRecord> records = gson.fromJson(reader, type);
                return records != null ? records : new ArrayList<>();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading practice records: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 删除练习记录
     */
    public void deletePracticeRecord(String recordId) {
        try {
            List<PracticeRecord> records = getAllPracticeRecords();
            records.removeIf(record -> record.getId().equals(recordId));
            
            // 保存到文件
            String json = gson.toJson(records);
            File file = new File(context.getFilesDir(), FILE_NAME);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(json);
                Log.d(TAG, "Practice record deleted successfully");
            }
        } catch (IOException e) {
            Log.e(TAG, "Error deleting practice record: " + e.getMessage(), e);
        }
    }
    
    /**
     * 清空所有练习记录
     */
    public void clearAllPracticeRecords() {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (file.exists()) {
                file.delete();
                Log.d(TAG, "All practice records cleared successfully");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error clearing practice records: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取最近的练习记录
     */
    public PracticeRecord getLatestPracticeRecord() {
        List<PracticeRecord> records = getAllPracticeRecords();
        return records.isEmpty() ? null : records.get(0);
    }
    
    /**
     * 获取练习记录数量
     */
    public int getPracticeRecordCount() {
        return getAllPracticeRecords().size();
    }
}