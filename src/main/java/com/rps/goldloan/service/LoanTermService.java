package com.rps.goldloan.service;

import com.rps.goldloan.dto.LoanTermRequest;
import com.rps.goldloan.dto.LoanTermResponse;
import com.rps.goldloan.dto.LoanTermUpdateDto;
import com.rps.goldloan.entity.LoanTerm;
import com.rps.goldloan.exception.LoanTermCreationException;
import com.rps.goldloan.exception.LoanTermNotFoundException;
import com.rps.goldloan.exception.LoanTermUpdateException;
import com.rps.goldloan.repository.LoanTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class LoanTermService {

    @Autowired
    private LoanTermRepository loanTermRepository;

    public LoanTermResponse createLoanTerm(LoanTermRequest loanTermRequest) {
        try {
            validateLoanTermRequest(loanTermRequest);

            LoanTerm loanTerm = new LoanTerm();
            loanTerm.setName(loanTermRequest.getName());
            loanTerm.setMaxLTVPercent(loanTermRequest.getMaxLTVPercent());
            loanTerm.setMinAmount(loanTermRequest.getMinAmount());
            loanTerm.setMaxAmount(loanTermRequest.getMaxAmount());
            loanTerm.setInterestRateAnnual(loanTermRequest.getInterestRateAnnual());
            loanTerm.setMaxTenureMonths(loanTermRequest.getMaxTenureMonths());
            loanTerm.setCreatedAt(LocalDateTime.now());
            loanTerm.setUpdatedAt(LocalDateTime.now());

            loanTerm = loanTermRepository.save(loanTerm);
            return loanTermToResponse(loanTerm);
        } catch (IllegalArgumentException e) {
            throw new LoanTermCreationException(e.getMessage());
        } catch (Exception e) {
            throw new LoanTermCreationException("Error creating loan term: " + e.getMessage());
        }
    }

    public LoanTermResponse getLoanTermById(Long termId) {
        LoanTerm loanTerm = loanTermRepository.findById(termId)
                .orElseThrow(() -> new LoanTermNotFoundException("Loan term not found with ID: " + termId));
        return loanTermToResponse(loanTerm);
    }

    public LoanTerm getLoanTerm(Long termId) {
        return loanTermRepository.findById(termId)
                .orElseThrow(() -> new LoanTermNotFoundException("Loan term not found with ID: " + termId));
    }

    public List<LoanTermResponse> getAllLoanTerms() {
        List<LoanTerm> loanTerms = loanTermRepository.findAll();
        List<LoanTermResponse> loanTermResponses = new ArrayList<>();
        for (LoanTerm loanTerm : loanTerms) {
            loanTermResponses.add(loanTermToResponse(loanTerm));
        }
        return loanTermResponses;
    }

    public LoanTermResponse updateLoanTerm(Long termId, LoanTermUpdateDto loanTermUpdateDto) {
        try {
            LoanTerm loanTerm = loanTermRepository.findById(termId)
                    .orElseThrow(() -> new LoanTermNotFoundException("Loan term not found with ID: " + termId));

            if (Objects.nonNull(loanTermUpdateDto.getName()) && !loanTermUpdateDto.getName().isEmpty()) {
                loanTerm.setName(loanTermUpdateDto.getName());
            }

            if (Objects.nonNull(loanTermUpdateDto.getMaxLTVPercent())) {
                loanTerm.setMaxLTVPercent(loanTermUpdateDto.getMaxLTVPercent());
            }

            if (Objects.nonNull(loanTermUpdateDto.getMinAmount())) {
                loanTerm.setMinAmount(loanTermUpdateDto.getMinAmount());
            }

            if (Objects.nonNull(loanTermUpdateDto.getMaxAmount())) {
                loanTerm.setMaxAmount(loanTermUpdateDto.getMaxAmount());
            }

            if (Objects.nonNull(loanTermUpdateDto.getInterestRateAnnual())) {
                loanTerm.setInterestRateAnnual(loanTermUpdateDto.getInterestRateAnnual());
            }

            if (Objects.nonNull(loanTermUpdateDto.getMaxTenureMonths())) {
                loanTerm.setMaxTenureMonths(loanTermUpdateDto.getMaxTenureMonths());
            }

            loanTerm.setUpdatedAt(LocalDateTime.now());
            loanTerm = loanTermRepository.save(loanTerm);

            return loanTermToResponse(loanTerm);
        } catch (LoanTermNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new LoanTermUpdateException("Error updating loan term with ID: " + termId + ". " + e.getMessage());
        }
    }

    public void deleteLoanTerm(Long termId) {
        if (!loanTermRepository.existsById(termId)) {
            throw new LoanTermNotFoundException("Loan term not found with ID: " + termId);
        }
        loanTermRepository.deleteById(termId);
    }

    private void validateLoanTermRequest(LoanTermRequest loanTermRequest) {
        if (Objects.isNull(loanTermRequest)) {
            throw new IllegalArgumentException("Loan term request cannot be null");
        }
        if (Objects.isNull(loanTermRequest.getName()) || loanTermRequest.getName().isEmpty()) {
            throw new IllegalArgumentException("Loan term name is required and cannot be empty");
        }
        if (Objects.isNull(loanTermRequest.getInterestRateAnnual()) || 
            loanTermRequest.getInterestRateAnnual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Interest rate is required and must be greater than zero");
        }
        if (Objects.nonNull(loanTermRequest.getMaxLTVPercent()) && 
            (loanTermRequest.getMaxLTVPercent() < 0 || loanTermRequest.getMaxLTVPercent() > 100)) {
            throw new IllegalArgumentException("Max LTV percent must be between 0 and 100");
        }
        if (Objects.nonNull(loanTermRequest.getMinAmount()) && 
            loanTermRequest.getMinAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Minimum amount must be greater than zero");
        }
        if (Objects.nonNull(loanTermRequest.getMaxAmount()) && 
            loanTermRequest.getMaxAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Maximum amount must be greater than zero");
        }
        if (Objects.nonNull(loanTermRequest.getMinAmount()) && 
            Objects.nonNull(loanTermRequest.getMaxAmount()) && 
            loanTermRequest.getMinAmount().compareTo(loanTermRequest.getMaxAmount()) > 0) {
            throw new IllegalArgumentException("Minimum amount cannot be greater than maximum amount");
        }
        if (Objects.nonNull(loanTermRequest.getMaxTenureMonths()) && 
            loanTermRequest.getMaxTenureMonths() <= 0) {
            throw new IllegalArgumentException("Max tenure months must be greater than zero");
        }
    }

    private LoanTermResponse loanTermToResponse(LoanTerm loanTerm) {
        LoanTermResponse response = new LoanTermResponse();
        response.setId(loanTerm.getId());
        response.setName(loanTerm.getName());
        response.setMaxLTVPercent(loanTerm.getMaxLTVPercent());
        response.setMinAmount(loanTerm.getMinAmount());
        response.setMaxAmount(loanTerm.getMaxAmount());
        response.setInterestRateAnnual(loanTerm.getInterestRateAnnual());
        response.setMaxTenureMonths(loanTerm.getMaxTenureMonths());
        response.setCreatedAt(loanTerm.getCreatedAt());
        response.setUpdatedAt(loanTerm.getUpdatedAt());
        return response;
    }
}
