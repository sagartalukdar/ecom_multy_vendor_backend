package com.react.Service;

import java.util.List;
import java.util.Set;

import com.react.Model.Address;
import com.react.Model.Cart;
import com.react.Model.OrderItem;
import com.react.Model.User;
import com.react.Model.UserOrder;
import com.react.domain.OrderStatus;

public interface OrderService {

	public Set<UserOrder> createOrder(User user,Address shippingAddress,Cart cart);
	
	public UserOrder findOrderById(Long id) throws Exception;
	
	public List<UserOrder> userOrderHistory(Long userId);
	
	public List<UserOrder> sellerOrder(Long sellerId);
	
	public UserOrder updateOrderStatus(Long orderId,OrderStatus status) throws Exception;
	
	public UserOrder cancelOrder(Long orderId,User user) throws Exception;
	
	public OrderItem findOrderItemById(Long id) throws Exception;
}
