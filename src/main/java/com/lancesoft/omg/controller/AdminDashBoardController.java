package com.lancesoft.omg.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lancesoft.omg.dto.CategoriesDto;
import com.lancesoft.omg.dto.ProductsDto;
import com.lancesoft.omg.entity.CategoriesEntity;
import com.lancesoft.omg.entity.Inventory;
import com.lancesoft.omg.entity.ProductsEntity;
import com.lancesoft.omg.service.AdminDashBoardService;
import com.lancesoft.omg.service.ProductsService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminDashBoardController {

	@Autowired
	AdminDashBoardService adminDashBoardServiceImpl;
	@Autowired
	ProductsService productsServiceImpl;

	@PostMapping("/addCategory")
	public ResponseEntity<Boolean> addCategory(@RequestBody CategoriesDto categoriesDto) {
		System.err.println("add category called");
		if (adminDashBoardServiceImpl.addCategory(categoriesDto)) {
			return new ResponseEntity(true, HttpStatus.CREATED);
		}

		return new ResponseEntity(false, HttpStatus.EXPECTATION_FAILED);
	}

	@PostMapping("/addProducts")
	public ResponseEntity<ProductsEntity> addCategory(@RequestBody @Valid ProductsDto productsDto) {
		System.err.println("add category called");
		return new ResponseEntity(productsServiceImpl.addProducts(productsDto), HttpStatus.CREATED);
	}

	@GetMapping("/getCategories")
	public ResponseEntity<CategoriesEntity> getCategories() {
		System.err.println("getcat called");
		return new ResponseEntity(adminDashBoardServiceImpl.getAllCategories(), HttpStatus.OK);
	}

	@GetMapping("/getAllProducts")
	public ResponseEntity<ProductsEntity> getProducts() {
		return new ResponseEntity(productsServiceImpl.getAllProducts(), HttpStatus.OK);
	}

	@GetMapping("/getOneCategory")
	public ResponseEntity<ProductsEntity> getProducts(@RequestParam("catName") String catName) {

		return new ResponseEntity(productsServiceImpl.getCategory(catName), HttpStatus.OK);
	}

	@PostMapping("/addInventory")
	public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory) {

		return new ResponseEntity(adminDashBoardServiceImpl.addInventory(inventory), HttpStatus.OK);
	}

	@GetMapping("/getAllInventory")
	public ResponseEntity<Inventory> getAllInventory() {
		return new ResponseEntity(adminDashBoardServiceImpl.getAllInventory(), HttpStatus.OK);
	}

	@GetMapping("/getProdByProdName")
	public ResponseEntity<ProductsEntity> getProd(@RequestParam("prodName") String prodName) {
		return new ResponseEntity<ProductsEntity>(productsServiceImpl.findProdByName(prodName), HttpStatus.OK);
	}

	@PutMapping("/updateProd")
	public ResponseEntity<ProductsEntity> updateProd(@RequestBody ProductsEntity productsEntity) {
		return new ResponseEntity<ProductsEntity>(productsServiceImpl.updateProd(productsEntity), HttpStatus.OK);
	}

}
