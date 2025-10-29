package com.rps.goldloan.dto;

import java.math.BigDecimal;

public class GoldDetailRequest {

    private Long loanApplicationId;
    private String description;
    private BigDecimal weightGrams;
    private BigDecimal purity;
    private BigDecimal marketRatePerGram;
    private String photoUrl;

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getWeightGrams() {
        return weightGrams;
    }

    public void setWeightGrams(BigDecimal weightGrams) {
        this.weightGrams = weightGrams;
    }

    public BigDecimal getPurity() {
        return purity;
    }

    public void setPurity(BigDecimal purity) {
        this.purity = purity;
    }

    public BigDecimal getMarketRatePerGram() {
        return marketRatePerGram;
    }

    public void setMarketRatePerGram(BigDecimal marketRatePerGram) {
        this.marketRatePerGram = marketRatePerGram;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
