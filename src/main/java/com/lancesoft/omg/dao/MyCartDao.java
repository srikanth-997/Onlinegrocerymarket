package com.lancesoft.omg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.MyCart;
import com.lancesoft.omg.entity.ProductsEntity;

public interface MyCartDao extends JpaRepository<MyCart, String>{

	

	
    MyCart findByCartId(String cartId);
	boolean existsByEntity(ProductsEntity productsEntity);

	List<MyCart> findByUserId(String userName);
	void deleteById(String itemId);
	boolean existsByCartId(String cartId);
	
}
