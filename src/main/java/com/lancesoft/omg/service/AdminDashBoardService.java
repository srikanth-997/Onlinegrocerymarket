package com.lancesoft.omg.service;

import java.util.List;

import com.lancesoft.omg.dto.CategoriesDto;
import com.lancesoft.omg.entity.CategoriesEntity;
import com.lancesoft.omg.entity.Inventory;

public interface AdminDashBoardService {
	
	 Boolean addCategory(CategoriesDto categoriesDto);
	
	 List <CategoriesEntity> getAllCategories();
	Inventory addInventory(Inventory inventory);
	List<Inventory> getAllInventory();
	

}
