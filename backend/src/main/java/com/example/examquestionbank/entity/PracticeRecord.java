package com.example.examquestionbank.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "practice_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "question_bank_id", nullable = false)
    private String questionBankId;

    @Column(name = "practice_type", nullable = false)
    private String practiceType; // 练习类型：顺序练习、随机练习、模拟考试

    @Column(name = "total_questions", nullable = false)
    private Integer totalQuestions;

    @Column(name = "correct_count", nullable = false)
    private Integer correctCount;

    @Column(name = "wrong_count", nullable = false)
    private Integer wrongCount;

    @Column(name = "unanswered_count", nullable = false)
    private Integer unansweredCount;

    @Column(name = "score")
    private Double score;

    @Column(name = "duration", nullable = false)
    private Integer duration; // 练习时长（秒）

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
