package com.rps.goldloan.repository;

import com.rps.goldloan.entity.LoanApplication;
import com.rps.goldloan.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    @Query("""
       SELECT l
       FROM LoanApplication l
       WHERE l.status = 'INCOMPLETE'
       """)
    List<LoanApplication> getAllIncomplete();

    @Query("""
       SELECT l
       FROM LoanApplication l
       WHERE l.status = 'PENDING'
       """)
    List<LoanApplication> getAllPending();

    @Query("""
       SELECT l
       FROM LoanApplication l
       WHERE l.status = 'REJECTED'
       """)
    List<LoanApplication> getAllRejected();

    @Query("""
       SELECT l
       FROM LoanApplication l
       WHERE l.status = 'DISBURSED'
       """)
    List<LoanApplication> getAllDisbursed();


    @Query("""
       SELECT l
       FROM LoanApplication l
       WHERE l.status = 'CLOSED'
       """)
    List<LoanApplication> getAllClosed();


    @Query("""
       SELECT l
       FROM LoanApplication l
       WHERE l.status = 'CREATED'
       """)
    List<LoanApplication> getAllCreated();


    @Query("""
       SELECT l
       FROM LoanApplication l
       WHERE l.status = 'APPLIED'
       """)
    List<LoanApplication> getALlApplied();


    @Query("""
       SELECT l
       FROM LoanApplication l
       WHERE l.status = 'APPROVED'
       """)
    List<LoanApplication> getAllApproved();
}
