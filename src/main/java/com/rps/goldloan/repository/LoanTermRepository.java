package com.rps.goldloan.repository;

import com.rps.goldloan.entity.LoanTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanTermRepository extends JpaRepository<LoanTerm, Long> {
}
