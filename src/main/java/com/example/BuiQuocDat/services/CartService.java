package com.example.BuiQuocDat.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.BuiQuocDat.dto.*;
import com.example.BuiQuocDat.entity.Users;

public interface CartService {
	
	cartDto updateProduct(cartDto cart, long idProduct, int quantity, boolean isUpdate);
	Integer getTotalQuantity(cartDto cart);
	Double getTotalPrice(cartDto cart);
	
	//void myshop(cartDto cart);
	HashMap<Long, cartDetailDto> myshop(cartDto cart);
	
	void insert(cartDto cart, Users user, String address, String phone) throws Exception;
}
