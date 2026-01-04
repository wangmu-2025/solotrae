package com.example.examquestionbank;

/**
 * 考试参数配置类，用于存储考试时间、及格分数等设置
 */
public class ExamConfig implements java.io.Serializable {
    private int examTime; // 考试时长，单位：分钟
    private double passingScore; // 及格分数
    private boolean isPercentage; // 是否为百分比，true表示百分比，false表示分数值
    private boolean showAnswerAfterSubmit; // 答题后是否显示答案
    private String multipleChoiceScoreMode; // 多选题得分模式

    // 默认构造函数
    public ExamConfig() {
        this.examTime = 45; // 默认45分钟
        this.passingScore = 60; // 默认60分或60%
        this.isPercentage = true; // 默认百分比
        this.showAnswerAfterSubmit = true; // 默认显示答案
        this.multipleChoiceScoreMode = "all_correct"; // 默认全部选对才算得分
    }

    // 带参数的构造函数
    public ExamConfig(int examTime, double passingScore, boolean isPercentage) {
        this.examTime = examTime;
        this.passingScore = passingScore;
        this.isPercentage = isPercentage;
        this.showAnswerAfterSubmit = true;
        this.multipleChoiceScoreMode = "all_correct";
    }

    // Getter和Setter方法
    public int getExamTime() {
        return examTime;
    }

    public void setExamTime(int examTime) {
        this.examTime = examTime;
    }

    public double getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(double passingScore) {
        this.passingScore = passingScore;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public void setPercentage(boolean percentage) {
        isPercentage = percentage;
    }

    public boolean isShowAnswerAfterSubmit() {
        return showAnswerAfterSubmit;
    }

    public void setShowAnswerAfterSubmit(boolean showAnswerAfterSubmit) {
        this.showAnswerAfterSubmit = showAnswerAfterSubmit;
    }

    public String getMultipleChoiceScoreMode() {
        return multipleChoiceScoreMode;
    }

    public void setMultipleChoiceScoreMode(String multipleChoiceScoreMode) {
        this.multipleChoiceScoreMode = multipleChoiceScoreMode;
    }

    // 计算实际及格分数（根据总分和及格分数类型）
    public double calculateActualPassingScore(double totalScore) {
        if (isPercentage) {
            // 如果是百分比，计算实际分数
            return totalScore * passingScore / 100;
        } else {
            // 否则直接返回设置的分数
            return passingScore;
        }
    }
}