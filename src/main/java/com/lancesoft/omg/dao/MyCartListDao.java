package com.lancesoft.omg.dao;

import java.util.List;

import javax.persistence.OneToMany;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.MyCart;
import com.lancesoft.omg.entity.MyCartLists;

public interface MyCartListDao extends JpaRepository<MyCartLists, String> {



	MyCartLists findByUserName(String userName);

	boolean existsByUserName(String userName);

	void deleteByUserName(String userName);

}
