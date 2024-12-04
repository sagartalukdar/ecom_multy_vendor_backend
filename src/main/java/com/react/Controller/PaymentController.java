package com.react.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.react.Model.PaymentOrder;
import com.react.Model.Seller;
import com.react.Model.SellerReport;
import com.react.Model.Transaction;
import com.react.Model.User;
import com.react.Model.UserOrder;
import com.react.Response.ApiResponse;
import com.react.Response.PaymentLinkResponse;
import com.react.Service.OrderService;
import com.react.Service.PaymentService;
import com.react.Service.SellerReportService;
import com.react.Service.SellerService;
import com.react.Service.TransectionService;
import com.react.Service.UserService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;
	@Autowired
	private UserService userService;
	@Autowired
	private SellerService sellerService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private SellerReportService sellerReportService;
	@Autowired
	private TransectionService transectionService;
	
	@GetMapping("/{orderId}/payment/{paymentId}")
	public ResponseEntity<ApiResponse> paymentSuccessHandler(@PathVariable Long orderId, @PathVariable String paymentId,@RequestParam String paymentLinkId,@RequestHeader("Authorization") String jwt) throws Exception{
        User user=userService.findUserByJwt(jwt);	
        
        PaymentOrder paymentOrder=paymentService.getPaymentOrderById(orderId);
        boolean paymentSuccess=paymentService.proceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);
        
        if(paymentSuccess) {
        	for(UserOrder order:paymentOrder.getOrders()) {
        		transectionService.createTransection(order);
        		
        		Seller seller=sellerService.getSellerById(order.getSellerId());
        		SellerReport report=sellerReportService.getSellerReport(seller);
        		
        		report.setTotalOrders(report.getTotalOrders()+1);
        		report.setTotalEarnings(report.getTotalEarnings()+order.getTotalSellingPrice());
        		report.setTotalSales(report.getTotalSales()+order.getOrderItems().size());
      
        		sellerReportService.updateSellerReport(report);
        	}
        }
        ApiResponse res= new ApiResponse("paaayment successfull");
        return new ResponseEntity<>(res,HttpStatus.OK);
        
	}
	
	
	
}
