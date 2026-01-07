package com.example.examquestionbank.service;

import com.example.examquestionbank.entity.QuestionBank;
import com.example.examquestionbank.repository.QuestionBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionBankService {

    @Autowired
    private QuestionBankRepository questionBankRepository;

    public QuestionBank createQuestionBank(QuestionBank questionBank) {
        return questionBankRepository.save(questionBank);
    }

    public List<QuestionBank> getAllQuestionBanks() {
        return questionBankRepository.findAll();
    }

    public Optional<QuestionBank> getQuestionBankById(Long id) {
        return questionBankRepository.findById(id);
    }

    public List<QuestionBank> getQuestionBanksByCategory(String category) {
        return questionBankRepository.findByCategory(category);
    }

    public QuestionBank updateQuestionBank(Long id, QuestionBank questionBankDetails) {
        QuestionBank questionBank = questionBankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuestionBank not found with id: " + id));

        questionBank.setTitle(questionBankDetails.getTitle());
        questionBank.setDescription(questionBankDetails.getDescription());
        questionBank.setCategory(questionBankDetails.getCategory());

        return questionBankRepository.save(questionBank);
    }

    public void deleteQuestionBank(Long id) {
        questionBankRepository.deleteById(id);
    }

    /**
     * 获取题库总数
     */
    public long getQuestionBankCount() {
        return questionBankRepository.count();
    }
}