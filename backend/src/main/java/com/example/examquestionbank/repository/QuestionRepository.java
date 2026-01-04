package com.example.examquestionbank.repository;

import com.example.examquestionbank.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuestionBankId(String questionBankId);
    List<Question> findByDifficulty(String difficulty);
    List<Question> findByTagsContaining(String tag);
    List<Question> findByTextContainingIgnoreCase(String keyword);
    List<Question> findByType(String type);
    List<Question> findByQuestionBankIdAndType(String questionBankId, String type);
}