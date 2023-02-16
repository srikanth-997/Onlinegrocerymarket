package com.lancesoft.omg.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.lancesoft.omg.dto.AdressDto;
import com.lancesoft.omg.entity.AddressEntity;
import com.lancesoft.omg.entity.MyCart;
import com.lancesoft.omg.entity.MyCartLists;
import com.lancesoft.omg.entity.OrdersEntity;
import com.lowagie.text.DocumentException;

public interface UserDashBoardService {
	
	MyCart saveToCart(MyCart myCart,String userName);

	MyCartLists myCartList(String userName, MyCartLists myCartList);
	MyCartLists updateMyCart(String cartId, long qty,String userName,MyCartLists myCartList);
	 MyCartLists getMyCartList(String userName,MyCartLists myCartList);

	 AddressEntity saveAddress(String userName, AdressDto addressDto);
	 OrdersEntity placeAOrder(String userName, OrdersEntity entity);
	 OrdersEntity orderByPaytm(String userName, OrdersEntity entity);
	 String deleteMyCartList(String userName) ;
	void deleteCartItem(String itemId, String userName,MyCartLists myCartList );
   void geratePdf( HttpServletResponse response, String orderId) throws DocumentException, IOException;

   String deleteAllCartItems( String userName, MyCartLists myCartList);
}
