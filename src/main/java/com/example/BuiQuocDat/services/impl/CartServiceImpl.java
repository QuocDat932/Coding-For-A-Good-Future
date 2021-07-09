package com.example.BuiQuocDat.services.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.persistence.criteria.Order;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.BuiQuocDat.dto.*;
import com.example.BuiQuocDat.entity.*;
import com.example.BuiQuocDat.services.*;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private ProductsServices productService;
	@Autowired
	private Orderservices orderservices;
	@Autowired
	private OrderDetailServices orderdetailServices;
	
	@Override
	public cartDto updateProduct(cartDto cart, long idProduct, int quantity, boolean isUpdate) {
		Products product = productService.findById(idProduct);
		HashMap<Long, cartDetailDto> listDetail = cart.getListDetail();
		
		if (!listDetail.containsKey(idProduct)) {
			cartDetailDto cartDetail = new cartDetailDto();
			cartDetail.setIdProduct(product.getId());
			cartDetail.setImgUrl(product.getImgUrl());
			cartDetail.setName(product.getName());
			cartDetail.setPrice(product.getPrice());
			cartDetail.setQuantity(quantity);
			cartDetail.setSlug(product.getSlug());
			listDetail.put(idProduct, cartDetail);
		} else if (quantity > 0){
			if (isUpdate) {
				listDetail.get(idProduct).setQuantity(quantity);
			} else {
				Integer oldQuantity = listDetail.get(idProduct).getQuantity();
				listDetail.get(idProduct).setQuantity(oldQuantity + quantity);
			}
		} else {
			listDetail.remove(idProduct);
		}
		
		cart.setTotalPrice(this.getTotalPrice(cart));
		cart.setTotalQuantity(this.getTotalQuantity(cart));
		return cart;
	}

	@Override
	public Integer getTotalQuantity(cartDto cart) {
		HashMap<Long, cartDetailDto> cartDetail = cart.getListDetail();
		Integer totalQuantity = 0;
		for (cartDetailDto cartDetailDto : cartDetail.values()) {
			totalQuantity += cartDetailDto.getQuantity();
			
		}
		
		return totalQuantity;
	}

	@Override
	public Double getTotalPrice(cartDto cart) {
		HashMap<Long, cartDetailDto> cartDetail = cart.getListDetail();
		Double totalPrice = 0D;
		for (cartDetailDto cartDetailDto : cartDetail.values()) {
			Products product = productService.findById(cartDetailDto.getIdProduct());
			totalPrice += product.getPrice() * cartDetailDto.getQuantity();
		}
		
		return totalPrice;
	}
	
	@Override
	public HashMap<Long, cartDetailDto> myshop(cartDto cart) {
		HashMap<Long, cartDetailDto> myshop = cart.getListDetail();
		for(cartDetailDto x : myshop.values()) {
			System.out.println(x.getName());
			System.out.println(x.getSlug());
		}
		return myshop;
	}
	@Override
	@Transactional
	public void insert(cartDto cart, Users user, String address, String phone) throws Exception {
		Orders order = new Orders();
		order.setAddress(address);
		order.setPhone(phone);
		order.setUser(user);
		
		try {
			
			Orders orderReturn = orderservices.insert(order);
			
			if(!ObjectUtils.isEmpty(orderReturn)) {
				for(cartDetailDto cartDetailDto : cart.getListDetail().values()) {
					cartDetailDto.setIdOrder(orderReturn.getId());
					orderdetailServices.insert(cartDetailDto);
					Products product = productService.findById(cartDetailDto.getIdProduct());
					Integer  newQuantity = product.getQuantity() - cartDetailDto.getQuantity();
					productService.updateQuantity(newQuantity, cartDetailDto.getIdProduct());
				}
			} else {
				throw new SQLException("Cannot Insert To DataBase1");
			}
			
			
		} catch (Exception e) {
			throw new SQLException("Cannot Insert To DataBase2");
		}
		
	}


}
