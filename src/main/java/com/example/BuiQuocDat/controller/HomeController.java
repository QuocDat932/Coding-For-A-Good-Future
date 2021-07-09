package com.example.BuiQuocDat.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.BuiQuocDat.constant.SessionConst;
import com.example.BuiQuocDat.dto.cartDetailDto;
import com.example.BuiQuocDat.dto.cartDto;
import com.example.BuiQuocDat.entity.Products;
import com.example.BuiQuocDat.entity.Users;
import com.example.BuiQuocDat.entity.roles;
import com.example.BuiQuocDat.jwt.JwtTokenProvider;
import com.example.BuiQuocDat.services.CartService;
import com.example.BuiQuocDat.services.ProductsServices;
import com.example.BuiQuocDat.services.RolesServices;
import com.example.BuiQuocDat.services.UsersService;
import com.example.BuiQuocDat.jwt.CustomUser;
@Controller(value = "controllerOfUser")
public class HomeController {
	@Autowired
	ServletContext app;
	@Autowired
	private UsersService userservices;
	@Autowired
	private ProductsServices productsservices;
	BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
	@Autowired
	private CartService cartServices;
	@Autowired
	private RolesServices rolesservices;

	@Autowired
	private AuthenticationManager  authenManager;
	@Autowired
    private JwtTokenProvider tokenProvider;
	@GetMapping({ "/home", "/" })
	public String DoGetloginHome(Model model, HttpSession session) {
		cartDto currentCart = (cartDto) session.getAttribute("currentCart");
		if(currentCart == null) {
			session.setAttribute("currentCart", new cartDto());
		}
		List<Products> sp = productsservices.findAllAvailable();
		model.addAttribute("listProduct", sp);
		return "home/index";
	}

	@GetMapping("/home/login")
	public String GetLogin(Model model) {
		model.addAttribute("user", new Users());
		return "home/login";
	}

	@PostMapping("/home/login")
	public String PostloginHome(Model model, @ModelAttribute("user") @Validated Users userlogin,
			// Errors errors,
			HttpSession session
	// HttpSession session2
	) {
		UsernamePasswordAuthenticationToken authenInfo = new UsernamePasswordAuthenticationToken(
				userlogin.getUsername(),userlogin.getHashPassword());
		Authentication authentication = authenManager.authenticate(authenInfo);
		//Authentication authentication = authenManager.authenticate(authenInfo);
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		Users userResponse = userservices.findByUserName(userlogin.getUsername());
		System.out.println(userResponse.getFullname());
		boolean loginStatus = bcrypt.matches(userlogin.getHashPassword()
											, userResponse.getHashPassword());
		System.out.println(loginStatus);
		if (userResponse != null && loginStatus) {
			roles RoleUserResponse = userResponse.getRole();
			// tạo Sesstion tại Server
			session.setAttribute(SessionConst.CURRENT_USER, userResponse);
			session.setAttribute(SessionConst.CURRENT_ROLE, RoleUserResponse);
			System.out.println(RoleUserResponse.getDescription());
			//model.addAttribute("role",RoleUserResponse);
			//return "home/index3";
			session.setAttribute(SessionConst.JWT, tokenProvider.generateToken(customUser));
			session.setAttribute(SessionConst.CURRENT_USER, userResponse);
			return "redirect:/home";
		} else {
			String message = "Login Failed, please try again!";
			model.addAttribute("message", message);
			System.out.println(message);
			return "home/login";
		}
	}

	@GetMapping("/home/logout")
	public String homeLogout(Model model, HttpSession session) {
		session.removeAttribute(SessionConst.CURRENT_USER);
		session.removeAttribute(SessionConst.CURRENT_ROLE);
		session.removeAttribute(SessionConst.CURRENT_CART);
		return "redirect:/home";
	}

	@GetMapping("/registration")
	public String doGetRegistration(Model model) {
		model.addAttribute("newUser", new Users());
		return "home/registration";
	}
	@PostMapping("/registration")
	public String doPostRegistration(Model model, @ModelAttribute("newUser") @Validated Users newUser) {
		try {
			newUser.setIsDeleted(true);
			newUser.setHashPassword(bcrypt.encode(newUser.getHashPassword()));
			roles role = rolesservices.findByUserID(2);
			newUser.setRole(role);
			System.out.println(newUser.getHashPassword());
			userservices.addUser(newUser);
			System.out.println("Lưu Thành Công !");
			return "redirect:/home";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/registration";
		}
		
	}			  
	@GetMapping("/home/management")
	public String doGetManagement(Model model,
								 @ModelAttribute("Products") Products getproduct) {
		List<Products> sp = productsservices.findAllAvailable();
		Products product = new Products();
		model.addAttribute("myshop", sp);
		getproduct.setId((long) -1);
		model.addAttribute("Products", getproduct);
		return "home/management";
	}	
	@GetMapping("/home/myshop")
	public String doGetMyShop(Model model, HttpSession session) {
		cartDto currentcart = (cartDto) session.getAttribute("currentCart");
		if(currentcart != null) {
			cartServices.myshop(currentcart);
		}
		HashMap<Long, cartDetailDto> myshop = cartServices.myshop(currentcart);
		model.addAttribute("myshop", myshop);
		model.addAttribute("cart",currentcart);
		
		return "home/management";
	}
	@GetMapping("/home/getproduct")
	public String doGetproduct(Model model,
							   @RequestParam("productid") Long productid,
							   @ModelAttribute("Products") Products Products
							   ) throws IllegalMonitorStateException, IOException{
		if(productid == null) {
			System.out.println("NULL");
			
		}
		else {
		Products product = productsservices.findById(productid);
		
		model.addAttribute("Products",product);
		
		System.out.println(product.getName());
		}
		List<Products> sp = productsservices.findAllAvailable();
		model.addAttribute("myshop", sp);
		//return "redirect:/home/management";
		return "/home/management";
	}
	
	@PostMapping("/home/upload")
	public String doGetupload(Model model,
							  @RequestParam("productid") Long productid,
							  @ModelAttribute("Products") Products product,
							  @RequestParam("attach") MultipartFile attach
							  
			) throws IllegalStateException, IOException{
		String fileName="";
		if(!attach.isEmpty()) {
		fileName = attach.getOriginalFilename();
		File file = new File(app.getRealPath("/img"+fileName));
		//E:\Springboot\SpringToolSuite\Assignment\src\main\webapp\imgdat.jpg
		attach.transferTo(file);
		System.out.println(fileName);
		}
		if(productid != -1) {
			Products newproduct = productsservices.findById(productid);
			if(fileName != null) {
				newproduct.setImgUrl(fileName);
			}
			newproduct.setQuantity(product.getQuantity());
			newproduct.setDescription(product.getDescription());
			newproduct.setName(product.getName());
			newproduct.setPrice(product.getPrice());
			//System.out.println(product.getTypeOfProduct());
			//System.out.println(product.getQuantity());
			productsservices.SaveAndUpdate(newproduct);
			System.out.println("Update Thành Công");
			return "redirect:/home/management";	
		}
		else {
			// Save and push
			System.out.println("NULL : "+ productid);
			return "redirect:/home/management";
		}
	}
}
