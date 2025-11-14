package com.rps.goldloan.dto;

import com.rps.goldloan.entity.GoldDetail;

import java.util.List;

public class MultiGoldRequest {

    private long id;
    private List<GoldDetail> goldDetails;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<GoldDetail> getGoldDetails() {
        return goldDetails;
    }

    public void setGoldDetails(List<GoldDetail> goldDetails) {
        this.goldDetails = goldDetails;
    }
}
