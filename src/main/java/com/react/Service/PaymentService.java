package com.react.Service;

import java.util.Set;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.react.Model.PaymentOrder;
import com.react.Model.User;
import com.react.Model.UserOrder;
import com.stripe.exception.StripeException;

public interface PaymentService {

	public PaymentOrder createOrder(User user,Set<UserOrder> orders);
	
	public PaymentOrder getPaymentOrderById(Long id) throws Exception;
	
	public PaymentOrder getPaymentOrderByPaymentId(String paymentLinkId) throws Exception;
	
	public boolean proceedPaymentOrder(PaymentOrder paymentOrder,String paymentId,String paymentLinkId) throws RazorpayException;
	
	public PaymentLink createRazorpayPaymentLink(User user,Long amount,Long orderId) throws RazorpayException;
	
	public String createStripePaymentLink(User user,Long amount,Long orderId) throws StripeException;
	
	
}
