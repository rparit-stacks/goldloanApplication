package com.rps.goldloan.controller;

import com.rps.goldloan.dto.LoanApplicationRequest;
import com.rps.goldloan.dto.LoanApplicationResponse;
import com.rps.goldloan.dto.LoanApplicationUpdateDto;
import com.rps.goldloan.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan-applications")
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Loan application service is healthy");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER')")
    public ResponseEntity<LoanApplicationResponse> createLoanApplication(@RequestBody LoanApplicationRequest loanApplicationRequest) {
        LoanApplicationResponse loanApplicationResponse = loanApplicationService.createLoanApplication(loanApplicationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(loanApplicationResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER')")
    public ResponseEntity<LoanApplicationResponse> getLoanApplicationById(@PathVariable Long id) {
        LoanApplicationResponse loanApplicationResponse = loanApplicationService.getLoanApplicationById(id);
        return ResponseEntity.ok(loanApplicationResponse);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER')")
    public ResponseEntity<List<LoanApplicationResponse>> getAllLoanApplications() {
        List<LoanApplicationResponse> loanApplications = loanApplicationService.getAllLoanApplications();
        return ResponseEntity.ok(loanApplications);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER')")
    public ResponseEntity<LoanApplicationResponse> updateLoanApplication(@PathVariable Long id, @RequestBody LoanApplicationUpdateDto loanApplicationUpdateDto) {
        LoanApplicationResponse loanApplicationResponse = loanApplicationService.updateLoanApplication(id, loanApplicationUpdateDto);
        return ResponseEntity.ok(loanApplicationResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER')")
    public ResponseEntity<Void> deleteLoanApplication(@PathVariable Long id) {
        loanApplicationService.deleteLoanApplication(id);
        return ResponseEntity.noContent().build();
    }
}

