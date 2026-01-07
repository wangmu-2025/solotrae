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

public class QuestionManager {
    private final List<Question> questionList = new ArrayList<>();
    private int currentIndex = 0;
    private int correctCount = 0;
    private final List<Question> wrongQuestions = new ArrayList<>();
    private static final String TAG = "QuestionManager";

    // 获取题目列表（用于题库选择）
    public List<Question> getQuestionList() {
        return new ArrayList<>(questionList);
    }

    public void loadQuestions(Context context) {
        loadQuestions(context, null);
    }

    // 重载loadQuestions方法，支持指定题库ID
    public void loadQuestions(Context context, String bankId) {
        questionList.clear();
        wrongQuestions.clear();
        correctCount = 0;
        currentIndex = 0;

        try {
            // 1. 优先从后端API获取题目
            boolean loaded = false;
            if (bankId != null) {
                try {
                    ApiService apiService = new ApiService();
                    List<Question> questions = apiService.getQuestionsByBankId(bankId);
                    if (questions != null && !questions.isEmpty()) {
                        questionList.addAll(questions);
                        loaded = true;
                        Log.d(TAG, "Loading questions from API for bank: " + bankId);
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Failed to load questions from API, trying local files: " + e.getMessage());
                }
            }

            // 2. 如果API获取失败，从本地文件读取
            if (!loaded) {
                // 只使用JSON格式作为唯一数据源
                String fileName;
                if (bankId != null) {
                    fileName = bankId + ".json";
                } else {
                    fileName = "questions.json";
                }
                
                // 2.1 优先从assets目录读取JSON文件
                try {
                    try (InputStream is = context.getAssets().open(fileName);
                         Reader reader = new InputStreamReader(is)) {
                        Gson gson = new Gson();
                        Type questionBankType = new TypeToken<QuestionBank>() {}.getType();
                        QuestionBank questionBank = gson.fromJson(reader, questionBankType);
                        if (questionBank != null && questionBank.getQuestions() != null) {
                            questionList.addAll(questionBank.getQuestions());
                            loaded = true;
                            Log.d(TAG, "Loading questions from assets JSON file: " + fileName);
                        }
                    }
                } catch (IOException e) {
                    Log.d(TAG, "JSON file not found in assets, trying files directory");
                }
                
                // 2.2 如果assets目录没有，尝试从files目录读取
                if (!loaded) {
                    File jsonFile = new File(context.getFilesDir(), fileName);
                    if (jsonFile.exists()) {
                        // 读取JSON格式题库
                        Log.d(TAG, "Loading questions from files directory JSON file: " + jsonFile.getName());
                        loadQuestionsFromJson(context, jsonFile);
                        loaded = true;
                    }
                }
                
                // 2.3 如果都没有找到，记录错误
                if (!loaded) {
                    Log.e(TAG, "JSON file not found: " + fileName);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading questions: " + e.getMessage(), e);
        }
    }

    // 从JSON文件加载题目
    private void loadQuestionsFromJson(Context context, File jsonFile) {
        try (FileReader reader = new FileReader(jsonFile)) {
            Gson gson = new Gson();
            Type questionBankType = new TypeToken<QuestionBank>() {}.getType();
            QuestionBank questionBank = gson.fromJson(reader, questionBankType);
            if (questionBank != null && questionBank.getQuestions() != null) {
                questionList.addAll(questionBank.getQuestions());
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading JSON file: " + e.getMessage(), e);
        }
    }

    // 从TXT文件加载题目（兼容旧格式）
    private void loadQuestionsFromTxt(Context context, String bankId) {
        try {
            InputStream is;
            File userFile;
            boolean found = false;

            // 先尝试按照指定ID查找文件
            if (bankId != null) {
                // 先检查外部存储中是否存在指定ID的文件
                userFile = new File(context.getFilesDir(), bankId + ".txt");
                if (userFile.exists()) {
                    is = new FileInputStream(userFile);
                    found = true;
                } else {
                    try {
                        // 再检查assets中是否存在指定ID的文件
                        is = context.getAssets().open(bankId + ".txt");
                        found = true;
                    } catch (IOException e) {
                        // 如果找不到指定ID的文件，回退到默认的questions.txt
                        Log.d(TAG, "Specified bankId file not found, falling back to default questions.txt");
                        is = context.getAssets().open("questions.txt");
                    }
                }
            } else {
                // 如果没有指定ID，直接加载默认的questions.txt
                userFile = new File(context.getFilesDir(), "questions.txt");
                if (userFile.exists()) {
                    is = new FileInputStream(userFile);
                } else {
                    is = context.getAssets().open("questions.txt");
                }
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split("\\|");
                    if (p.length == 6) {
                        questionList.add(new Question(p[0], p[2], p[3], p[4], p[5], p[1]));
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading TXT file: " + e.getMessage(), e);
        }
    }

    // 将题目保存为JSON格式
    public void saveQuestionsAsJson(Context context, String bankId, String title, String description, String category) {
        try {
            QuestionBank questionBank = new QuestionBank(bankId, title, description, category, questionList);
            Gson gson = new Gson();
            String json = gson.toJson(questionBank);

            File jsonFile = new File(context.getFilesDir(), bankId + ".json");
            try (FileWriter writer = new FileWriter(jsonFile)) {
                writer.write(json);
                Log.d(TAG, "Questions saved to JSON file: " + jsonFile.getName());
            }
        } catch (IOException e) {
            Log.e(TAG, "Error saving questions to JSON: " + e.getMessage(), e);
        }
    }

    public Question getCurrentQuestion() {
        if (currentIndex >= 0 && currentIndex < questionList.size()) {
            return questionList.get(currentIndex);
        }
        return null;
    }

    public int getTotalQuestions() {
        return questionList.size();
    }

    public int getCurrentQuestionNumber() {
        return currentIndex + 1;
    }

    public boolean nextQuestion() {
        if (currentIndex < questionList.size() - 1) {
            currentIndex++;
            return true;
        }
        return false;
    }

    public boolean previousQuestion() {
        if (currentIndex > 0) {
            currentIndex--;
            return true;
        }
        return false;
    }

    public void goToQuestion(int index) {
        if (index >= 0 && index < questionList.size()) {
            currentIndex = index;
        }
    }

    public boolean checkAnswer(String selectedAnswer) {
        Question q = getCurrentQuestion();
        return q != null && q.isCorrect(selectedAnswer);
    }

    public void recordAnswer(boolean correct, Question q) {
        if (correct) correctCount++;
        if (!correct && !wrongQuestions.contains(q)) wrongQuestions.add(q);
    }

    public List<Question> getWrongQuestions() {
        return new ArrayList<>(wrongQuestions);
    }

    public String getStatistics() {
        int answered = currentIndex + 1;
        return "正确: " + (answered - wrongQuestions.size()) + "/" + answered +
                (answered == 0 ? "" : " (" + (100 * (answered - wrongQuestions.size()) / answered) + "%)");
    }
    
    /**
     * 重新检查所有题目的答案，确保统计数据准确
     */
    public void recheckAllAnswers() {
        // 清空之前的统计数据
        wrongQuestions.clear();
        
        // 遍历所有题目，重新检查答案
        for (Question question : questionList) {
            String selectedAnswer = question.getSelectedAnswer();
            if (selectedAnswer != null && !selectedAnswer.isEmpty()) {
                boolean isCorrect = question.isCorrect(selectedAnswer);
                if (!isCorrect) {
                    wrongQuestions.add(question);
                }
            }
        }
    }
    
    /**
     * 获取所有题目列表
     */
    public List<Question> getQuestions() {
        return new ArrayList<>(questionList);
    }


}