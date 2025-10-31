package com.rps.goldloan.controller;

import com.rps.goldloan.dto.ValuationReportRequest;
import com.rps.goldloan.dto.ValuationReportResponse;
import com.rps.goldloan.service.ValuationReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/valuation-reports")
public class ValuationReportController {

    @Autowired
    private ValuationReportService valuationReportService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Valuation report service is healthy");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<ValuationReportResponse> createValuationReport(@RequestBody ValuationReportRequest valuationReportRequest) {
        ValuationReportResponse valuationReportResponse = valuationReportService.createValuationReport(valuationReportRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(valuationReportResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<ValuationReportResponse> getValuationReportById(@PathVariable Long id) {
        ValuationReportResponse valuationReportResponse = valuationReportService.getValuationReportById(id);
        return ResponseEntity.ok(valuationReportResponse);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<List<ValuationReportResponse>> getAllValuationReports() {
        List<ValuationReportResponse> valuationReports = valuationReportService.getAllValuationReports();
        return ResponseEntity.ok(valuationReports);
    }

    @GetMapping("/loan-application/{loanApplicationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<ValuationReportResponse> getValuationReportByLoanApplicationId(@PathVariable Long loanApplicationId) {
        ValuationReportResponse valuationReportResponse = valuationReportService.getValuationReportByLoanApplicationId(loanApplicationId);
        return ResponseEntity.ok(valuationReportResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<ValuationReportResponse> updateValuationReport(@PathVariable Long id, @RequestBody ValuationReportRequest valuationReportRequest) {
        ValuationReportResponse valuationReportResponse = valuationReportService.updateValuationReport(id, valuationReportRequest);
        return ResponseEntity.ok(valuationReportResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<Void> deleteValuationReport(@PathVariable Long id) {
        valuationReportService.deleteValuationReport(id);
        return ResponseEntity.noContent().build();
    }
}

