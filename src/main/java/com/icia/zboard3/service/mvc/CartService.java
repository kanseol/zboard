package com.icia.zboard3.service.mvc;

import java.util.*;

import javax.servlet.http.*;

import org.springframework.stereotype.*;

import com.icia.zboard3.dao.*;
import com.icia.zboard3.dto.*;
import com.icia.zboard3.entity.*;
import com.icia.zboard3.exception.*;

import lombok.*;

@RequiredArgsConstructor
@Service
public class CartService {
	private final ProductDao productDao;
	
	private Cart saveCartToSession(Cart cart, HttpSession session) {
		session.setAttribute("cart", cart);
		return cart;
	}
	
	private Cart getCart(HttpSession session) {
		if(session.getAttribute("cart")==null)
			return null;
		return (Cart)session.getAttribute("cart");
	}
	
	public Cart list(HttpSession session) {
		return getCart(session);
	}
	
	public Cart addCart(Integer pno, HttpSession session) {
		Cart cart = getCart(session);
		Product product = productDao.findById(pno).orElseThrow(ShopJob.ProductNotFoundException::new);
		CartItem cartItem = new CartItem(pno, product.getImage(), product.getName(), product.getPrice(), 1);
		if(cart==null) 
			cart = new Cart(cartItem, product);
		else
			cart.addCart(cartItem);
		return saveCartToSession(cart, session);
	}
	
	public Map<String, Object> change(Integer pno, Boolean isIncrease, HttpSession session) {
		Cart cart = getCart(session);
		CartItem cartItem = null;
		if(isIncrease==true) {
			Product product = productDao.findById(pno).orElseThrow(ShopJob.ProductNotFoundException::new);
			cartItem = cart.increase(pno, product);
		} else 
			cartItem = cart.decrease(pno);
		saveCartToSession(cart, session);
		Map<String, Object> map = new HashMap<>();
		map.put("cartItem", cartItem);
		map.put("totalPrice", cart.getTotalPrice());
		return map;
	}

	public Cart delete(List<Integer> pnos, HttpSession session) {
		Cart cart = getCart(session);
		cart.delete(pnos);
		return saveCartToSession(cart, session);
	}

	
}