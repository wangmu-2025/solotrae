package com.example.exambankadmin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "题目内容不能为空")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @NotBlank(message = "选项A不能为空")
    @Column(name = "option_a", nullable = false)
    private String optionA;

    @NotBlank(message = "选项B不能为空")
    @Column(name = "option_b", nullable = false)
    private String optionB;

    @Column(name = "option_c")
    private String optionC;

    @Column(name = "option_d")
    private String optionD;

    @NotBlank(message = "题目答案不能为空")
    @Column(nullable = false)
    private String answer;

    @NotBlank(message = "题目类型不能为空")
    @Column(nullable = false)
    private String type;

    @NotNull(message = "题目难度不能为空")
    @Column(nullable = false)
    private String difficulty;

    @NotNull(message = "题目分值不能为空")
    @Column(nullable = false)
    private Double score;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String analysis;

    @Column(name = "tags")
    private String tags;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 与QuestionBank的多对一关系
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_bank_id", nullable = false)
    private QuestionBank questionBank;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}