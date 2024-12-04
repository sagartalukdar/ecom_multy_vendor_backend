package com.react.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.react.Model.SellerReport;

@Repository
public interface SellerReportRepository extends JpaRepository<SellerReport, Long>{

	public SellerReport findBySellerId(Long sellerId);
}
