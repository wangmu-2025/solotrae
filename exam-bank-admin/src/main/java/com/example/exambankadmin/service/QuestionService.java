package com.example.exambankadmin.service;

import com.example.exambankadmin.entity.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    Question createQuestion(Question question);
    Question createQuestion(Long bankId, Question question);
    List<Question> getAllQuestions();
    Optional<Question> getQuestionById(Long id);
    Question updateQuestion(Long id, Question questionDetails);
    void deleteQuestion(Long id);
    List<Question> getQuestionsByBankId(Long bankId);
    List<Question> getQuestionsByType(String type);
    List<Question> getQuestionsByDifficulty(String difficulty);
    List<Question> searchQuestions(String keyword);
    
    /**
     * 多条件组合查询题目
     */
    List<Question> searchQuestions(String keyword, Long bankId, String type, String difficulty);
    
    long getQuestionCount();
}