package com.rps.goldloan.service;

import com.rps.goldloan.dto.BranchRequest;
import com.rps.goldloan.dto.BranchResponse;
import com.rps.goldloan.dto.BranchUpdateDto;
import com.rps.goldloan.entity.Branch;
import com.rps.goldloan.exception.BranchCreationException;
import com.rps.goldloan.exception.BranchNotFoundException;
import com.rps.goldloan.exception.BranchUpdateException;
import com.rps.goldloan.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class BranchService {

    @Autowired
    BranchRepository branchRepository;

    public BranchResponse createBranch(BranchRequest branchRequest) {
        try {
            validateBranchRequest(branchRequest);

            Branch branch = new Branch();
            branch.setName(branchRequest.getName());
            branch.setAddress(branchRequest.getAddress());
            branch.setContactNumber(branchRequest.getContactNumber());
            branch.setManagerId(branchRequest.getManagerId());
            branch.setCode(branchRequest.getCode());
            branch.setCreatedAt(LocalDateTime.now());
            branch.setUpdatedAt(LocalDateTime.now());
            branch = branchRepository.save(branch);
            return branchToBranchDto(branch);
        } catch (IllegalArgumentException e) {
            throw new BranchCreationException(e.getMessage());
        } catch (Exception e) {
            throw new BranchCreationException("Error creating branch: " + e.getMessage());
        }
    }

    public BranchResponse getBranchById(Long branchId) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(()
                -> new BranchNotFoundException("Branch not found with ID: " + branchId));
        return branchToBranchDto(branch);
    }

    public Branch getBranch(Long branchId) {
        return branchRepository.findById(branchId).orElseThrow(()
                -> new BranchNotFoundException("Branch not found with ID: " + branchId));
    }


    public List<BranchResponse> getAllBranches() {
        List<Branch> branches = branchRepository.findAll();
        List<BranchResponse> branchResponses = new ArrayList<>();
        for (Branch branch : branches) {
            branchResponses.add(branchToBranchDto(branch));
        }
        return branchResponses;
    }

    public BranchResponse updateBranch(Long branchId, BranchUpdateDto branchUpdateDto) {
        try {
            Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));

            if (Objects.nonNull(branchUpdateDto.getName()) && !branchUpdateDto.getName().isEmpty()) {
                branch.setName(branchUpdateDto.getName());
            }

            if (Objects.nonNull(branchUpdateDto.getAddress()) && !branchUpdateDto.getAddress().isEmpty()) {
                branch.setAddress(branchUpdateDto.getAddress());
            }

            if (Objects.nonNull(branchUpdateDto.getContactNumber()) && !branchUpdateDto.getContactNumber().isEmpty()) {
                branch.setContactNumber(branchUpdateDto.getContactNumber());
            }

            if (Objects.nonNull(branchUpdateDto.getManagerId())) {
                branch.setManagerId(branchUpdateDto.getManagerId());
            }

            if (Objects.nonNull(branchUpdateDto.getCode()) && !branchUpdateDto.getCode().isEmpty()) {
                branch.setCode(branchUpdateDto.getCode());
            }

            branch.setUpdatedAt(LocalDateTime.now());

            branch = branchRepository.save(branch);

            return branchToBranchDto(branch);
        } catch (BranchNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BranchUpdateException("Error updating branch with ID: " + branchId + ". " + e.getMessage());
        }
    }

    public void deleteBranch(Long branchId) {
        if (!branchRepository.existsById(branchId)) {
            throw new BranchNotFoundException("Branch not found with ID: " + branchId);
        }
        branchRepository.deleteById(branchId);
    }

    private void validateBranchRequest(BranchRequest branchRequest) {
        if (Objects.isNull(branchRequest)) {
            throw new IllegalArgumentException("Branch request cannot be null");
        }
        if (Objects.isNull(branchRequest.getCode()) || branchRequest.getCode().isEmpty()) {
            throw new IllegalArgumentException("Branch code is required and cannot be empty");
        }
        if (Objects.isNull(branchRequest.getName()) || branchRequest.getName().isEmpty()) {
            throw new IllegalArgumentException("Branch name is required and cannot be empty");
        }
    }

    private BranchResponse branchToBranchDto(Branch branch) {
        BranchResponse branchResponse = new BranchResponse();
        branchResponse.setId(branch.getId());
        branchResponse.setCode(branch.getCode());
        branchResponse.setName(branch.getName());
        branchResponse.setAddress(branch.getAddress());
        branchResponse.setContactNumber(branch.getContactNumber());
        branchResponse.setManagerId(branch.getManagerId());
        branchResponse.setCreatedAt(branch.getCreatedAt());
        branchResponse.setUpdatedAt(branch.getUpdatedAt());
        return branchResponse;
    }
}
