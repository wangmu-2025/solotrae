package com.example.examquestionbank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {
    // 题型常量
    public static final String TYPE_SINGLE = "SINGLE_CHOICE";      // 单选题
    public static final String TYPE_MULTIPLE = "MULTIPLE_CHOICE";    // 多选题
    public static final String TYPE_JUDGE = "JUDGE";        // 判断题

    private String id;           // 题目ID
    private String text;         // 题目内容
    private String optionA;      // 选项A
    private String optionB;      // 选项B
    private String optionC;      // 选项C
    private String optionD;      // 选项D
    private String answer;       // 正确答案 A/B/C/D 或多个答案（多选题用逗号分隔，如"A,B"）
    private String selectedAnswer; // 用户选择的答案
    private String explanation;  // 题目解析
    private String difficulty;   // 难度等级（简单/中等/困难）
    private List<String> tags;   // 标签列表
    private String type;         // 题型（single/multiple/judge）

    // 构造函数（兼容旧格式，默认为单选题）
    public Question(String text, String optionA, String optionB,
                    String optionC, String optionD, String answer) {
        this.text = text;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
        this.difficulty = "中等"; // 默认难度
        this.type = TYPE_SINGLE;   // 默认单选题
    }

    // 全参构造函数
    public Question(String id, String text, String optionA, String optionB,
                    String optionC, String optionD, String answer, String explanation,
                    String difficulty, List<String> tags) {
        this.id = id;
        this.text = text;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
        this.explanation = explanation;
        this.difficulty = difficulty;
        this.tags = tags;
        this.type = TYPE_SINGLE;   // 默认单选题
    }

    // 全参构造函数（带题型）
    public Question(String id, String text, String optionA, String optionB,
                    String optionC, String optionD, String answer, String explanation,
                    String difficulty, List<String> tags, String type) {
        this.id = id;
        this.text = text;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
        this.explanation = explanation;
        this.difficulty = difficulty;
        this.tags = tags;
        this.type = type != null ? type : TYPE_SINGLE;   // 默认为单选题
    }

    // Getter和Setter方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }
    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }
    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }
    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(String selectedAnswer) { this.selectedAnswer = selectedAnswer; }

    // 检查答案是否正确
    public boolean isCorrect(String selectedAnswer) {
        // 根据题型检查答案
        if (TYPE_MULTIPLE.equals(type)) {
            // 多选题：需要比较所有选中的选项是否与正确答案完全匹配
            // 假设selectedAnswer格式为"A,B,C"，answer格式也为"A,B,C"
            return answer.equalsIgnoreCase(selectedAnswer);
        } else if (TYPE_JUDGE.equals(type)) {
            // 判断题：正确答案为"A"（对）或"B"（错）
            return answer.equalsIgnoreCase(selectedAnswer);
        } else {
            // 单选题：默认情况
            return answer.equalsIgnoreCase(selectedAnswer);
        }
    }

    // 打乱选项顺序
    public void shuffleOptions() {
        // 只有单选题和多选题需要打乱选项
        if (!TYPE_JUDGE.equals(type)) {
            // 创建选项列表
            List<Option> options = new ArrayList<>();
            if (optionA != null && !optionA.isEmpty()) {
                options.add(new Option("A", optionA));
            }
            if (optionB != null && !optionB.isEmpty()) {
                options.add(new Option("B", optionB));
            }
            if (optionC != null && !optionC.isEmpty()) {
                options.add(new Option("C", optionC));
            }
            if (optionD != null && !optionD.isEmpty()) {
                options.add(new Option("D", optionD));
            }

            // 打乱选项顺序
            Collections.shuffle(options);

            // 重置所有选项为null，准备重新赋值
            optionA = null;
            optionB = null;
            optionC = null;
            optionD = null;

            // 重新设置选项，只包含有内容的选项
            for (int i = 0; i < options.size(); i++) {
                Option opt = options.get(i);
                char newKey = (char) ('A' + i);
                switch (newKey) {
                    case 'A':
                        optionA = opt.content;
                        break;
                    case 'B':
                        optionB = opt.content;
                        break;
                    case 'C':
                        optionC = opt.content;
                        break;
                    case 'D':
                        optionD = opt.content;
                        break;
                }
            }
            
            // 更新正确答案
            if (answer != null && !answer.isEmpty()) {
                String[] correctAnswers = answer.split(",");
                StringBuilder newAnswer = new StringBuilder();
                
                // 创建映射关系：原选项 -> 新选项
                // 例如：原A -> 新B，表示原A选项现在在新的B位置
                for (int i = 0; i < options.size(); i++) {
                    Option opt = options.get(i);
                    char newKey = (char) ('A' + i);
                    
                    // 检查当前原选项是否在正确答案中
                    for (String correctAnswer : correctAnswers) {
                        if (correctAnswer.trim().equals(opt.key)) {
                            newAnswer.append(newKey);
                            newAnswer.append(",");
                            break;
                        }
                    }
                }
                
                // 移除末尾的逗号
                if (newAnswer.length() > 0) {
                    newAnswer.setLength(newAnswer.length() - 1);
                    // 更新答案
                    answer = newAnswer.toString();
                }
            }
        }
    }

    // 选项内部类
    private static class Option {
        String key;
        String content;

        Option(String key, String content) {
            this.key = key;
            this.content = content;
        }
    }
}