package com.example.BuiQuocDat.api;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BuiQuocDat.entity.Users;
import com.example.BuiQuocDat.services.UsersService;


@RestController
public class Api {
	
	@Autowired
	private UsersService repo; 
	@GetMapping("/api/print")
	public ResponseEntity<?> doGetAPI(@RequestParam("id") Long userid) {
		System.out.println("*****************************"+userid);
		Users findx = repo.findByid(userid);
		//System.out.println(findx.getFullname());
		return ResponseEntity.ok(findx);
	}
	@GetMapping("/api/print/all")
	public ResponseEntity<?> doGetAllUser() {
		List<Users> list = repo.findAll();
		//System.out.println(findx.getFullname());
		return ResponseEntity.ok(list);
	}
	@GetMapping("/api/delete")
	public ResponseEntity<?> doGetDelate(@RequestParam("id") Long userid) {
		System.out.println("Deleting...");
		Users findx = repo.findByid(userid);
		repo.delete(findx);
		List<Users> list = repo.findAll();

		return ResponseEntity.ok(list);
	}
}