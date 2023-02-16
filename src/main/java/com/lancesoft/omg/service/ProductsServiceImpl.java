package com.lancesoft.omg.service;

import java.util.List;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lancesoft.omg.dao.AdminDashBoardDao;
import com.lancesoft.omg.dao.ProductsDao;
import com.lancesoft.omg.dto.ProductsDto;
import com.lancesoft.omg.entity.CategoriesEntity;
import com.lancesoft.omg.entity.ProductsEntity;
import com.lancesoft.omg.exception.CategoriesAreEmpty;
import com.lancesoft.omg.exception.CustomException;
import com.lancesoft.omg.exception.ProductAlreadyExist;
import com.lancesoft.omg.exception.ProductsAreEmpty;

@Service
public class ProductsServiceImpl implements ProductsService {
 	@Autowired
	private ProductsDao productsDao;

	@Autowired
	private AdminDashBoardDao adminDashBoardDao;

	private static Logger logger = Logger.getLogger(ProductsServiceImpl.class);

	@Override
	public ProductsEntity addProducts(ProductsDto productsDto) throws ProductAlreadyExist {
		logger.info("Add products method start..");
		ModelMapper modelMapper = new ModelMapper();
		ProductsEntity productsEntity = new ProductsEntity();
		System.err.println(productsDto);
		if (productsDto == null) {
			throw new RuntimeException("null found in Products please check");
		} else if (productsDao.existsByProdName(productsDto.getProdName())) {
			throw new ProductAlreadyExist("Product Already exists");
		}

		modelMapper.map(productsDto, productsEntity);

		CategoriesEntity categoriesEntity = adminDashBoardDao
				.findByCatName(productsEntity.getCategoriesEntity().getCatName());
		productsEntity.setCategoriesEntity(categoriesEntity);
		logger.info("End of add products..");
		return productsDao.save(productsEntity);

	} 

	public List<ProductsEntity> getAllProducts() {
		logger.info("Get all products start..");
		List<ProductsEntity> products = productsDao.findAll();

		if (!products.isEmpty()) {
			logger.info("Get all products end..");
			return products;
		} else
			throw new ProductsAreEmpty("Products are empty ");

	}

	public List<ProductsEntity> getCategory(String catName) {
		logger.info("Start of get Category method..");
		CategoriesEntity categoriesEntity = adminDashBoardDao.findByCatName(catName);
		String catId = categoriesEntity.getCatId();
		List<ProductsEntity> productsEntity = productsDao.findByCategoriesEntity(categoriesEntity);
		if (!productsEntity.isEmpty()) {
			logger.info("End of get category method..");
			return productsEntity;
		} else
			throw new ProductsAreEmpty("Given category products are not available");

	}

	public ProductsEntity findProdByName(String prodName) {
		if(!(productsDao.existsByProdName(prodName)))
		{
			throw new CustomException("Product does not exists");
		}
		ProductsEntity productsEntity = productsDao.findByProdName(prodName);

		return productsEntity;

	}

	@Override
	public ProductsEntity updateProd(ProductsEntity productsEntity) {
		
		if(productsEntity==null)
		{
			throw new CustomException("Products details should not be null");
		}
		
		
		return productsDao.save(productsEntity);
	}

}
