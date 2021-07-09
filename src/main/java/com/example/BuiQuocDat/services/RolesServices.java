package com.example.BuiQuocDat.services;
import java.util.*;
import com.example.BuiQuocDat.entity.*;
public interface RolesServices  {
	List<roles> findAll();
	
	roles findByUserID(Integer roles);
}
