package com.plantshop.shop.controller.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.plantshop.shop.model.*;
import com.plantshop.shop.service.CartService;
import com.plantshop.shop.service.OrderService;
import com.plantshop.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CartService cartService;
	
	@GetMapping("/address")
	public String getOrderAddress(@AuthenticationPrincipal UserPrincipal principal, HttpSession session, Model model) {
		if (cartService.getCart(session).isEmptyCart()) {
			return "redirect:/";
		}
		Order order = orderService.getSessionOrder(session);
		if(orderService.getOrderStep(session)==1){
			Account user = userService.getUserById(principal.getId());
			order.setReceiverName(user.getFullName());
			order.setReceiverAddress(user.getAddress());
			order.setReceiverTel(user.getTel());
		}
		model.addAttribute("order", order);
		return "customer/order-address";
	}
	
	@PostMapping("/address")
	public String postOrderAddress(@AuthenticationPrincipal UserPrincipal principal, @ModelAttribute Order dto, HttpSession session) throws JsonProcessingException {
		Order order = orderService.getSessionOrder(session);
		order.setReceiverName(dto.getReceiverName());
		order.setReceiverTel(dto.getReceiverTel());
		order.setReceiverAddress(dto.getReceiverAddress());
		orderService.setOrderStep(session,2);
		return "redirect:/order/payment";
	}
	
	@GetMapping("/payment")
	public String getOrderPayment(HttpSession session, Model model) {
		if (orderService.getOrderStep(session)!=2) {
			return "redirect:/order/address";
		}
		Order order = orderService.getSessionOrder(session);
		Collection<CartItem> items = cartService.getCart(session).getItemsInCart();
		order.setOrderDetails(items.stream().map(item -> OrderDetails.create(order, item)).collect(Collectors.toList()));
		model.addAttribute("order", order);
		return "customer/order-payment";
	}
	
	@PostMapping("/payment")
	public String doOrder(@ModelAttribute Order dto, HttpSession session, @AuthenticationPrincipal UserPrincipal principal) {
		Order order = (Order) session.getAttribute("order");
		order.setExtra(dto.getExtra());
		order.setAccount(userService.getUserById(principal.getId()));
		order.setOrderDate(new Date(System.currentTimeMillis()));
		order.setStatus(Order.Status.PENDING.toString());
		orderService.createOrder(order);
		cartService.destroyCart(session);
		orderService.destroyOrder(session);
		return "redirect:/my-orders";
	}
}
