package com.react.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.react.Model.Seller;
import com.react.Model.SellerReport;
import com.react.Repository.SellerReportRepository;
import com.react.Service.SellerReportService;

@Service
public class SellerReportServiceImpl implements SellerReportService{

	@Autowired
	private SellerReportRepository sellerReportRepository;
	
	@Override
	public SellerReport getSellerReport(Seller seller) {
		SellerReport sellerReport=sellerReportRepository.findBySellerId(seller.getId());
		if(sellerReport==null) {
			SellerReport sReport=new SellerReport();
			sReport.setSeller(seller);
		   return sellerReportRepository.save(sReport);
		}
		return sellerReport;
	}

	@Override
	public SellerReport updateSellerReport(SellerReport sellerReport) {
		return sellerReportRepository.save(sellerReport);
	}

}
