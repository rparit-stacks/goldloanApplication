package com.rps.goldloan.dto;

import java.math.BigDecimal;
import java.util.List;

public class LoanApplicationRequest {

    private Long customerId;
    private Long branchId;
    private BigDecimal requestedAmount;
    private Long termId;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private String notes;
    private List<Long> goldDetailId;
    private List<Long> documentId;

    public List<Long> getGoldDetailId() {
        return goldDetailId;
    }

    public void setGoldDetailId(List<Long> goldDetailId) {
        this.goldDetailId = goldDetailId;
    }

    public List<Long> getDocumentId() {
        return documentId;
    }

    public void setDocumentId(List<Long> documentId) {
        this.documentId = documentId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public Long getTermId() {
        return termId;
    }

    public void setTermId(Long termId) {
        this.termId = termId;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
