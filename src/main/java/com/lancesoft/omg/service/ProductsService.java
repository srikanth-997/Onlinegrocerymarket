package com.lancesoft.omg.service;


import java.util.List;

import com.lancesoft.omg.dto.ProductsDto;
import com.lancesoft.omg.entity.ProductsEntity;


public interface ProductsService {
	
	ProductsEntity addProducts(ProductsDto productsDto);
	List <ProductsEntity> getAllProducts();
	List<ProductsEntity> getCategory(String catName);
	ProductsEntity findProdByName(String prodName);
	ProductsEntity updateProd(ProductsEntity productsEntity);

}
