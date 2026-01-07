package com.example.exambankadmin.repository;

import com.example.exambankadmin.entity.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long> {
    List<QuestionBank> findByCategory(String category);
    List<QuestionBank> findByTitleContainingIgnoreCase(String keyword);
    List<QuestionBank> findByTitleContainingIgnoreCaseAndCategory(String keyword, String category);
}