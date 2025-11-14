package com.rps.goldloan.service;


import com.rps.goldloan.dto.AnalyticsDto;
import com.rps.goldloan.entity.LoanApplication;
import com.rps.goldloan.enums.ApplicationStatus;
import com.rps.goldloan.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {
    @Autowired
    LoanApplicationRepository loanApplicationService;

    public List<AnalyticsDto> getAllIncompleteLoans(){
        List<LoanApplication> allIncompletedLoans = loanApplicationService.getAllIncomplete();
        return convertTOListDto(allIncompletedLoans);
    }

    private List<AnalyticsDto> convertTOListDto(List<LoanApplication> applicationsList) {
        List<AnalyticsDto> analyticsDtoList = new ArrayList<>();
        for (LoanApplication application : applicationsList) {
            AnalyticsDto analyticsDto = new AnalyticsDto();
            analyticsDto.setCustomerEmail(application.getCustomer().getEmail());
            analyticsDto.setCustomerId(application.getCustomer().getEmail());
            analyticsDto.setCustomerName(application.getCustomer().getFirstName());
            analyticsDto.setCustomerPhone(application.getCustomer().getMobileNumber());
            analyticsDto.setLoanAmountAsked(application.getRequestedAmount());
            analyticsDto.setLoanAmountApproved(application.getApprovedAmount());
            analyticsDto.setLoanId(application.getId().toString());
            analyticsDto.setLoanName(application.getTerm().getName());
            analyticsDto.setLoanDate(application.getCreatedAt().toString());
            analyticsDto.setToApplicationStatus(application.getStatus().toString());
            analyticsDtoList.add(analyticsDto);
        }
        return analyticsDtoList;

    }

    public List<AnalyticsDto> getAllPendingLoans() {
        List<LoanApplication> allIncompletedLoans = loanApplicationService.getAllPending();
        return convertTOListDto(allIncompletedLoans);
    }

    public List<AnalyticsDto> getAllApprovedLoans() {
        List<LoanApplication> allIncompletedLoans = loanApplicationService.getAllApproved();
        return convertTOListDto(allIncompletedLoans);
    }

    public List<AnalyticsDto> getAllRejectedLoans() {
        List<LoanApplication> allIncompletedLoans = loanApplicationService.getAllRejected();
        return convertTOListDto(allIncompletedLoans);
    }

    public List<AnalyticsDto> getAllDisburedLoans() {
        List<LoanApplication> allIncompletedLoans = loanApplicationService.getAllDisbursed();
        return convertTOListDto(allIncompletedLoans);
    }

    public List<AnalyticsDto> getAllClosedLoans() {
        List<LoanApplication> allIncompletedLoans = loanApplicationService.getAllClosed();
        return convertTOListDto(allIncompletedLoans);
    }

    public List<AnalyticsDto> getAllCreatedLoans() {
        List<LoanApplication> allIncompletedLoans = loanApplicationService.getAllCreated();
        return convertTOListDto(allIncompletedLoans);
    }

    public List<AnalyticsDto> getAllAppliedOnlyLoans() {
        List<LoanApplication> allIncompletedLoans = loanApplicationService.getALlApplied();
        return convertTOListDto(allIncompletedLoans);
    }
}
