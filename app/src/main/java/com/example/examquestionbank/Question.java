package com.example.examquestionbank;

public class Question {
    private String text;      // 题目
    private String optionA;   // 选项A
    private String optionB;   // 选项B
    private String optionC;   // 选项C
    private String optionD;   // 选项D
    private String answer;    // 正确答案 A/B/C/D

    // 构造函数
    public Question(String text, String optionA, String optionB,
                    String optionC, String optionD, String answer) {
        this.text = text;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
    }

    // Getter方法
    public String getText() { return text; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getAnswer() { return answer; }

    // 检查答案是否正确
    public boolean isCorrect(String selectedAnswer) {
        return answer.equals(selectedAnswer);
    }
}