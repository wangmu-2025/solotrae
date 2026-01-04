package com.example.examquestionbank;

import java.util.List;

public class QuestionBank {
    private String id;
    private String title;
    private String description;
    private String category;
    private List<Question> questions;
    private long createdAt;
    private long updatedAt;

    // 构造函数
    public QuestionBank() {
    }

    public QuestionBank(String id, String title, String description, String category, List<Question> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.questions = questions;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // Getter和Setter方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    // 获取题目数量
    public int getQuestionCount() {
        return questions != null ? questions.size() : 0;
    }
}