package com.rps.goldloan.controller;

import com.rps.goldloan.dto.DocumentRequest;
import com.rps.goldloan.dto.DocumentResponse;
import com.rps.goldloan.dto.DocumentUpdateDto;
import com.rps.goldloan.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Document service is healthy");
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<DocumentResponse> createDocument(@RequestBody DocumentRequest documentRequest) {
        DocumentResponse documentResponse = documentService.createDocument(documentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(documentResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<DocumentResponse> getDocumentById(@PathVariable Long id) {
        DocumentResponse documentResponse = documentService.getDocumentById(id);
        return ResponseEntity.ok(documentResponse);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<List<DocumentResponse>> getAllDocuments() {
        List<DocumentResponse> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/loan-application/{loanApplicationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByLoanApplicationId(@PathVariable Long loanApplicationId) {
        List<DocumentResponse> documents = documentService.getDocumentsByLoanApplicationId(loanApplicationId);
        return ResponseEntity.ok(documents);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<DocumentResponse> updateDocument(@PathVariable Long id, @RequestBody DocumentUpdateDto documentUpdateDto) {
        DocumentResponse documentResponse = documentService.updateDocument(id, documentUpdateDto);
        return ResponseEntity.ok(documentResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OFFICER', 'AUDITOR')")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}

