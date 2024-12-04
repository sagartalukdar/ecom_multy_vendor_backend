package com.react.Service;

import com.react.Model.Seller;
import com.react.Model.SellerReport;

public interface SellerReportService {

	public SellerReport getSellerReport(Seller seller);
	
	public SellerReport updateSellerReport(SellerReport sellerReport);
}
