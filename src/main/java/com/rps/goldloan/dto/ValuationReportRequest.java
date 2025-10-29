package com.rps.goldloan.dto;

import java.math.BigDecimal;

public class ValuationReportRequest {

    private Long loanApplicationId;
    private String evaluatorName;
    private BigDecimal totalAssessedValue;
    private String comments;
    private String detailedBreakdown;

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public String getEvaluatorName() {
        return evaluatorName;
    }

    public void setEvaluatorName(String evaluatorName) {
        this.evaluatorName = evaluatorName;
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
