package com.example.examquestionbank;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ApiService {
    private static final String TAG = "ApiService";
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private final OkHttpClient client;
    private final Gson gson;

    public ApiService() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    // 获取所有题库列表
    public List<QuestionBank> getQuestionBanks() {
        String url = BASE_URL + "question-banks";
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseData = response.body().string();
                Type listType = new TypeToken<List<QuestionBank>>() {}.getType();
                return gson.fromJson(responseData, listType);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error fetching question banks: " + e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    // 获取指定题库的题目
    public List<Question> getQuestionsByBankId(String bankId) {
        String url = BASE_URL + "questions/bank/" + bankId;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseData = response.body().string();
                Type listType = new TypeToken<List<Question>>() {}.getType();
                return gson.fromJson(responseData, listType);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error fetching questions for bank " + bankId + ": " + e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    // 获取指定题目的详细信息
    public Question getQuestionById(String questionId) {
        String url = BASE_URL + "questions/" + questionId;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseData = response.body().string();
                return gson.fromJson(responseData, Question.class);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error fetching question " + questionId + ": " + e.getMessage(), e);
        }
        return null;
    }
}