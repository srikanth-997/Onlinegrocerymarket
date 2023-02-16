package com.lancesoft.omg.dao;

import org.springframework.data.jpa.repository.JpaRepository;


import com.lancesoft.omg.entity.CategoriesEntity;

public interface AdminDashBoardDao extends JpaRepository<CategoriesEntity, String> {
	
	 boolean existsByCatName(String catName);

	CategoriesEntity findByCatName(String catName);

}
