package com.example.exambankadmin.service.impl;

import com.example.exambankadmin.entity.Question;
import com.example.exambankadmin.entity.QuestionBank;
import com.example.exambankadmin.repository.QuestionRepository;
import com.example.exambankadmin.service.QuestionBankService;
import com.example.exambankadmin.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuestionBankService questionBankService;

    @Override
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }
    
    @Override
    public Question createQuestion(Long bankId, Question question) {
        // 根据bankId获取题库
        QuestionBank questionBank = questionBankService.getQuestionBankById(bankId)
                .orElseThrow(() -> new RuntimeException("QuestionBank not found with id: " + bankId));
        
        // 将题目与题库关联
        question.setQuestionBank(questionBank);
        
        // 保存题目
        return questionRepository.save(question);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    @Override
    public Question updateQuestion(Long id, Question questionDetails) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        
        question.setText(questionDetails.getText());
        question.setType(questionDetails.getType());
        question.setAnswer(questionDetails.getAnswer());
        question.setDifficulty(questionDetails.getDifficulty());
        question.setScore(questionDetails.getScore());
        question.setOptionA(questionDetails.getOptionA());
        question.setOptionB(questionDetails.getOptionB());
        question.setOptionC(questionDetails.getOptionC());
        question.setOptionD(questionDetails.getOptionD());
        question.setAnalysis(questionDetails.getAnalysis());
        question.setTags(questionDetails.getTags());
        
        return questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        questionRepository.delete(question);
    }

    @Override
    public List<Question> getQuestionsByBankId(Long bankId) {
        return questionRepository.findByQuestionBankId(bankId);
    }

    @Override
    public List<Question> getQuestionsByType(String type) {
        return questionRepository.findByType(type);
    }

    @Override
    public List<Question> getQuestionsByDifficulty(String difficulty) {
        return questionRepository.findByDifficulty(difficulty);
    }

    @Override
    public List<Question> searchQuestions(String keyword) {
        return questionRepository.findByTextContainingIgnoreCase(keyword);
    }
    
    @Override
    public List<Question> searchQuestions(String keyword, Long bankId, String type, String difficulty) {
        // 根据提供的条件组合调用不同的查询方法
        if (keyword != null && bankId != null && type != null && difficulty != null) {
            return questionRepository.findByQuestionBankIdAndTypeAndDifficultyAndTextContainingIgnoreCase(
                    bankId, type, difficulty, keyword);
        } else if (bankId != null && type != null && difficulty != null) {
            return questionRepository.findByQuestionBankIdAndTypeAndDifficulty(
                    bankId, type, difficulty);
        } else if (keyword != null && bankId != null && type != null) {
            return questionRepository.findByQuestionBankIdAndTypeAndTextContainingIgnoreCase(
                    bankId, type, keyword);
        } else if (keyword != null && bankId != null && difficulty != null) {
            return questionRepository.findByQuestionBankIdAndDifficultyAndTextContainingIgnoreCase(
                    bankId, difficulty, keyword);
        } else if (keyword != null && type != null && difficulty != null) {
            return questionRepository.findByTypeAndDifficultyAndTextContainingIgnoreCase(
                    type, difficulty, keyword);
        } else if (keyword != null && bankId != null) {
            return questionRepository.findByQuestionBankIdAndTextContainingIgnoreCase(
                    bankId, keyword);
        } else if (keyword != null && type != null) {
            return questionRepository.findByTypeAndTextContainingIgnoreCase(
                    type, keyword);
        } else if (keyword != null && difficulty != null) {
            return questionRepository.findByDifficultyAndTextContainingIgnoreCase(
                    difficulty, keyword);
        } else if (keyword != null) {
            return searchQuestions(keyword);
        } else if (bankId != null) {
            return getQuestionsByBankId(bankId);
        } else if (type != null) {
            return getQuestionsByType(type);
        } else if (difficulty != null) {
            return getQuestionsByDifficulty(difficulty);
        } else {
            return getAllQuestions();
        }
    }

    @Override
    public long getQuestionCount() {
        return questionRepository.count();
    }
}
