package com.example.exambankadmin.controller;

import com.example.exambankadmin.entity.QuestionBank;
import com.example.exambankadmin.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question-banks")
public class QuestionBankController {

    @Autowired
    private QuestionBankService questionBankService;

    /**
     * 获取所有题库
     */
    @GetMapping
    public ResponseEntity<List<QuestionBank>> getAllQuestionBanks() {
        List<QuestionBank> banks = questionBankService.getAllQuestionBanks();
        return ResponseEntity.ok(banks);
    }

    /**
     * 根据ID获取题库
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestionBank> getQuestionBankById(@PathVariable Long id) {
        QuestionBank bank = questionBankService.getQuestionBankById(id)
                .orElseThrow(() -> new RuntimeException("QuestionBank not found with id: " + id));
        return ResponseEntity.ok(bank);
    }

    /**
     * 创建题库
     */
    @PostMapping
    public ResponseEntity<QuestionBank> createQuestionBank(@RequestBody QuestionBank questionBank) {
        QuestionBank createdBank = questionBankService.createQuestionBank(questionBank);
        return new ResponseEntity<>(createdBank, HttpStatus.CREATED);
    }

    /**
     * 更新题库
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuestionBank> updateQuestionBank(@PathVariable Long id, @RequestBody QuestionBank questionBankDetails) {
        QuestionBank updatedBank = questionBankService.updateQuestionBank(id, questionBankDetails);
        return ResponseEntity.ok(updatedBank);
    }

    /**
     * 删除题库
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionBank(@PathVariable Long id) {
        questionBankService.deleteQuestionBank(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 搜索题库
     */
    @GetMapping("/search")
    public ResponseEntity<List<QuestionBank>> searchQuestionBanks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        List<QuestionBank> banks = questionBankService.searchQuestionBanks(keyword, category);
        return ResponseEntity.ok(banks);
    }

    /**
     * 获取题库总数
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getQuestionBankCount() {
        long count = questionBankService.getQuestionBankCount();
        return ResponseEntity.ok(count);
    }
}