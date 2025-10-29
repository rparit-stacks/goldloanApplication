package com.rps.goldloan.repository;

import com.rps.goldloan.entity.GoldDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoldDetailRepository extends JpaRepository<GoldDetail, Long> {
}
