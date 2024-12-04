package com.react.ServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.react.Model.Address;
import com.react.Model.Cart;
import com.react.Model.CartItem;
import com.react.Model.OrderItem;
import com.react.Model.User;
import com.react.Model.UserOrder;
import com.react.Repository.AddressRepository;
import com.react.Repository.CartItemRepository;
import com.react.Repository.OrderItemRepository;
import com.react.Repository.OrderRepository;
import com.react.Repository.UserRepository;
import com.react.Service.OrderService;
import com.react.domain.OrderStatus;
import com.react.domain.PaymentStatus;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Override
	public Set<UserOrder> createOrder(User user, Address shippingAddress, Cart cart) {

		if(!user.getAddresses().contains(shippingAddress)) {
			shippingAddress = addressRepository.save(shippingAddress);
			user.getAddresses().add(shippingAddress);	
			user=userRepository.save(user);
		}
		
		Set<CartItem> cartItems=cartItemRepository.findByCart(cart);
		
		Map<Long, List<CartItem>> itemsBySeller=cartItems.stream()
				.collect(Collectors.groupingBy(item->item.getProduct()
						.getSeller().getId()
						));
		Set<UserOrder> orders=new HashSet<>();
		
		for(Map.Entry<Long, List<CartItem>> entry: itemsBySeller.entrySet()) {
			Long sellerId=entry.getKey();
			List<CartItem> items=entry.getValue();
			
			int totalOrderPrices=items.stream().mapToInt(
					CartItem::getSellingPrice
					).sum();
			int totalMrpPrices=items.stream().mapToInt(
					CartItem::getMrpPrice
					).sum();
			int totalItem=items.stream().mapToInt(CartItem::getQuantity).sum();
			
			UserOrder createOrder=new UserOrder();
			createOrder.setUser(user);
			createOrder.setSellerId(sellerId);
			createOrder.setTotalMrpPrice(totalMrpPrices);
			createOrder.setTotalSellingPrice(totalOrderPrices);	
			createOrder.setTotalItem(totalItem);
			createOrder.setShippingAddress(shippingAddress);
			createOrder.setOrderStatus(OrderStatus.PENDING);
			createOrder.getPaymentDetails().setPaymentStatus(PaymentStatus.PENDING);
			
			UserOrder savedOrder=orderRepository.save(createOrder);
			
			List<OrderItem> orderItems=new ArrayList<>();
			for(CartItem item:items) {
				OrderItem orderItem=new OrderItem();
				orderItem.setOrder(savedOrder);
				orderItem.setMrpPrice(item.getMrpPrice());
				orderItem.setSellingPrice(item.getSellingPrice());
				orderItem.setProduct(item.getProduct());
				orderItem.setQuantity(item.getQuantity());
				orderItem.setSize(item.getSize());
				orderItem.setUserId(item.getUserId());
				OrderItem savedOrderItem=orderItemRepository.save(orderItem);
				
				savedOrder.getOrderItems().add(savedOrderItem);	
				orderItems.add(savedOrderItem);
			}
			savedOrder=orderRepository.save(savedOrder);			
			orders.add(savedOrder);
		}	
		return orders;
	}

	@Override
	public UserOrder findOrderById(Long id) throws Exception {
		return orderRepository.findById(id).orElseThrow(()->
		      new Exception("order not found with id :"+id)
				);
	}

	@Override
	public List<UserOrder> userOrderHistory(Long userId) {
		return orderRepository.findByUserId(userId);
	}

	@Override
	public List<UserOrder> sellerOrder(Long sellerId) {
		return orderRepository.findBySellerId(sellerId);
	}

	@Override
	public UserOrder updateOrderStatus(Long orderId, OrderStatus status) throws Exception {
		UserOrder order=findOrderById(orderId);
		order.setOrderStatus(status);
		return orderRepository.save(order);
	}

	@Override
	public UserOrder cancelOrder(Long orderId, User user) throws Exception {
		UserOrder order=findOrderById(orderId);
		if(order.getUser().getId()==user.getId()) {
			order.setOrderStatus(OrderStatus.CANCELLED);
		}
	    return orderRepository.save(order);
	}

	@Override
	public OrderItem findOrderItemById(Long id) throws Exception {
		 return orderItemRepository.findById(id).orElseThrow(()->
		 new Exception("order item not found with id :"+id)
				 );
	}

}
