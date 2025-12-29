package com.example.examquestionbank;

import android.content.Context;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionManager {
    private final List<Question> questionList = new ArrayList<>();
    private int currentIndex = 0;
    private int correctCount = 0;
    private final List<Question> wrongQuestions = new ArrayList<>();

    public void loadQuestions(Context context) {
        questionList.clear();
        wrongQuestions.clear();
        correctCount = 0;
        try {
            // 优先读取用户上传的私有文件，不存在再回退到 assets
            InputStream is;
            File userFile = new File(context.getFilesDir(), "questions.txt");
            if (userFile.exists()) {
                is = new FileInputStream(userFile);
            } else {
                is = context.getAssets().open("questions.txt");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split("\\|");
                    if (p.length == 6) questionList.add(new Question(p[0], p[2], p[3], p[4], p[5], p[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        return q != null && q.getAnswer().equalsIgnoreCase(selectedAnswer);
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
}