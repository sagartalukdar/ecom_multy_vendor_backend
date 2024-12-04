package com.react.Controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.PaymentLink;
import com.react.Model.Address;
import com.react.Model.Cart;
import com.react.Model.OrderItem;
import com.react.Model.PaymentOrder;
import com.react.Model.Seller;
import com.react.Model.SellerReport;
import com.react.Model.User;
import com.react.Model.UserOrder;
import com.react.Repository.PaymentOrderRepository;
import com.react.Response.PaymentLinkResponse;
import com.react.Service.CartService;
import com.react.Service.OrderService;
import com.react.Service.PaymentService;
import com.react.Service.SellerReportService;
import com.react.Service.SellerService;
import com.react.Service.UserService;
import com.react.domain.PaymentMethod;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	@Autowired
	private CartService cartService;
	@Autowired
	private SellerService sellerService;
	@Autowired
	private SellerReportService sellerReportService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private PaymentOrderRepository paymentOrderRepository;
	
	@PostMapping("/create")
	public ResponseEntity<PaymentLinkResponse> createOrder(
			@RequestBody Address shippingAddress,
			@RequestParam PaymentMethod paymentMethod,
			@RequestHeader("Authorization")String jwt
			) throws Exception{
		User user=userService.findUserByJwt(jwt);
		Cart cart=cartService.findUserCartForAddItem(user);
		Set<UserOrder> orders=orderService.createOrder(user, shippingAddress, cart);
		
		PaymentOrder paymentOrder=paymentService.createOrder(user, orders);
		
		PaymentLinkResponse res=new PaymentLinkResponse();
		if(paymentMethod.equals(paymentMethod.RAZORPAY)) {
			PaymentLink payment=paymentService.createRazorpayPaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
			String paymentUrl=payment.get("short_url");
			String paymentUrlId=payment.get("id");
			res.setPayment_link_url(paymentUrl);
			res.setPayment_link_id(paymentUrlId);
			paymentOrder.setPaymentLinkId(paymentUrlId);
			paymentOrder.setPaymentMethod(paymentMethod);
			paymentOrderRepository.save(paymentOrder);
		}else {
			String paymentUrl=paymentService.createStripePaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
			res.setPayment_link_url(paymentUrl);
		}
		return new ResponseEntity<PaymentLinkResponse>(res,HttpStatus.CREATED);
	}
	
	@GetMapping("/user")
	public ResponseEntity<List<UserOrder>> userOrderHistory(@RequestHeader("Authorization")String jwt) throws Exception{
		User user=userService.findUserByJwt(jwt);
		return new ResponseEntity<List<UserOrder>>(
	    orderService.userOrderHistory(user.getId()),HttpStatus.OK);				
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<UserOrder> getOrderById(@RequestHeader("Authorization")String jwt,@PathVariable Long orderId) throws Exception{
		User user=userService.findUserByJwt(jwt);
		return new ResponseEntity<UserOrder>(
				orderService.findOrderById(orderId),HttpStatus.OK
				);
	}
	@GetMapping("/orderItem/{orderItemId}")
	public ResponseEntity<OrderItem> getOrderItemById(@RequestHeader("Authorization")String jwt,@PathVariable Long orderItemId) throws Exception{
		return new ResponseEntity<OrderItem>(orderService.findOrderItemById(orderItemId),HttpStatus.OK);
	}
	@GetMapping("/{orderId}/cancel")
	public ResponseEntity<UserOrder> cancelOrder(@RequestHeader("Authorization")String jwt,@PathVariable Long orderId) throws Exception{
		User user=userService.findUserByJwt(jwt);
		UserOrder order=orderService.cancelOrder(orderId, user);
		
		Seller seller=sellerService.getSellerById(order.getSellerId());
		SellerReport sellerReport=sellerReportService.getSellerReport(seller);
		
		sellerReport.setCanceledOrders(sellerReport.getCanceledOrders()+1);
		sellerReport.setTotalRefunds(sellerReport.getTotalRefunds()+order.getTotalSellingPrice());
		sellerReportService.updateSellerReport(sellerReport);

		return new ResponseEntity<UserOrder>(order,HttpStatus.OK);
	}

}
