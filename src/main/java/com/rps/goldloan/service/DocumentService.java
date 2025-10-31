package com.rps.goldloan.service;

import com.rps.goldloan.dto.DocumentRequest;
import com.rps.goldloan.dto.DocumentResponse;
import com.rps.goldloan.dto.DocumentUpdateDto;
import com.rps.goldloan.entity.Document;
import com.rps.goldloan.entity.LoanApplication;
import com.rps.goldloan.entity.User;
import com.rps.goldloan.exception.DocumentCreationException;
import com.rps.goldloan.exception.DocumentNotFoundException;
import com.rps.goldloan.exception.DocumentUpdateException;
import com.rps.goldloan.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private LoanApplicationService loanApplicationService;

    @Autowired
    private UserService userService;

    public DocumentResponse createDocument(DocumentRequest documentRequest) {
        try {
            validateDocumentRequest(documentRequest);

            Document document = new Document();
            
            LoanApplication loanApplication = loanApplicationService.getLoanApplication(documentRequest.getLoanApplicationId());
            document.setLoanApplication(loanApplication);

            document.setType(documentRequest.getType());
            document.setFileName(documentRequest.getFileName());
            document.setFileUrl(documentRequest.getFileUrl());

            if (documentRequest.getUploadedById() != null) {
                User user = userService.getUser(documentRequest.getUploadedById());
                document.setUploadedBy(user);
            }

            document.setUploadedAt(LocalDateTime.now());
            document = documentRepository.save(document);

            return documentToDocumentResponse(document);
        } catch (IllegalArgumentException e) {
            throw new DocumentCreationException(e.getMessage());
        } catch (Exception e) {
            throw new DocumentCreationException("Error creating document: " + e.getMessage());
        }
    }

    public DocumentResponse getDocumentById(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with ID: " + documentId));
        return documentToDocumentResponse(document);
    }

    public Document getDocument(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with ID: " + documentId));
    }

    public List<DocumentResponse> getAllDocuments() {
        List<Document> documents = documentRepository.findAll();
        List<DocumentResponse> documentResponses = new ArrayList<>();
        for (Document document : documents) {
            documentResponses.add(documentToDocumentResponse(document));
        }
        return documentResponses;
    }

    public List<DocumentResponse> getDocumentsByLoanApplicationId(Long loanApplicationId) {
        if (!loanApplicationService.existsById(loanApplicationId)) {
            throw new DocumentNotFoundException("Loan application not found with ID: " + loanApplicationId);
        }
        
        List<Document> documents = documentRepository.findAll();
        List<DocumentResponse> documentResponses = new ArrayList<>();
        
        for (Document document : documents) {
            if (Objects.nonNull(document.getLoanApplication()) && 
                document.getLoanApplication().getId().equals(loanApplicationId)) {
                documentResponses.add(documentToDocumentResponse(document));
            }
        }
        return documentResponses;
    }

    public DocumentResponse updateDocument(Long documentId, DocumentUpdateDto documentUpdateDto) {
        try {
            Document document = documentRepository.findById(documentId)
                    .orElseThrow(() -> new DocumentNotFoundException("Document not found with ID: " + documentId));

            if (Objects.nonNull(documentUpdateDto.getLoanApplicationId())) {
                LoanApplication loanApplication = loanApplicationService.getLoanApplication(documentUpdateDto.getLoanApplicationId());
                document.setLoanApplication(loanApplication);
            }

            if (Objects.nonNull(documentUpdateDto.getType())) {
                document.setType(documentUpdateDto.getType());
            }

            if (Objects.nonNull(documentUpdateDto.getFileName()) && !documentUpdateDto.getFileName().isEmpty()) {
                document.setFileName(documentUpdateDto.getFileName());
            }

            if (Objects.nonNull(documentUpdateDto.getFileUrl()) && !documentUpdateDto.getFileUrl().isEmpty()) {
                document.setFileUrl(documentUpdateDto.getFileUrl());
            }

            if (Objects.nonNull(documentUpdateDto.getUploadedByUserId())) {
                User user = userService.getUser(documentUpdateDto.getUploadedByUserId());
                document.setUploadedBy(user);
            }

            document = documentRepository.save(document);

            return documentToDocumentResponse(document);
        } catch (DocumentNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DocumentUpdateException("Error updating document with ID: " + documentId + ". " + e.getMessage());
        }
    }

    public void deleteDocument(Long documentId) {
        if (!documentRepository.existsById(documentId)) {
            throw new DocumentNotFoundException("Document not found with ID: " + documentId);
        }
        documentRepository.deleteById(documentId);
    }

    private void validateDocumentRequest(DocumentRequest documentRequest) {
        if (Objects.isNull(documentRequest)) {
            throw new IllegalArgumentException("Document request cannot be null");
        }
        if (Objects.isNull(documentRequest.getLoanApplicationId())) {
            throw new IllegalArgumentException("Loan application ID is required and cannot be null");
        }
        if (Objects.isNull(documentRequest.getType())) {
            throw new IllegalArgumentException("Document type is required and cannot be null");
        }
        if (Objects.isNull(documentRequest.getFileName()) || documentRequest.getFileName().isEmpty()) {
            throw new IllegalArgumentException("File name is required and cannot be empty");
        }
        if (Objects.isNull(documentRequest.getFileUrl()) || documentRequest.getFileUrl().isEmpty()) {
            throw new IllegalArgumentException("File URL is required and cannot be empty");
        }
    }

    private DocumentResponse documentToDocumentResponse(Document document) {
        DocumentResponse documentResponse = new DocumentResponse();
        documentResponse.setId(document.getId());
        if (Objects.nonNull(document.getLoanApplication())) {
            documentResponse.setLoanApplicationId(document.getLoanApplication().getId());
        }
        documentResponse.setType(document.getType());
        documentResponse.setFileName(document.getFileName());
        documentResponse.setFileUrl(document.getFileUrl());
        if (Objects.nonNull(document.getUploadedBy())) {
            documentResponse.setUploadedById(document.getUploadedBy().getId());
        }
        documentResponse.setUploadedAt(document.getUploadedAt());
        return documentResponse;
    }
}
