package com.example.BuiQuocDat.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BuiQuocDat.entity.*;

public interface TypesOfProductRepo extends JpaRepository<TypeOfProduct, Integer> {
	
	Optional<TypeOfProduct>  findBySlug(String slug);
}
