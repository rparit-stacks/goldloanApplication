package com.rps.goldloan.controller;

import com.rps.goldloan.dto.BranchRequest;
import com.rps.goldloan.dto.BranchResponse;
import com.rps.goldloan.dto.BranchUpdateDto;
import com.rps.goldloan.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Branch service is healthy");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<BranchResponse> createBranch(@RequestBody BranchRequest branchRequest) {
        BranchResponse branchResponse = branchService.createBranch(branchRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(branchResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<BranchResponse> getBranchById(@PathVariable Long id) {
        BranchResponse branchResponse = branchService.getBranchById(id);
        return ResponseEntity.ok(branchResponse);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<BranchResponse>> getAllBranches() {
        List<BranchResponse> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<BranchResponse> updateBranch(@PathVariable Long id, @RequestBody BranchUpdateDto branchUpdateDto) {
        BranchResponse branchResponse = branchService.updateBranch(id, branchUpdateDto);
        return ResponseEntity.ok(branchResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
}

