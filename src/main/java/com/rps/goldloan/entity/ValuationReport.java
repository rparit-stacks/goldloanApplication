package com.rps.goldloan.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "valuation_report")
public class ValuationReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "loan_application_id", nullable = false)
    private LoanApplication loanApplication;

    private String evaluatorName;

    private LocalDateTime evaluatedAt;

    private BigDecimal totalAssessedValue;

    @Lob
    private String comments;

    @Lob
    private String detailedBreakdown;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanApplication getLoanApplication() {
        return loanApplication;
    }

    public void setLoanApplication(LoanApplication loanApplication) {
        this.loanApplication = loanApplication;
    }

    public String getEvaluatorName() {
        return evaluatorName;
    }

    public void setEvaluatorName(String evaluatorName) {
        this.evaluatorName = evaluatorName;
    }

    public LocalDateTime getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(LocalDateTime evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }

    public BigDecimal getTotalAssessedValue() {
        return totalAssessedValue;
    }

    public void setTotalAssessedValue(BigDecimal totalAssessedValue) {
        this.totalAssessedValue = totalAssessedValue;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDetailedBreakdown() {
        return detailedBreakdown;
    }

    public void setDetailedBreakdown(String detailedBreakdown) {
        this.detailedBreakdown = detailedBreakdown;
    }
}
