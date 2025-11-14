package com.rps.goldloan.service;

import com.rps.goldloan.dto.GoldDetailRequest;
import com.rps.goldloan.dto.GoldDetailResponse;
import com.rps.goldloan.dto.GoldDetailUpdateDto;
import com.rps.goldloan.entity.GoldDetail;
import com.rps.goldloan.entity.LoanApplication;
import com.rps.goldloan.exception.GoldDetailCreationException;
import com.rps.goldloan.exception.GoldDetailNotFoundException;
import com.rps.goldloan.exception.GoldDetailUpdateException;
import com.rps.goldloan.repository.GoldDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class GoldDetailService {

    @Autowired
    private GoldDetailRepository goldDetailRepository;
    @Autowired
    private LoanApplicationService loanApplicationService;

    public GoldDetailResponse createGoldDetail(GoldDetailRequest goldDetailRequest) {
        try {
            validateGoldDetailRequest(goldDetailRequest);

            GoldDetail goldDetail = new GoldDetail();

            goldDetail.setDescription(goldDetailRequest.getDescription());
            goldDetail.setWeightGrams(goldDetailRequest.getWeightGrams());
            goldDetail.setPurity(goldDetailRequest.getPurity());
            goldDetail.setMarketRatePerGram(goldDetailRequest.getMarketRatePerGram());
            goldDetail.setPhotoUrl(goldDetailRequest.getPhotoUrl());
            goldDetail.setLoanApplication(null);

            BigDecimal assessedValue = calculateAssessedValue(
                goldDetailRequest.getWeightGrams(),
                goldDetailRequest.getPurity(),
                goldDetailRequest.getMarketRatePerGram()
            );
            goldDetail.setAssessedValue(assessedValue);

            goldDetail.setCreatedAt(LocalDateTime.now());
            goldDetail.setUpdatedAt(LocalDateTime.now());

            goldDetail = goldDetailRepository.save(goldDetail);
            return goldDetailToResponse(goldDetail);
        } catch (IllegalArgumentException e) {
            throw new GoldDetailCreationException(e.getMessage());
        } catch (Exception e) {
            throw new GoldDetailCreationException("Error creating gold detail: " + e.getMessage());
        }
    }

    public GoldDetailResponse getGoldDetailById(Long goldDetailId) {
        GoldDetail goldDetail = goldDetailRepository.findById(goldDetailId)
                .orElseThrow(() -> new GoldDetailNotFoundException("Gold detail not found with ID: " + goldDetailId));
        return goldDetailToResponse(goldDetail);
    }

    public GoldDetail getGoldDetail(Long goldDetailId) {
        return goldDetailRepository.findById(goldDetailId)
                .orElseThrow(() -> new GoldDetailNotFoundException("Gold detail not found with ID: " + goldDetailId));
    }

    public List<GoldDetailResponse> getAllGoldDetails() {
        List<GoldDetail> goldDetails = goldDetailRepository.findAll();
        List<GoldDetailResponse> goldDetailResponses = new ArrayList<>();
        for (GoldDetail goldDetail : goldDetails) {
            goldDetailResponses.add(goldDetailToResponse(goldDetail));
        }
        return goldDetailResponses;
    }

    public GoldDetailResponse updateGoldDetail(Long goldDetailId, GoldDetailUpdateDto goldDetailUpdateDto) {
        try {
            GoldDetail goldDetail = goldDetailRepository.findById(goldDetailId)
                    .orElseThrow(() -> new GoldDetailNotFoundException("Gold detail not found with ID: " + goldDetailId));

            if (Objects.nonNull(goldDetailUpdateDto.getDescription()) && !goldDetailUpdateDto.getDescription().isEmpty()) {
                goldDetail.setDescription(goldDetailUpdateDto.getDescription());
            }

            if (Objects.nonNull(goldDetailUpdateDto.getWeightGrams())) {
                goldDetail.setWeightGrams(goldDetailUpdateDto.getWeightGrams());
            }

            if (Objects.nonNull(goldDetailUpdateDto.getPurity())) {
                goldDetail.setPurity(goldDetailUpdateDto.getPurity());
            }

            if (Objects.nonNull(goldDetailUpdateDto.getMarketRatePerGram())) {
                goldDetail.setMarketRatePerGram(goldDetailUpdateDto.getMarketRatePerGram());
            }

            if (Objects.nonNull(goldDetailUpdateDto.getPhotoUrl()) && !goldDetailUpdateDto.getPhotoUrl().isEmpty()) {
                goldDetail.setPhotoUrl(goldDetailUpdateDto.getPhotoUrl());
            }

            if (goldDetail.getWeightGrams() != null && 
                goldDetail.getPurity() != null && 
                goldDetail.getMarketRatePerGram() != null) {
                BigDecimal assessedValue = calculateAssessedValue(
                    goldDetail.getWeightGrams(),
                    goldDetail.getPurity(),
                    goldDetail.getMarketRatePerGram()
                );
                goldDetail.setAssessedValue(assessedValue);
            }

            goldDetail.setUpdatedAt(LocalDateTime.now());
            goldDetail = goldDetailRepository.save(goldDetail);

            return goldDetailToResponse(goldDetail);
        } catch (GoldDetailNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new GoldDetailUpdateException("Error updating gold detail with ID: " + goldDetailId + ". " + e.getMessage());
        }
    }

    public void deleteGoldDetail(Long goldDetailId) {
        if (!goldDetailRepository.existsById(goldDetailId)) {
            throw new GoldDetailNotFoundException("Gold detail not found with ID: " + goldDetailId);
        }
        goldDetailRepository.deleteById(goldDetailId);
    }

    private void validateGoldDetailRequest(GoldDetailRequest goldDetailRequest) {
        if (Objects.isNull(goldDetailRequest)) {
            throw new IllegalArgumentException("Gold detail request cannot be null");
        }
        if (Objects.isNull(goldDetailRequest.getWeightGrams()) || 
            goldDetailRequest.getWeightGrams().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Weight in grams is required and must be greater than zero");
        }
        if (Objects.isNull(goldDetailRequest.getPurity()) || 
            goldDetailRequest.getPurity().compareTo(BigDecimal.ZERO) <= 0 ||
            goldDetailRequest.getPurity().compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Purity is required and must be between 0 and 100");
        }
        if (Objects.isNull(goldDetailRequest.getMarketRatePerGram()) || 
            goldDetailRequest.getMarketRatePerGram().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Market rate per gram is required and must be greater than zero");
        }
    }

    private BigDecimal calculateAssessedValue(BigDecimal weightGrams, BigDecimal purity, BigDecimal marketRatePerGram) {
        BigDecimal purityRatio = purity.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        return weightGrams.multiply(purityRatio).multiply(marketRatePerGram).setScale(2, RoundingMode.HALF_UP);
    }

    private GoldDetailResponse goldDetailToResponse(GoldDetail goldDetail) {
        GoldDetailResponse response = new GoldDetailResponse();
        response.setId(goldDetail.getId());
        response.setDescription(goldDetail.getDescription());
        response.setWeightGrams(goldDetail.getWeightGrams());
        response.setPurity(goldDetail.getPurity());
        response.setAssessedValue(goldDetail.getAssessedValue());
        response.setMarketRatePerGram(goldDetail.getMarketRatePerGram());
        response.setPhotoUrl(goldDetail.getPhotoUrl());
        response.setCreatedAt(goldDetail.getCreatedAt());
        response.setUpdatedAt(goldDetail.getUpdatedAt());
        return response;
    }

    //assign loan application to gold
    public GoldDetail assignLoanApplication(Long goldDetailId, LoanApplication loanApplication) {
        GoldDetail goldDetail = goldDetailRepository.findById(goldDetailId)
                .orElseThrow(() -> new GoldDetailNotFoundException("Gold detail not found with ID: " + goldDetailId));
        goldDetail.setLoanApplication(loanApplication);
        return goldDetailRepository.save(goldDetail);
    }
}
