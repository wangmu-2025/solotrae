package com.example.examquestionbank.controller;

import com.example.examquestionbank.entity.Question;
import com.example.examquestionbank.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // 在指定题库中创建题目
    @PostMapping("/bank/{bankId}")
    public ResponseEntity<Question> createQuestion(@PathVariable String bankId, @RequestBody Question question) {
        Question createdQuestion = questionService.createQuestion(bankId, question);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    // 获取所有题目
    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    // 根据ID获取题目
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        Question question = questionService.getQuestionById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    // 根据题库ID获取题目
    @GetMapping("/bank/{bankId}")
    public ResponseEntity<List<Question>> getQuestionsByBankId(@PathVariable String bankId) {
        List<Question> questions = questionService.getQuestionsByBankId(bankId);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    // 根据难度获取题目
    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<Question>> getQuestionsByDifficulty(@PathVariable String difficulty) {
        List<Question> questions = questionService.getQuestionsByDifficulty(difficulty);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    // 根据标签获取题目
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<Question>> getQuestionsByTag(@PathVariable String tag) {
        List<Question> questions = questionService.getQuestionsByTagsContaining(tag);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    // 根据关键词搜索题目
    @GetMapping("/search")
    public ResponseEntity<List<Question>> searchQuestions(@RequestParam String keyword) {
        List<Question> questions = questionService.getQuestionsByTextContaining(keyword);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    // 根据题型获取题目
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Question>> getQuestionsByType(@PathVariable String type) {
        List<Question> questions = questionService.getQuestionsByType(type);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    // 根据题库ID和题型获取题目
    @GetMapping("/bank/{bankId}/type/{type}")
    public ResponseEntity<List<Question>> getQuestionsByBankIdAndType(@PathVariable String bankId, @PathVariable String type) {
        List<Question> questions = questionService.getQuestionsByBankIdAndType(bankId, type);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    // 更新题目
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question questionDetails) {
        Question updatedQuestion = questionService.updateQuestion(id, questionDetails);
        return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
    }

    // 删除题目
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}