package com.example.BuiQuocDat.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.BuiQuocDat.entity.*;

@Repository
public interface RolesRepository extends JpaRepository<roles, Long> {
	roles findById(Integer id);
}
