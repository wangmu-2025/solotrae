package com.example.examquestionbank;

import java.io.Serializable;
import java.util.Date;

public class AnswerRecord implements Serializable {
    private String id;
    private String questionId;
    private String questionText;
    private String selectedAnswer;
    private String correctAnswer;
    private boolean isCorrect;
    private long timestamp;
    private String explanation;
    private String difficulty;
    private String questionBankId;
    private String questionBankTitle;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String optionE;

    // 构造函数
    public AnswerRecord() {
        this.id = java.util.UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }

    // 完整构造函数
    public AnswerRecord(String id, String questionId, String questionText, String selectedAnswer, String correctAnswer, boolean isCorrect, long timestamp, String explanation, String difficulty, String questionBankId, String questionBankTitle, String optionA, String optionB, String optionC, String optionD, String optionE) {
        this.id = id;
        this.questionId = questionId;
        this.questionText = questionText;
        this.selectedAnswer = selectedAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
        this.timestamp = timestamp;
        this.explanation = explanation;
        this.difficulty = difficulty;
        this.questionBankId = questionBankId;
        this.questionBankTitle = questionBankTitle;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.optionE = optionE;
    }

    // Getter和Setter方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestionBankId() {
        return questionBankId;
    }

    public void setQuestionBankId(String questionBankId) {
        this.questionBankId = questionBankId;
    }

    public String getQuestionBankTitle() {
        return questionBankTitle;
    }

    public void setQuestionBankTitle(String questionBankTitle) {
        this.questionBankTitle = questionBankTitle;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getOptionE() {
        return optionE;
    }

    public void setOptionE(String optionE) {
        this.optionE = optionE;
    }

    // 获取格式化的时间
    public String getFormattedTime() {
        Date date = new Date(timestamp);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }
}