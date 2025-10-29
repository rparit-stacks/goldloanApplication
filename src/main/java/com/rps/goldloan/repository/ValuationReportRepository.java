package com.rps.goldloan.repository;

import com.rps.goldloan.entity.ValuationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValuationReportRepository extends JpaRepository<ValuationReport, Long> {
}
