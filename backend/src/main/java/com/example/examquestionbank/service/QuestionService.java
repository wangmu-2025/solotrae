package com.example.examquestionbank.service;

import com.example.examquestionbank.entity.Question;
import com.example.examquestionbank.entity.QuestionBank;
import com.example.examquestionbank.repository.QuestionRepository;
import com.example.examquestionbank.repository.QuestionBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionBankRepository questionBankRepository;

    public Question createQuestion(String bankId, Question question) {
        // 首先检查题库是否存在
        QuestionBank questionBank = questionBankRepository.findById(bankId)
                .orElseThrow(() -> new RuntimeException("QuestionBank not found with id: " + bankId));

        // 设置题目所属的题库
        question.setQuestionBank(questionBank);
        
        // 保存题目
        return questionRepository.save(question);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public List<Question> getQuestionsByBankId(String bankId) {
        return questionRepository.findByQuestionBankId(bankId);
    }

    public List<Question> getQuestionsByDifficulty(String difficulty) {
        return questionRepository.findByDifficulty(difficulty);
    }

    public List<Question> getQuestionsByTagsContaining(String tag) {
        return questionRepository.findByTagsContaining(tag);
    }

    public List<Question> getQuestionsByTextContaining(String keyword) {
        return questionRepository.findByTextContainingIgnoreCase(keyword);
    }

    // 根据题型获取题目
    public List<Question> getQuestionsByType(String type) {
        return questionRepository.findByType(type);
    }

    // 根据题库ID和题型获取题目
    public List<Question> getQuestionsByBankIdAndType(String bankId, String type) {
        return questionRepository.findByQuestionBankIdAndType(bankId, type);
    }

    public Question updateQuestion(Long id, Question questionDetails) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));

        question.setText(questionDetails.getText());
        question.setOptionA(questionDetails.getOptionA());
        question.setOptionB(questionDetails.getOptionB());
        question.setOptionC(questionDetails.getOptionC());
        question.setOptionD(questionDetails.getOptionD());
        question.setAnswer(questionDetails.getAnswer());
        question.setExplanation(questionDetails.getExplanation());
        question.setDifficulty(questionDetails.getDifficulty());
        question.setType(questionDetails.getType());
        question.setTags(questionDetails.getTags());

        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}