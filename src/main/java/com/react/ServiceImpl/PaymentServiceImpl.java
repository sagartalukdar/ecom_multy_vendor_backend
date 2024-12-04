package com.react.ServiceImpl;

import java.util.Set;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.react.Model.PaymentOrder;
import com.react.Model.User;
import com.react.Model.UserOrder;
import com.react.Repository.OrderRepository;
import com.react.Repository.PaymentOrderRepository;
import com.react.Service.OrderService;
import com.react.Service.PaymentService;
import com.react.domain.PaymentOrderStatus;
import com.react.domain.PaymentStatus;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class PaymentServiceImpl implements PaymentService{
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private PaymentOrderRepository paymentOrderRepository;
	@Autowired
	private OrderRepository orderRepository;
	
	
	private String apiKey="rzp_test_8Cwya2nBskHkn3";
	private String apiSecret="Aa1cVfQCDCqXEY7wpyTbuoHC";
	
	private String stripeSecretKey="";

	@Override
	public PaymentOrder createOrder(User user, Set<UserOrder> orders) {
		
		Long amount=orders.stream().mapToLong(UserOrder::getTotalSellingPrice).sum();
				
		PaymentOrder paymentOrder=new PaymentOrder();
		paymentOrder.setAmount(amount);
		paymentOrder.setUser(user);
		paymentOrder.setOrders(orders);
		return paymentOrderRepository.save(paymentOrder);
	}

	@Override
	public PaymentOrder getPaymentOrderById(Long id) throws Exception {
		return paymentOrderRepository.findById(id).orElseThrow(()->
		 new Exception("payment order not found with id :"+id)
				);
	}

	@Override
	public PaymentOrder getPaymentOrderByPaymentId(String paymentLinkId) throws Exception {
		PaymentOrder paymentOrder=paymentOrderRepository.findByPaymentLinkId(paymentLinkId);
		if(paymentOrder==null) {
			throw new Exception("payment order not found with provided link id :"+paymentLinkId);
		}
		return paymentOrder;
	}

	@Override
	public boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {
				
		if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
			RazorpayClient razorpay=new RazorpayClient(apiKey, apiSecret);
			
			Payment payment=razorpay.payments.fetch(paymentId);
			String status=payment.get("status");
			
			if(status.equals("captured")) {
				Set<UserOrder> orders=paymentOrder.getOrders();
				for(UserOrder order:orders) {
					order.getPaymentDetails().setPaymentStatus(PaymentStatus.COMPLETED);
					orderRepository.save(order);
				}
				paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
				paymentOrderRepository.save(paymentOrder);
				return true;
			}
			paymentOrder.setStatus(PaymentOrderStatus.FAILURE);
			paymentOrderRepository.save(paymentOrder);
			return false;
		}
		return false;
	}

	@Override
	public PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException {
		amount=amount*100;

		try {
			RazorpayClient razorpay=new RazorpayClient(apiKey, apiSecret);
			
			JSONObject paymentLinkRequest=new JSONObject();
			paymentLinkRequest.put("amount", amount);
			paymentLinkRequest.put("currency", "INR");
			
			JSONObject customer=new JSONObject();
			customer.put("name", user.getFullName());
			customer.put("email", user.getEmail());
			paymentLinkRequest.put("customer", customer);
			
			JSONObject notify=new JSONObject();
			notify.put("email", true);
			paymentLinkRequest.put("notify", notify);
			
			paymentLinkRequest.put("callback_url", "http://localhost:3000/payment-success/"+orderId);
			paymentLinkRequest.put("callback_method", "get");
			
			PaymentLink paymentLink=razorpay.paymentLink.create(paymentLinkRequest);
			String paymentLinkUrl=paymentLink.get("short_url");
			String paymentLinkId=paymentLink.get("id");			
			
			return paymentLink;			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RazorpayException(e.getMessage());
		}
	}

	@Override
	public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
		Stripe.apiKey=stripeSecretKey;
		
		SessionCreateParams params=SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://localhost:3000/payment-success/"+orderId)
				.setCancelUrl("http://localhost:3000/payment-cancel")
				.addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L)
						.setPriceData(SessionCreateParams.LineItem.PriceData.builder().setCurrency("usd").setUnitAmount(amount*100)
								.setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder().setName("ecommerce multi vendor").build())
								.build())
						.build())				
				.build();
	
		Session session=Session.create(params);
		return session.getUrl();		
	}

}
