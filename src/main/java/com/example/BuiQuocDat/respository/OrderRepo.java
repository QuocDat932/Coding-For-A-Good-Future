package com.example.BuiQuocDat.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BuiQuocDat.entity.Orders;
@Repository
public interface OrderRepo extends JpaRepository<Orders, Long>{

}
