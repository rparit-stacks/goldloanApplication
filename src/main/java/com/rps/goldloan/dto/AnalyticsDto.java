package com.rps.goldloan.dto;

import java.math.BigDecimal;

public class AnalyticsDto {
    private String loanId;
    private String loanName;
    private BigDecimal loanAmountAsked;
    private BigDecimal loanAmountApproved;
    private String loanDate;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String toApplicationStatus;
    //private String statusFrom;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public BigDecimal getLoanAmountAsked() {
        return loanAmountAsked;
    }

    public void setLoanAmountAsked(BigDecimal loanAmountAsked) {
        this.loanAmountAsked = loanAmountAsked;
    }

    public BigDecimal getLoanAmountApproved() {
        return loanAmountApproved;
    }

    public void setLoanAmountApproved(BigDecimal loanAmountApproved) {
        this.loanAmountApproved = loanAmountApproved;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getToApplicationStatus() {
        return toApplicationStatus;
    }

    public void setToApplicationStatus(String toApplicationStatus) {
        this.toApplicationStatus = toApplicationStatus;
    }

//    public String getStatusFrom() {
//        return statusFrom;
//    }
//
//    public void setStatusFrom(String statusFrom) {
//        this.statusFrom = statusFrom;
//    }
}
