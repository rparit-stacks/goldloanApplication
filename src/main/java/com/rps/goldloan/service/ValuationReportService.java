package com.rps.goldloan.service;

import com.rps.goldloan.dto.ValuationReportRequest;
import com.rps.goldloan.dto.ValuationReportResponse;
import com.rps.goldloan.entity.LoanApplication;
import com.rps.goldloan.entity.ValuationReport;
import com.rps.goldloan.exception.ValuationReportCreationException;
import com.rps.goldloan.exception.ValuationReportNotFoundException;
import com.rps.goldloan.exception.ValuationReportUpdateException;
import com.rps.goldloan.repository.ValuationReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ValuationReportService {

    @Autowired
    private ValuationReportRepository valuationReportRepository;

    @Autowired
    private LoanApplicationService loanApplicationService;

    public ValuationReportResponse createValuationReport(ValuationReportRequest valuationReportRequest) {
        try {
            validateValuationReportRequest(valuationReportRequest);

            ValuationReport valuationReport = new ValuationReport();
            
            LoanApplication loanApplication = loanApplicationService.getLoanApplication(valuationReportRequest.getLoanApplicationId());
            valuationReport.setLoanApplication(loanApplication);

            valuationReport.setEvaluatorName(valuationReportRequest.getEvaluatorName());
            valuationReport.setTotalAssessedValue(valuationReportRequest.getTotalAssessedValue());
            valuationReport.setComments(valuationReportRequest.getComments());
            valuationReport.setDetailedBreakdown(valuationReportRequest.getDetailedBreakdown());
            valuationReport.setEvaluatedAt(LocalDateTime.now());

            valuationReport = valuationReportRepository.save(valuationReport);
            return valuationReportToResponse(valuationReport);
        } catch (IllegalArgumentException e) {
            throw new ValuationReportCreationException(e.getMessage());
        } catch (Exception e) {
            throw new ValuationReportCreationException("Error creating valuation report: " + e.getMessage());
        }
    }

    public ValuationReportResponse getValuationReportById(Long id) {
        ValuationReport valuationReport = valuationReportRepository.findById(id)
                .orElseThrow(() -> new ValuationReportNotFoundException("Valuation report not found with ID: " + id));
        return valuationReportToResponse(valuationReport);
    }

    public ValuationReportResponse getValuationReportByLoanApplicationId(Long loanApplicationId) {
        if (!loanApplicationService.existsById(loanApplicationId)) {
            throw new ValuationReportNotFoundException("Loan application not found with ID: " + loanApplicationId);
        }
        
        List<ValuationReport> valuationReports = valuationReportRepository.findAll();
        
        for (ValuationReport valuationReport : valuationReports) {
            if (Objects.nonNull(valuationReport.getLoanApplication()) && 
                valuationReport.getLoanApplication().getId().equals(loanApplicationId)) {
                return valuationReportToResponse(valuationReport);
            }
        }
        throw new ValuationReportNotFoundException("Valuation report not found for loan application ID: " + loanApplicationId);
    }

    public List<ValuationReportResponse> getAllValuationReports() {
        List<ValuationReport> valuationReports = valuationReportRepository.findAll();
        List<ValuationReportResponse> valuationReportResponses = new ArrayList<>();
        for (ValuationReport valuationReport : valuationReports) {
            valuationReportResponses.add(valuationReportToResponse(valuationReport));
        }
        return valuationReportResponses;
    }

    public ValuationReportResponse updateValuationReport(Long id, ValuationReportRequest valuationReportRequest) {
        try {
            ValuationReport valuationReport = valuationReportRepository.findById(id)
                    .orElseThrow(() -> new ValuationReportNotFoundException("Valuation report not found with ID: " + id));

            if (Objects.nonNull(valuationReportRequest.getLoanApplicationId())) {
                LoanApplication loanApplication = loanApplicationService.getLoanApplication(valuationReportRequest.getLoanApplicationId());
                valuationReport.setLoanApplication(loanApplication);
            }

            if (Objects.nonNull(valuationReportRequest.getEvaluatorName()) && !valuationReportRequest.getEvaluatorName().isEmpty()) {
                valuationReport.setEvaluatorName(valuationReportRequest.getEvaluatorName());
            }

            if (Objects.nonNull(valuationReportRequest.getTotalAssessedValue())) {
                valuationReport.setTotalAssessedValue(valuationReportRequest.getTotalAssessedValue());
            }

            if (Objects.nonNull(valuationReportRequest.getComments()) && !valuationReportRequest.getComments().isEmpty()) {
                valuationReport.setComments(valuationReportRequest.getComments());
            }

            if (Objects.nonNull(valuationReportRequest.getDetailedBreakdown()) && !valuationReportRequest.getDetailedBreakdown().isEmpty()) {
                valuationReport.setDetailedBreakdown(valuationReportRequest.getDetailedBreakdown());
            }

            valuationReport.setEvaluatedAt(LocalDateTime.now());
            valuationReport = valuationReportRepository.save(valuationReport);

            return valuationReportToResponse(valuationReport);
        } catch (ValuationReportNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ValuationReportUpdateException("Error updating valuation report with ID: " + id + ". " + e.getMessage());
        }
    }

    public void deleteValuationReport(Long id) {
        if (!valuationReportRepository.existsById(id)) {
            throw new ValuationReportNotFoundException("Valuation report not found with ID: " + id);
        }
        valuationReportRepository.deleteById(id);
    }

    private void validateValuationReportRequest(ValuationReportRequest valuationReportRequest) {
        if (Objects.isNull(valuationReportRequest)) {
            throw new IllegalArgumentException("Valuation report request cannot be null");
        }
        if (Objects.isNull(valuationReportRequest.getLoanApplicationId())) {
            throw new IllegalArgumentException("Loan application ID is required and cannot be null");
        }
        if (Objects.isNull(valuationReportRequest.getEvaluatorName()) || valuationReportRequest.getEvaluatorName().isEmpty()) {
            throw new IllegalArgumentException("Evaluator name is required and cannot be empty");
        }
        if (Objects.isNull(valuationReportRequest.getTotalAssessedValue()) || 
            valuationReportRequest.getTotalAssessedValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total assessed value is required and must be greater than zero");
        }
    }

    private ValuationReportResponse valuationReportToResponse(ValuationReport valuationReport) {
        ValuationReportResponse response = new ValuationReportResponse();
        response.setId(valuationReport.getId());
        if (Objects.nonNull(valuationReport.getLoanApplication())) {
            response.setLoanApplicationId(valuationReport.getLoanApplication().getId());
        }
        response.setEvaluatorName(valuationReport.getEvaluatorName());
        response.setEvaluatedAt(valuationReport.getEvaluatedAt());
        response.setTotalAssessedValue(valuationReport.getTotalAssessedValue());
        response.setComments(valuationReport.getComments());
        response.setDetailedBreakdown(valuationReport.getDetailedBreakdown());
        return response;
    }
}
