package com.example.exambankadmin.service;

import com.example.exambankadmin.entity.QuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface QuestionBankService {
    QuestionBank createQuestionBank(QuestionBank questionBank);
    List<QuestionBank> getAllQuestionBanks();
    Optional<QuestionBank> getQuestionBankById(Long id);
    QuestionBank updateQuestionBank(Long id, QuestionBank questionBankDetails);
    void deleteQuestionBank(Long id);
    List<QuestionBank> getQuestionBanksByCategory(String category);
    List<QuestionBank> searchQuestionBanks(String keyword, String category);
    long getQuestionBankCount();
}
