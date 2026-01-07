package com.example.exambankadmin.service.impl;

import com.example.exambankadmin.entity.QuestionBank;
import com.example.exambankadmin.repository.QuestionBankRepository;
import com.example.exambankadmin.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionBankServiceImpl implements QuestionBankService {

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Override
    public QuestionBank createQuestionBank(QuestionBank questionBank) {
        return questionBankRepository.save(questionBank);
    }

    @Override
    public List<QuestionBank> getAllQuestionBanks() {
        return questionBankRepository.findAll();
    }

    @Override
    public Optional<QuestionBank> getQuestionBankById(Long id) {
        return questionBankRepository.findById(id);
    }

    @Override
    public QuestionBank updateQuestionBank(Long id, QuestionBank questionBankDetails) {
        QuestionBank questionBank = questionBankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuestionBank not found with id: " + id));
        
        questionBank.setTitle(questionBankDetails.getTitle());
        questionBank.setDescription(questionBankDetails.getDescription());
        questionBank.setCategory(questionBankDetails.getCategory());
        
        return questionBankRepository.save(questionBank);
    }

    @Override
    public void deleteQuestionBank(Long id) {
        QuestionBank questionBank = questionBankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuestionBank not found with id: " + id));
        questionBankRepository.delete(questionBank);
    }

    @Override
    public List<QuestionBank> getQuestionBanksByCategory(String category) {
        return questionBankRepository.findByCategory(category);
    }

    @Override
    public List<QuestionBank> searchQuestionBanks(String keyword, String category) {
        if (keyword != null && !keyword.isEmpty() && category != null && !category.isEmpty()) {
            return questionBankRepository.findByTitleContainingIgnoreCaseAndCategory(keyword, category);
        } else if (keyword != null && !keyword.isEmpty()) {
            return questionBankRepository.findByTitleContainingIgnoreCase(keyword);
        } else if (category != null && !category.isEmpty()) {
            return questionBankRepository.findByCategory(category);
        } else {
            return questionBankRepository.findAll();
        }
    }

    @Override
    public long getQuestionBankCount() {
        return questionBankRepository.count();
    }
}
