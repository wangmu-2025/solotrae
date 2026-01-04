package com.example.examquestionbank.repository;

import com.example.examquestionbank.entity.PracticeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRecordRepository extends JpaRepository<PracticeRecord, Long> {
    List<PracticeRecord> findByUserId(String userId);
    List<PracticeRecord> findByQuestionBankId(String questionBankId);
    List<PracticeRecord> findByPracticeType(String practiceType);
    List<PracticeRecord> findByUserIdAndQuestionBankId(String userId, String questionBankId);
    List<PracticeRecord> findByUserIdAndPracticeType(String userId, String practiceType);
}
