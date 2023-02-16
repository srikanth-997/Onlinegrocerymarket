package com.lancesoft.omg.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lancesoft.omg.dao.AdminDashBoardDao;
import com.lancesoft.omg.dao.InventoryDao;
import com.lancesoft.omg.dto.CategoriesDto;
import com.lancesoft.omg.entity.CategoriesEntity;
import com.lancesoft.omg.entity.Inventory;
import com.lancesoft.omg.exception.CategoriesAreEmpty;
import com.lancesoft.omg.exception.CategoryAlreadyExist;
import com.lancesoft.omg.exception.CustomException;

@Service
public class AdminDashBoardServiceImpl implements AdminDashBoardService {

	@Autowired
	private AdminDashBoardDao adminDashBoardDao;
	@Autowired
	private InventoryDao inventoryDao;

	private static Logger logger = Logger.getLogger(AdminDashBoardServiceImpl.class);

	public Boolean addCategory(CategoriesDto categoriesDto) {
		logger.info("Admin Dash Board add category started..");
		ModelMapper modelMapper = new ModelMapper();
		CategoriesEntity categoriesEntity = new CategoriesEntity();

		// Checking categories entities are initialized or not
		if (categoriesDto == null) {
			throw new RuntimeException("null found in Categories please check");
		} else if (adminDashBoardDao.existsByCatName(categoriesDto.getCatName())) {
			throw new CategoryAlreadyExist("Category Already exists");
		}

		modelMapper.map(categoriesDto, categoriesEntity);
		logger.info("Admin Dash Board add category end..");
		try {
			adminDashBoardDao.save(categoriesEntity);
		} catch (Exception ex) {
			return false;
		}

		return true;
	}

	public List<CategoriesEntity> getAllCategories() {
		logger.info("Admin Dash Board get all categories start ");
		List<CategoriesEntity> categories = adminDashBoardDao.findAll();
		if (!categories.isEmpty()) {
			logger.info("Admin Dash Board get all categories end");
			return categories;
		} else
			throw new CategoriesAreEmpty("Categories are empty ");

	}

	public Inventory addInventory(Inventory inventory) {
		logger.info("Admin dash board add Inventory method started");
		boolean isProdExist = inventoryDao.existsByProductName(inventory.getProductName());
		if (isProdExist) {
			throw new CustomException("Product is already exist");
		} else if (inventory == null) {
			throw new CustomException("Please enter item details to add into inventory");
		}
		logger.info("Admin dash board add Inventory method ended..");

		return inventoryDao.save(inventory);
	}

	public List<Inventory> getAllInventory() {
		logger.info("Admin Dash Board start Inventory end");
		List<Inventory> inventory = inventoryDao.findAll();
		if (!inventory.isEmpty()) {
			logger.info("Admin Dash Board get all Inventory end");
			return inventory;
		} else
			throw new CustomException("Inventories are empty ");

	}

}
