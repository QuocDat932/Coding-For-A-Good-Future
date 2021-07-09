package com.example.BuiQuocDat.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.BuiQuocDat.entity.Orders;
@Repository
public interface StatsRepository extends JpaRepository<Orders, Long>{
	@Query(value = "{CALL sp_getTotalPriceOneMonth(:month, :year)}", nativeQuery = true)
	String getTotalPriceByMonth(@Param("month") String month, 
			@Param("year") String year);
}
