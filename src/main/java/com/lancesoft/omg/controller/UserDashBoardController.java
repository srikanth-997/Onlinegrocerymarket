package com.lancesoft.omg.controller;

import java.io.IOException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lancesoft.omg.dao.AdressDao;
import com.lancesoft.omg.dao.ProductsDao;
import com.lancesoft.omg.dto.AdressDto;
import com.lancesoft.omg.entity.AddToCart;
import com.lancesoft.omg.entity.AddressEntity;
import com.lancesoft.omg.entity.MyCart;
import com.lancesoft.omg.entity.MyCartLists;
import com.lancesoft.omg.entity.OrdersEntity;
import com.lancesoft.omg.entity.ProductsEntity;
import com.lancesoft.omg.exception.CustomException;
import com.lancesoft.omg.service.AdminRegistrationServiceImpl;
import com.lancesoft.omg.service.UserDashBoardService;
import com.lowagie.text.DocumentException;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class UserDashBoardController {
	@Autowired
	ProductsDao dao;
	@Autowired
	AdminRegistrationServiceImpl adminRegistrationServiceImpl;
	@Autowired
	UserDashBoardService UserDashBoardServiceImpl;

	@PostMapping("/user/addToCart")
	public ResponseEntity<MyCart> saveToCart(@RequestBody AddToCart addToCart, MyCart cart,
			HttpServletRequest httpServletRequest) {
		ProductsEntity entity = dao.findByProdId(addToCart.getProdId());
		if (entity.getStatus().equals("not available")) {
			throw new CustomException("Product is not available");
		}
		cart.setEntity(entity);
		cart.setQty(addToCart.getQty());

		String userName = adminRegistrationServiceImpl.getMyToken(httpServletRequest);
		
		return new ResponseEntity<MyCart>(UserDashBoardServiceImpl.saveToCart(cart, userName), HttpStatus.OK);
	}

	@GetMapping("/user/getMyCartList")
	public ResponseEntity<MyCartLists> getMyCartList(HttpServletRequest httpServletRequest, MyCartLists cartList) {
		String userName = adminRegistrationServiceImpl.getMyToken(httpServletRequest);
		return new ResponseEntity<MyCartLists>(UserDashBoardServiceImpl.myCartList(userName, cartList), HttpStatus.OK);
	}

	@PutMapping("/user/updateMyCart")
	public ResponseEntity<MyCartLists> upDateMyCart(@RequestParam String cartId, @RequestParam long qty,HttpServletRequest httpServletRequest,MyCartLists myCartList)
	{
		String userName=adminRegistrationServiceImpl.getMyToken(httpServletRequest);
		
		return new ResponseEntity<MyCartLists>(UserDashBoardServiceImpl.updateMyCart(cartId, qty, userName, myCartList),HttpStatus.OK);
		
	}
	@GetMapping("/user/getAllCartList")
	public ResponseEntity<MyCartLists> getMyCar(String userName, HttpServletRequest httpServletRequest,MyCartLists myCartList)
	{
		userName=adminRegistrationServiceImpl.getMyToken(httpServletRequest);
		return new ResponseEntity<MyCartLists>(UserDashBoardServiceImpl.getMyCartList(userName,myCartList),HttpStatus.OK);
	}
	@DeleteMapping("/user/delete")
    public ResponseEntity<String> deleteCartItem(@RequestParam String cartId,HttpServletRequest httpServletRequest,MyCartLists myCartList)  {
		
		String userName=adminRegistrationServiceImpl.getMyToken(httpServletRequest);

		UserDashBoardServiceImpl.deleteCartItem(cartId,userName,myCartList);
        return new ResponseEntity<String>("s", HttpStatus.OK);
    }
	@PostMapping("/user/addAddress")
	public ResponseEntity<AddressEntity> saveAddress(HttpServletRequest httpServletRequest, @RequestBody AdressDto adressDto)
	{
		String userName=adminRegistrationServiceImpl.getMyToken(httpServletRequest);
		return new ResponseEntity<AddressEntity>(UserDashBoardServiceImpl.saveAddress(userName, adressDto), HttpStatus.OK);
	}
	@PostMapping("/user/payWithCod")
	public ResponseEntity<OrdersEntity> playAOrder(HttpServletRequest httpServletRequest, OrdersEntity entity)
	{
		String userName=adminRegistrationServiceImpl.getMyToken(httpServletRequest);
		
		return new ResponseEntity<OrdersEntity>(UserDashBoardServiceImpl.placeAOrder(userName,entity), HttpStatus.OK);
	}
	@PostMapping("/user/generatePdf")
	public ResponseEntity<String> generatePdf( @RequestParam String ordersId, HttpServletResponse response) throws DocumentException, IOException
	{
		UserDashBoardServiceImpl.geratePdf(response, ordersId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	@DeleteMapping("/user/deleteMyCartList")
    public ResponseEntity<String> deleteMyCartIList(HttpServletRequest httpServletRequest, MyCartLists cartLists)  {
		
		String userName=adminRegistrationServiceImpl.getMyToken(httpServletRequest);

	String deleted=	UserDashBoardServiceImpl.deleteAllCartItems(userName,cartLists);
        return new ResponseEntity<String>(deleted, HttpStatus.OK);
    }
}
