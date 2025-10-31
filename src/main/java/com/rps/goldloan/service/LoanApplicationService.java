package com.rps.goldloan.service;

import com.rps.goldloan.dto.LoanApplicationRequest;
import com.rps.goldloan.dto.LoanApplicationResponse;
import com.rps.goldloan.dto.LoanApplicationUpdateDto;
import com.rps.goldloan.entity.LoanApplication;
import com.rps.goldloan.entity.LoanTerm;
import com.rps.goldloan.enums.ApplicationStatus;
import com.rps.goldloan.exception.LoanApplicationCreationException;
import com.rps.goldloan.exception.LoanApplicationNotFoundException;
import com.rps.goldloan.exception.LoanApplicationUpdateException;
import com.rps.goldloan.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            loanApplication.setApplicationNumber(generateApplicationNumber());
            
            loanApplication.setCreatedAt(LocalDateTime.now());
            loanApplication.setUpdatedAt(LocalDateTime.now());

            loanApplication = loanApplicationRepository.save(loanApplication);
            return loanApplicationToResponse(loanApplication);
        } catch (IllegalArgumentException e) {
            throw new LoanApplicationCreationException(e.getMessage());
        } catch (Exception e) {
            throw new LoanApplicationCreationException("Error creating loan application: " + e.getMessage());
        }
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
        List<LoanApplicationResponse> loanApplicationResponses = new ArrayList<>();
        for (LoanApplication loanApplication : loanApplications) {
            loanApplicationResponses.add(loanApplicationToResponse(loanApplication));
        }
        return loanApplicationResponses;
    }

    public LoanApplicationResponse updateLoanApplication(Long loanApplicationId, LoanApplicationUpdateDto loanApplicationUpdateDto) {
        try {
            LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId)
                    .orElseThrow(() -> new LoanApplicationNotFoundException("Loan application not found with ID: " + loanApplicationId));

            if (Objects.nonNull(loanApplicationUpdateDto.getCustomerId())) {
                loanApplication.setCustomer(customerService.getCustomer(loanApplicationUpdateDto.getCustomerId()));
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getBranchId())) {
                loanApplication.setBranch(branchService.getBranch(loanApplicationUpdateDto.getBranchId()));
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getRequestedAmount())) {
                loanApplication.setRequestedAmount(loanApplicationUpdateDto.getRequestedAmount());
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getApprovedAmount())) {
                loanApplication.setApprovedAmount(loanApplicationUpdateDto.getApprovedAmount());
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getStatus())) {
                loanApplication.setStatus(loanApplicationUpdateDto.getStatus());
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getStage()) && !loanApplicationUpdateDto.getStage().isEmpty()) {
                loanApplication.setStage(loanApplicationUpdateDto.getStage());
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getTermId())) {
                LoanTerm loanTerm = loanTermService.getLoanTerm(loanApplicationUpdateDto.getTermId());
                loanApplication.setTerm(loanTerm);
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getInterestRate())) {
                loanApplication.setInterestRate(loanApplicationUpdateDto.getInterestRate());
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getTenureMonths())) {
                loanApplication.setTenureMonths(loanApplicationUpdateDto.getTenureMonths());
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getCreatedBy())) {
                loanApplication.setCreatedBy(userService.getUser(loanApplicationUpdateDto.getCreatedBy()));
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getAssignedTo())) {
                loanApplication.setAssignedTo(userService.getUser(loanApplicationUpdateDto.getAssignedTo()));
            }

            if (Objects.nonNull(loanApplicationUpdateDto.getNotes()) && !loanApplicationUpdateDto.getNotes().isEmpty()) {
                loanApplication.setNotes(loanApplicationUpdateDto.getNotes());
            }

            loanApplication.setUpdatedAt(LocalDateTime.now());
            loanApplication = loanApplicationRepository.save(loanApplication);

            return loanApplicationToResponse(loanApplication);
        } catch (LoanApplicationNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new LoanApplicationUpdateException("Error updating loan application with ID: " + loanApplicationId + ". " + e.getMessage());
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
        if (Objects.nonNull(loanApplication.getCustomer())) {
            response.setCustomerId(loanApplication.getCustomer().getId());
        }
        if (Objects.nonNull(loanApplication.getBranch())) {
            response.setBranchId(loanApplication.getBranch().getId());
        }
        response.setRequestedAmount(loanApplication.getRequestedAmount());
        response.setApprovedAmount(loanApplication.getApprovedAmount());
        response.setStatus(loanApplication.getStatus());
        response.setStage(loanApplication.getStage());
        if (Objects.nonNull(loanApplication.getTerm())) {
            response.setTermId(loanApplication.getTerm().getId());
        }
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
