package com.example.examquestionbank;

import java.io.Serializable;
import java.util.Date;

/**
 * 练习记录类，用于存储模拟考试的历史记录
 */
public class PracticeRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String questionBankTitle;
    private long examTime; // 使用long类型存储时间戳，避免Gson序列化问题
    private int score;
    private int totalQuestions;
    private int correctQuestions;
    private long usedTimeMillis;
    private boolean passed;
    
    // 构造方法
    public PracticeRecord() {
        this.id = System.currentTimeMillis() + "";
        this.examTime = System.currentTimeMillis();
    }
    
    // Getter和Setter方法
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getQuestionBankTitle() {
        return questionBankTitle;
    }
    
    public void setQuestionBankTitle(String questionBankTitle) {
        this.questionBankTitle = questionBankTitle;
    }
    
    public long getExamTime() {
        return examTime;
    }
    
    public void setExamTime(long examTime) {
        this.examTime = examTime;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public int getTotalQuestions() {
        return totalQuestions;
    }
    
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    
    public int getCorrectQuestions() {
        return correctQuestions;
    }
    
    public void setCorrectQuestions(int correctQuestions) {
        this.correctQuestions = correctQuestions;
    }
    
    public long getUsedTimeMillis() {
        return usedTimeMillis;
    }
    
    public void setUsedTimeMillis(long usedTimeMillis) {
        this.usedTimeMillis = usedTimeMillis;
    }
    
    public boolean isPassed() {
        return passed;
    }
    
    public void setPassed(boolean passed) {
        this.passed = passed;
    }
    
    // 获取用时字符串，格式为 "mm:ss"
    public String getUsedTimeString() {
        long totalSeconds = usedTimeMillis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}