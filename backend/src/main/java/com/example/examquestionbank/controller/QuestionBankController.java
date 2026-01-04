package com.example.examquestionbank.controller;

import com.example.examquestionbank.entity.QuestionBank;
import com.example.examquestionbank.service.QuestionBankService;
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

    // 创建题库
    @PostMapping
    public ResponseEntity<QuestionBank> createQuestionBank(@RequestBody QuestionBank questionBank) {
        QuestionBank createdBank = questionBankService.createQuestionBank(questionBank);
        return new ResponseEntity<>(createdBank, HttpStatus.CREATED);
    }

    // 获取所有题库
    @GetMapping
    public ResponseEntity<List<QuestionBank>> getAllQuestionBanks() {
        List<QuestionBank> banks = questionBankService.getAllQuestionBanks();
        return new ResponseEntity<>(banks, HttpStatus.OK);
    }

    // 根据ID获取题库
    @GetMapping("/{id}")
    public ResponseEntity<QuestionBank> getQuestionBankById(@PathVariable String id) {
        QuestionBank bank = questionBankService.getQuestionBankById(id)
                .orElseThrow(() -> new RuntimeException("QuestionBank not found with id: " + id));
        return new ResponseEntity<>(bank, HttpStatus.OK);
    }

    // 根据分类获取题库
    @GetMapping("/category/{category}")
    public ResponseEntity<List<QuestionBank>> getQuestionBanksByCategory(@PathVariable String category) {
        List<QuestionBank> banks = questionBankService.getQuestionBanksByCategory(category);
        return new ResponseEntity<>(banks, HttpStatus.OK);
    }

    // 更新题库
    @PutMapping("/{id}")
    public ResponseEntity<QuestionBank> updateQuestionBank(@PathVariable String id, @RequestBody QuestionBank bankDetails) {
        QuestionBank updatedBank = questionBankService.updateQuestionBank(id, bankDetails);
        return new ResponseEntity<>(updatedBank, HttpStatus.OK);
    }

    // 删除题库
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionBank(@PathVariable String id) {
        questionBankService.deleteQuestionBank(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}