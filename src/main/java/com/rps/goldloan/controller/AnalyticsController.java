package com.rps.goldloan.controller;


import com.rps.goldloan.dto.AnalyticsDto;
import com.rps.goldloan.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;


    @GetMapping("/pending-loans")
    public List<AnalyticsDto> pendingLoans() {
        return analyticsService.getAllPendingLoans();
    }

    @GetMapping("/approved-loans")
    public List<AnalyticsDto> approved() {
        return analyticsService.getAllApprovedLoans();
    }

    @GetMapping("/verified-loans")
    public List<AnalyticsDto> rejected() {
        return analyticsService.getAllRejectedLoans();
    }

    @GetMapping("/disbursed-loans")
    public List<AnalyticsDto> disbursed() {
        return analyticsService.getAllDisburedLoans();
    }

    @GetMapping("/closed-loans")
    public List<AnalyticsDto> closed() {
        return analyticsService.getAllClosedLoans();
    }

    @GetMapping("/created-loans")
    public List<AnalyticsDto> created() {
        return analyticsService.getAllCreatedLoans();
    }



    @GetMapping("/incomplete-loans")
    public List<AnalyticsDto> incomplete() {
        return analyticsService.getAllIncompleteLoans();
    }

    @GetMapping("/applied-loans")
    public List<AnalyticsDto> applied() {
        return analyticsService.getAllAppliedOnlyLoans();
    }
}
