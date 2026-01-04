package com.example.examquestionbank.repository;

import com.example.examquestionbank.entity.AnswerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRecordRepository extends JpaRepository<AnswerRecord, Long> {
    List<AnswerRecord> findByUserId(String userId);
    List<AnswerRecord> findByQuestionBankId(String questionBankId);
    List<AnswerRecord> findByPracticeRecordId(Long practiceRecordId);
    List<AnswerRecord> findByQuestionId(Long questionId);
    List<AnswerRecord> findByUserIdAndQuestionId(String userId, Long questionId);
    List<AnswerRecord> findByUserIdAndIsCorrect(String userId, Boolean isCorrect);
    List<AnswerRecord> findByUserIdAndQuestionBankId(String userId, String questionBankId);
}
