package com.rps.goldloan.controller;

import com.rps.goldloan.dto.GoldDetailRequest;
import com.rps.goldloan.dto.GoldDetailResponse;
import com.rps.goldloan.dto.GoldDetailUpdateDto;
import com.rps.goldloan.service.GoldDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gold-details")
public class GoldDetailController {

    @Autowired
    private GoldDetailService goldDetailService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Gold detail service is healthy");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<GoldDetailResponse> createGoldDetail(@RequestBody GoldDetailRequest goldDetailRequest) {
        GoldDetailResponse goldDetailResponse = goldDetailService.createGoldDetail(goldDetailRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(goldDetailResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<GoldDetailResponse> getGoldDetailById(@PathVariable Long id) {
        GoldDetailResponse goldDetailResponse = goldDetailService.getGoldDetailById(id);
        return ResponseEntity.ok(goldDetailResponse);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<List<GoldDetailResponse>> getAllGoldDetails() {
        List<GoldDetailResponse> goldDetails = goldDetailService.getAllGoldDetails();
        return ResponseEntity.ok(goldDetails);
    }



    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<GoldDetailResponse> updateGoldDetail(@PathVariable Long id, @RequestBody GoldDetailUpdateDto goldDetailUpdateDto) {
        GoldDetailResponse goldDetailResponse = goldDetailService.updateGoldDetail(id, goldDetailUpdateDto);
        return ResponseEntity.ok(goldDetailResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<Void> deleteGoldDetail(@PathVariable Long id) {
        goldDetailService.deleteGoldDetail(id);
        return ResponseEntity.noContent().build();
    }
}

