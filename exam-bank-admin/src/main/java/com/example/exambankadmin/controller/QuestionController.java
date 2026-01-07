package com.example.exambankadmin.controller;

import com.example.exambankadmin.entity.Question;
import com.example.exambankadmin.service.QuestionService;
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

    /**
     * 获取所有题目
     */
    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    /**
     * 根据ID获取题目
     */
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        Question question = questionService.getQuestionById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        return ResponseEntity.ok(question);
    }

    /**
     * 根据题库ID获取题目
     */
    @GetMapping("/bank/{bankId}")
    public ResponseEntity<List<Question>> getQuestionsByBankId(@PathVariable Long bankId) {
        List<Question> questions = questionService.getQuestionsByBankId(bankId);
        return ResponseEntity.ok(questions);
    }

    /**
     * 根据题型获取题目
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Question>> getQuestionsByType(@PathVariable String type) {
        List<Question> questions = questionService.getQuestionsByType(type);
        return ResponseEntity.ok(questions);
    }

    /**
     * 根据难度获取题目
     */
    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<Question>> getQuestionsByDifficulty(@PathVariable String difficulty) {
        List<Question> questions = questionService.getQuestionsByDifficulty(difficulty);
        return ResponseEntity.ok(questions);
    }

    /**
     * 搜索题目 - 支持多种过滤条件组合
     */
    @GetMapping("/search")
    public ResponseEntity<List<Question>> searchQuestions(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long bankId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String difficulty) {
        // 调用新实现的多条件组合查询方法
        List<Question> questions = questionService.searchQuestions(keyword, bankId, type, difficulty);
        return ResponseEntity.ok(questions);
    }

    /**
     * 创建题目
     */
    @PostMapping("/bank/{bankId}")
    public ResponseEntity<Question> createQuestion(@PathVariable Long bankId, @RequestBody Question question) {
        // 这里应该将题目与指定的题库关联起来
        Question createdQuestion = questionService.createQuestion(bankId, question);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    /**
     * 更新题目
     */
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question questionDetails) {
        Question updatedQuestion = questionService.updateQuestion(id, questionDetails);
        return ResponseEntity.ok(updatedQuestion);
    }

    /**
     * 删除题目
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 获取题目总数
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getQuestionCount() {
        long count = questionService.getQuestionCount();
        return ResponseEntity.ok(count);
    }
}