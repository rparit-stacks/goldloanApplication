package com.rps.goldloan.dto;

import java.math.BigDecimal;

public class LoanTermUpdateDto {

    private String name;
    private Integer maxLTVPercent;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private BigDecimal interestRateAnnual;
    private Integer maxTenureMonths;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxLTVPercent() {
        return maxLTVPercent;
    }

    public void setMaxLTVPercent(Integer maxLTVPercent) {
        this.maxLTVPercent = maxLTVPercent;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public BigDecimal getInterestRateAnnual() {
        return interestRateAnnual;
    }

    public void setInterestRateAnnual(BigDecimal interestRateAnnual) {
        this.interestRateAnnual = interestRateAnnual;
    }

    public Integer getMaxTenureMonths() {
        return maxTenureMonths;
    }

    public void setMaxTenureMonths(Integer maxTenureMonths) {
        this.maxTenureMonths = maxTenureMonths;
    }
}

