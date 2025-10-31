package com.rps.goldloan.controller;

import com.rps.goldloan.dto.ApplicationAuditRequest;
import com.rps.goldloan.dto.ApplicationAuditResponse;
import com.rps.goldloan.service.ApplicationAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/application-audits")
public class ApplicationAuditController {

    @Autowired
    private ApplicationAuditService applicationAuditService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Application audit service is healthy");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<ApplicationAuditResponse> createApplicationAudit(@RequestBody ApplicationAuditRequest applicationAuditRequest) {
        ApplicationAuditResponse applicationAuditResponse = applicationAuditService.createApplicationAudit(applicationAuditRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationAuditResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<ApplicationAuditResponse> getApplicationAuditById(@PathVariable Long id) {
        ApplicationAuditResponse applicationAuditResponse = applicationAuditService.getApplicationAuditById(id);
        return ResponseEntity.ok(applicationAuditResponse);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ApplicationAuditResponse>> getAllApplicationAudits() {
        List<ApplicationAuditResponse> applicationAudits = applicationAuditService.getAllApplicationAudits();
        return ResponseEntity.ok(applicationAudits);
    }

    @GetMapping("/loan-application/{loanApplicationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<List<ApplicationAuditResponse>> getApplicationAuditsByLoanApplicationId(@PathVariable Long loanApplicationId) {
        List<ApplicationAuditResponse> applicationAudits = applicationAuditService.getApplicationAuditsByLoanApplicationId(loanApplicationId);
        return ResponseEntity.ok(applicationAudits);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<Void> deleteApplicationAudit(@PathVariable Long id) {
        applicationAuditService.deleteApplicationAudit(id);
        return ResponseEntity.noContent().build();
    }
}

