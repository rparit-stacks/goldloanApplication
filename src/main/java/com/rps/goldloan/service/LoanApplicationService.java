package com.rps.goldloan.service;

import com.rps.goldloan.dto.*;
import com.rps.goldloan.entity.*;
import com.rps.goldloan.enums.ApplicationStatus;
import com.rps.goldloan.exception.LoanApplicationCreationException;
import com.rps.goldloan.exception.LoanApplicationNotFoundException;
import com.rps.goldloan.exception.LoanApplicationUpdateException;
import com.rps.goldloan.repository.LoanApplicationRepository;
import com.rps.goldloan.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private LoanTermService loanTermService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    @Lazy
    private GoldDetailService goldDetailService;

    @Autowired
    @Lazy
    private DocumentService documentService;

    @Autowired
    @Lazy
    private ApplicationAuditService applicationAuditService;

    public LoanApplicationResponse createLoanApplication(LoanApplicationRequest loanApplicationRequest) {
        try {
            validateLoanApplicationRequest(loanApplicationRequest);

            LoanApplication loanApplication = new LoanApplication();

            loanApplication.setCustomer(customerService.getCustomer(loanApplicationRequest.getCustomerId()));
            loanApplication.setBranch(branchService.getBranch(loanApplicationRequest.getBranchId()));
            loanApplication.setRequestedAmount(loanApplicationRequest.getRequestedAmount());

            if (loanApplicationRequest.getTermId() != null) {
                LoanTerm loanTerm = loanTermService.getLoanTerm(loanApplicationRequest.getTermId());
                loanApplication.setTerm(loanTerm);
            }

            loanApplication.setInterestRate(loanApplicationRequest.getInterestRate());
            loanApplication.setTenureMonths(loanApplicationRequest.getTenureMonths());
            loanApplication.setNotes(loanApplicationRequest.getNotes());
            loanApplication.setStatus(ApplicationStatus.APPLIED);
            loanApplication.setStage("IN REVIEW");
            loanApplication.setApplicationNumber(generateApplicationNumber());

            List<GoldDetail> goldDetails = new ArrayList<>();
            for (Long goldDetailId : loanApplicationRequest.getGoldDetailId()) {
                GoldDetail goldDetail = goldDetailService.getGoldDetail(goldDetailId);
                goldDetails.add(goldDetail);
            }
            //when we create a loan application the gold detail auto assigned to loan application
            // and loan applcate direct assign with gold



            List<Document> documentList = new ArrayList<>();
            for (Long documentId : loanApplicationRequest.getDocumentId()) {
                Document document = documentService.getDocument(documentId);
                documentList.add(document);
            }

            loanApplication.setDocuments(documentList);
            loanApplication.setGoldDetails(goldDetails);


            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                User currentUser = userService.getUser(userDetails.getId());
                loanApplication.setCreatedBy(currentUser);
                loanApplication.setAssignedTo(currentUser);
            }

            loanApplication.setCreatedAt(LocalDateTime.now());
            loanApplication.setUpdatedAt(LocalDateTime.now());

            loanApplication = loanApplicationRepository.save(loanApplication);

            for(GoldDetail goldDetail : loanApplication.getGoldDetails()){
                goldDetailService.assignLoanApplication(goldDetail.getId(), loanApplication);
            }

            for(Document document : loanApplication.getDocuments()) {
                documentService.assignLoanApplication(document.getId(), loanApplication);
            }

            //Audit trail
            createCreationAudit(loanApplication);

            emailService.sendLoanApplicationEmail(loanApplication);
            return loanApplicationToResponse(loanApplication);
        } catch (IllegalArgumentException e) {
            throw new LoanApplicationCreationException(e.getMessage(), e);
        } catch (Exception e) {
            throw new LoanApplicationCreationException("Error creating loan application: " + e.getMessage(), e);
        }
    }

    private void createCreationAudit(LoanApplication loanApplication) {
        ApplicationAuditRequest applicationAuditRequest = new ApplicationAuditRequest();
        applicationAuditRequest.setLoanApplicationId(loanApplication.getId());
        applicationAuditRequest.setAction("Loan Application Created");
        applicationAuditRequest.setFromStatus(null);
        applicationAuditRequest.setToStatus(ApplicationStatus.APPLIED);
        applicationAuditRequest.setPerformedById(loanApplication.getCreatedBy().getId());
        applicationAuditRequest.setComment("New loan application created and submitted for review.");
        applicationAuditService.createApplicationAudit(applicationAuditRequest);
    }

    public LoanApplicationResponse getLoanApplicationById(Long loanApplicationId) {
        LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Loan application not found with ID: " + loanApplicationId));
        return loanApplicationToResponse(loanApplication);
    }

    public LoanApplication getLoanApplication(Long loanApplicationId) {
        return loanApplicationRepository.findById(loanApplicationId)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Loan application not found with ID: " + loanApplicationId));
    }

    public boolean existsById(Long loanApplicationId) {
        return loanApplicationRepository.existsById(loanApplicationId);
    }

    public List<LoanApplicationResponse> getAllLoanApplications() {
        List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
        return loanApplications.stream()
                .map(this::loanApplicationToResponse)
                .collect(Collectors.toList());
    }

    public LoanApplicationResponse updateLoanApplication(Long loanApplicationId, LoanApplicationUpdateDto loanApplicationUpdateDto) {
        try {
            LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId)
                    .orElseThrow(() -> new LoanApplicationNotFoundException("Loan application not found with ID: " + loanApplicationId));

            // --- Prepare for Audit ---
            StringBuilder commentBuilder = new StringBuilder("Application updated: ");
            ApplicationStatus fromStatus = loanApplication.getStatus();
            boolean isStatusChanged = false;

            if (Objects.nonNull(loanApplicationUpdateDto.getCustomerId())) {
                loanApplication.setCustomer(customerService.getCustomer(loanApplicationUpdateDto.getCustomerId()));
                commentBuilder.append("Customer changed. ");
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getBranchId())) {
                loanApplication.setBranch(branchService.getBranch(loanApplicationUpdateDto.getBranchId()));
                commentBuilder.append("Branch changed. ");
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getRequestedAmount())) {
                loanApplication.setRequestedAmount(loanApplicationUpdateDto.getRequestedAmount());
                commentBuilder.append("Requested amount updated. ");
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getApprovedAmount())) {
                loanApplication.setApprovedAmount(loanApplicationUpdateDto.getApprovedAmount());
                commentBuilder.append("Approved amount set. ");
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getStatus())) {
                if (loanApplication.getStatus() != loanApplicationUpdateDto.getStatus()) {
                    isStatusChanged = true;
                    commentBuilder.append("Status changed from ").append(fromStatus).append(" to ").append(loanApplicationUpdateDto.getStatus()).append(". ");
                    loanApplication.setStatus(loanApplicationUpdateDto.getStatus());
                }
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getStage()) && !loanApplicationUpdateDto.getStage().isEmpty()) {
                loanApplication.setStage(loanApplicationUpdateDto.getStage());
                commentBuilder.append("Stage updated to '").append(loanApplicationUpdateDto.getStage()).append("'. ");
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getTermId())) {
                LoanTerm loanTerm = loanTermService.getLoanTerm(loanApplicationUpdateDto.getTermId());
                loanApplication.setTerm(loanTerm);
                commentBuilder.append("Loan term updated. ");
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getInterestRate())) {
                loanApplication.setInterestRate(loanApplicationUpdateDto.getInterestRate());
                commentBuilder.append("Interest rate updated. ");
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getTenureMonths())) {
                loanApplication.setTenureMonths(loanApplicationUpdateDto.getTenureMonths());
                commentBuilder.append("Tenure updated. ");
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getCreatedBy())) {
                loanApplication.setCreatedBy(userService.getUser(loanApplicationUpdateDto.getCreatedBy()));
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getAssignedTo())) {
                User newAssignee = userService.getUser(loanApplicationUpdateDto.getAssignedTo());
                loanApplication.setAssignedTo(newAssignee);
                commentBuilder.append("Assigned to user '").append(newAssignee.getUsername()).append("'. ");
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getNotes()) && !loanApplicationUpdateDto.getNotes().isEmpty()) {
                loanApplication.setNotes(loanApplicationUpdateDto.getNotes());
                commentBuilder.append("Notes updated. ");
            }

            loanApplication.setUpdatedAt(LocalDateTime.now());
            loanApplication = loanApplicationRepository.save(loanApplication);

            // --- Create Audit Record for Update ---
            ApplicationAuditRequest applicationAuditRequest = new ApplicationAuditRequest();
            applicationAuditRequest.setLoanApplicationId(loanApplication.getId());
            applicationAuditRequest.setAction("Loan Application Updated");
            applicationAuditRequest.setComment(commentBuilder.toString());

            if (isStatusChanged) {
                applicationAuditRequest.setFromStatus(fromStatus);
                applicationAuditRequest.setToStatus(loanApplication.getStatus());
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                User currentUser = userService.getUser(userDetails.getId());
                applicationAuditRequest.setPerformedById(currentUser.getId());
            }
            applicationAuditService.createApplicationAudit(applicationAuditRequest);

            return loanApplicationToResponse(loanApplication);
        } catch (LoanApplicationNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new LoanApplicationUpdateException("Error updating loan application with ID: " + loanApplicationId + ". " + e.getMessage(), e);
        }
    }

    public void deleteLoanApplication(Long loanApplicationId) {
        if (!loanApplicationRepository.existsById(loanApplicationId)) {
            throw new LoanApplicationNotFoundException("Loan application not found with ID: " + loanApplicationId);
        }
        loanApplicationRepository.deleteById(loanApplicationId);
    }

    private void validateLoanApplicationRequest(LoanApplicationRequest loanApplicationRequest) {
        if (Objects.isNull(loanApplicationRequest)) {
            throw new IllegalArgumentException("Loan application request cannot be null");
        }
        if (Objects.isNull(loanApplicationRequest.getCustomerId())) {
            throw new IllegalArgumentException("Customer ID is required and cannot be null");
        }
        if (Objects.isNull(loanApplicationRequest.getBranchId())) {
            throw new IllegalArgumentException("Branch ID is required and cannot be null");
        }

        if (Objects.isNull(loanApplicationRequest.getGoldDetailId())) {
            throw new IllegalArgumentException("Gold detail ID is required and cannot be null");
        }

        if (Objects.isNull(loanApplicationRequest.getDocumentId())) {
            throw new IllegalArgumentException("Document ID is required and cannot be null");

        }



        if (Objects.isNull(loanApplicationRequest.getRequestedAmount()) || loanApplicationRequest.getRequestedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Requested amount is required and must be greater than zero");
        }
        if (Objects.nonNull(loanApplicationRequest.getInterestRate()) &&
            loanApplicationRequest.getInterestRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Interest rate must be greater than zero");
        }
        if (Objects.nonNull(loanApplicationRequest.getTenureMonths()) &&
            loanApplicationRequest.getTenureMonths() <= 0) {
            throw new IllegalArgumentException("Tenure months must be greater than zero");
        }

    }

    private String generateApplicationNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "APP-" + timestamp;
    }

    private LoanApplicationResponse loanApplicationToResponse(LoanApplication loanApplication) {
        LoanApplicationResponse response = new LoanApplicationResponse();
        response.setId(loanApplication.getId());
        response.setApplicationNumber(loanApplication.getApplicationNumber());

        response.setCustomer(customerService.getCustomerById(loanApplication.getCustomer().getId()));
        response.setBranch(branchService.getBranchById(loanApplication.getBranch().getId()));


        response.setRequestedAmount(loanApplication.getRequestedAmount());
        response.setApprovedAmount(loanApplication.getApprovedAmount());
        response.setStatus(loanApplication.getStatus());
        response.setStage(loanApplication.getStage());

        response.setTerm(loanTermService.getLoanTermById(loanApplication.getTerm().getId()));


        response.setInterestRate(loanApplication.getInterestRate());
        response.setTenureMonths(loanApplication.getTenureMonths());

        if (Objects.nonNull(loanApplication.getCreatedBy())) {
            response.setCreatedBy(loanApplication.getCreatedBy().getId());
        }
        if (Objects.nonNull(loanApplication.getAssignedTo())) {
            response.setAssignedTo(loanApplication.getAssignedTo().getId());
        }

        response.setCreatedAt(loanApplication.getCreatedAt());
        response.setUpdatedAt(loanApplication.getUpdatedAt());
        response.setNotes(loanApplication.getNotes());
        return response;
    }
}
