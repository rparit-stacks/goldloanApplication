package com.rps.goldloan.controller;

import com.rps.goldloan.dto.LoanTermRequest;
import com.rps.goldloan.dto.LoanTermResponse;
import com.rps.goldloan.dto.LoanTermUpdateDto;
import com.rps.goldloan.service.LoanTermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan-terms")
public class LoanTermController {

    @Autowired
    private LoanTermService loanTermService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Loan term service is healthy");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<LoanTermResponse> createLoanTerm(@RequestBody LoanTermRequest loanTermRequest) {
        LoanTermResponse loanTermResponse = loanTermService.createLoanTerm(loanTermRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(loanTermResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<LoanTermResponse> getLoanTermById(@PathVariable Long id) {
        LoanTermResponse loanTermResponse = loanTermService.getLoanTermById(id);
        return ResponseEntity.ok(loanTermResponse);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<LoanTermResponse>> getAllLoanTerms() {
        List<LoanTermResponse> loanTerms = loanTermService.getAllLoanTerms();
        return ResponseEntity.ok(loanTerms);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<LoanTermResponse> updateLoanTerm(@PathVariable Long id, @RequestBody LoanTermUpdateDto loanTermUpdateDto) {
        LoanTermResponse loanTermResponse = loanTermService.updateLoanTerm(id, loanTermUpdateDto);
        return ResponseEntity.ok(loanTermResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteLoanTerm(@PathVariable Long id) {
        loanTermService.deleteLoanTerm(id);
        return ResponseEntity.noContent().build();
    }
}

