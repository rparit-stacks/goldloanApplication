package com.rps.goldloan.repository;

import com.rps.goldloan.entity.ApplicationAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationAuditRepository extends JpaRepository<ApplicationAudit, Long> {
}
