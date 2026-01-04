package com.example.examquestionbank.repository;

import com.example.examquestionbank.entity.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, String> {
    List<QuestionBank> findByCategory(String category);
    List<QuestionBank> findByTitleContainingIgnoreCase(String keyword);
}