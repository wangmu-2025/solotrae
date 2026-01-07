package com.example.exambankadmin.repository;

import com.example.exambankadmin.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuestionBankId(Long bankId);
    List<Question> findByType(String type);
    List<Question> findByDifficulty(String difficulty);
    List<Question> findByTextContainingIgnoreCase(String keyword);
    
    /**
     * 多条件组合查询题目
     */
    List<Question> findByQuestionBankIdAndTypeAndDifficultyAndTextContainingIgnoreCase(
            Long bankId, String type, String difficulty, String keyword);
    
    /**
     * 支持部分条件的组合查询
     */
    List<Question> findByQuestionBankIdAndTypeAndDifficulty(
            Long bankId, String type, String difficulty);
    
    List<Question> findByQuestionBankIdAndTypeAndTextContainingIgnoreCase(
            Long bankId, String type, String keyword);
    
    List<Question> findByQuestionBankIdAndDifficultyAndTextContainingIgnoreCase(
            Long bankId, String difficulty, String keyword);
    
    List<Question> findByTypeAndDifficultyAndTextContainingIgnoreCase(
            String type, String difficulty, String keyword);
    
    List<Question> findByQuestionBankIdAndTextContainingIgnoreCase(
            Long bankId, String keyword);
    
    List<Question> findByTypeAndTextContainingIgnoreCase(
            String type, String keyword);
    
    List<Question> findByDifficultyAndTextContainingIgnoreCase(
            String difficulty, String keyword);
}