package com.rps.goldloan.service;

import com.rps.goldloan.dto.ApplicationAuditRequest;
import com.rps.goldloan.dto.ApplicationAuditResponse;
import com.rps.goldloan.entity.ApplicationAudit;
import com.rps.goldloan.entity.LoanApplication;
import com.rps.goldloan.entity.User;
import com.rps.goldloan.exception.ApplicationAuditCreationException;
import com.rps.goldloan.exception.ApplicationAuditNotFoundException;
import com.rps.goldloan.repository.ApplicationAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ApplicationAuditService {

    @Autowired
    private ApplicationAuditRepository applicationAuditRepository;

    @Autowired
    @Lazy
    private LoanApplicationService loanApplicationService;

    @Autowired
    private UserService userService;

    public ApplicationAuditResponse createApplicationAudit(ApplicationAuditRequest applicationAuditRequest) {
        try {
            validateApplicationAuditRequest(applicationAuditRequest);

            ApplicationAudit applicationAudit = new ApplicationAudit();
            
            LoanApplication loanApplication = loanApplicationService.getLoanApplication(applicationAuditRequest.getLoanApplicationId());
            applicationAudit.setLoanApplication(loanApplication);

            applicationAudit.setAction(applicationAuditRequest.getAction());
            applicationAudit.setFromStatus(applicationAuditRequest.getFromStatus());
            applicationAudit.setToStatus(applicationAuditRequest.getToStatus());
            applicationAudit.setComment(applicationAuditRequest.getComment());

            if (applicationAuditRequest.getPerformedById() != null) {
                User user = userService.getUser(applicationAuditRequest.getPerformedById());
                applicationAudit.setPerformedBy(user);
            }

            applicationAudit.setTimestamp(Instant.now());

            applicationAudit = applicationAuditRepository.save(applicationAudit);
            return applicationAuditToResponse(applicationAudit);
        } catch (IllegalArgumentException e) {
            throw new ApplicationAuditCreationException(e.getMessage());
        } catch (Exception e) {
            throw new ApplicationAuditCreationException("Error creating application audit: " + e.getMessage());
        }
    }

    public ApplicationAuditResponse getApplicationAuditById(Long id) {
        ApplicationAudit applicationAudit = applicationAuditRepository.findById(id)
                .orElseThrow(() -> new ApplicationAuditNotFoundException("Application audit not found with ID: " + id));
        return applicationAuditToResponse(applicationAudit);
    }

    public List<ApplicationAuditResponse> getAllApplicationAudits() {
        List<ApplicationAudit> applicationAudits = applicationAuditRepository.findAll();
        List<ApplicationAuditResponse> applicationAuditResponses = new ArrayList<>();
        for (ApplicationAudit applicationAudit : applicationAudits) {
            applicationAuditResponses.add(applicationAuditToResponse(applicationAudit));
        }
        return applicationAuditResponses;
    }

    public List<ApplicationAuditResponse> getApplicationAuditsByLoanApplicationId(Long loanApplicationId) {
        if (!loanApplicationService.existsById(loanApplicationId)) {
            throw new ApplicationAuditNotFoundException("Loan application not found with ID: " + loanApplicationId);
        }
        
        List<ApplicationAudit> applicationAudits = applicationAuditRepository.findAll();
        List<ApplicationAuditResponse> applicationAuditResponses = new ArrayList<>();
        
        for (ApplicationAudit applicationAudit : applicationAudits) {
            if (Objects.nonNull(applicationAudit.getLoanApplication()) && 
                applicationAudit.getLoanApplication().getId().equals(loanApplicationId)) {
                applicationAuditResponses.add(applicationAuditToResponse(applicationAudit));
            }
        }
        return applicationAuditResponses;
    }

    public void deleteApplicationAudit(Long id) {
        if (!applicationAuditRepository.existsById(id)) {
            throw new ApplicationAuditNotFoundException("Application audit not found with ID: " + id);
        }
        applicationAuditRepository.deleteById(id);
    }

    private void validateApplicationAuditRequest(ApplicationAuditRequest applicationAuditRequest) {
        if (Objects.isNull(applicationAuditRequest)) {
            throw new IllegalArgumentException("Application audit request cannot be null");
        }
        if (Objects.isNull(applicationAuditRequest.getLoanApplicationId())) {
            throw new IllegalArgumentException("Loan application ID is required and cannot be null");
        }
        if (Objects.isNull(applicationAuditRequest.getAction()) || applicationAuditRequest.getAction().isEmpty()) {
            throw new IllegalArgumentException("Action is required and cannot be empty");
        }
    }

    private ApplicationAuditResponse applicationAuditToResponse(ApplicationAudit applicationAudit) {
        ApplicationAuditResponse response = new ApplicationAuditResponse();
        response.setId(applicationAudit.getId());
        if (Objects.nonNull(applicationAudit.getLoanApplication())) {
            response.setLoanApplicationId(applicationAudit.getLoanApplication().getId());
        }
        response.setAction(applicationAudit.getAction());
        response.setFromStatus(applicationAudit.getFromStatus());
        response.setToStatus(applicationAudit.getToStatus());
        response.setComment(applicationAudit.getComment());
        if (Objects.nonNull(applicationAudit.getPerformedBy())) {
            response.setPerformedById(applicationAudit.getPerformedBy().getId());
        }
        response.setTimestamp(applicationAudit.getTimestamp());
        return response;
    }
}
