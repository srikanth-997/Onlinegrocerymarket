package com.lancesoft.omg.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lancesoft.omg.dao.AdressDao;
import com.lancesoft.omg.dao.InventoryDao;
import com.lancesoft.omg.dao.MyCartDao;
import com.lancesoft.omg.dao.MyCartListDao;
import com.lancesoft.omg.dao.OrdersDao;
import com.lancesoft.omg.dao.ProductsDao;
import com.lancesoft.omg.dao.RegistrationDao;
import com.lancesoft.omg.dto.AdressDto;
import com.lancesoft.omg.entity.AddressEntity;
import com.lancesoft.omg.entity.Inventory;
import com.lancesoft.omg.entity.MyCart;
import com.lancesoft.omg.entity.MyCartLists;
import com.lancesoft.omg.entity.OrdersEntity;
import com.lancesoft.omg.entity.ProductsEntity;
import com.lancesoft.omg.entity.RegistrationEntity;
import com.lancesoft.omg.exception.CustomException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Header;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

@Service
@Transactional
public class UserDashBoardServiceImpl implements UserDashBoardService {

	@Autowired
	MyCartDao cartDao;
	@Autowired
	ProductsDao productsDao;
	@Autowired
	MyCartListDao myCartListDao;
	@Autowired
	AdressDao adressDao;
	@Autowired
	OrdersDao ordersdao;
	@Autowired
	RegistrationDao registrationDao;
	@Autowired
	InventoryDao inventoryDao;

	private static Logger logger = Logger.getLogger(UserDashBoardServiceImpl.class);

	@Override
	public MyCart saveToCart(MyCart myCart, String userName) {

		logger.info("Save to cart method start..");
	    String prodIdToBeAdded=	myCart.getEntity().getProdId();
		ProductsEntity productsEntity = productsDao.findByProdId(myCart.getEntity().getProdId());
		
	    List<MyCart> myCarts=	cartDao.findByUserId(userName);
	
	    for(int i=0; i<myCarts.size();i++)
	    {
	     String addedProd=	myCarts.get(i).getEntity().getProdId();
	     if(addedProd==prodIdToBeAdded)
	     {
	    	 throw new CustomException("Product is already exist in your cart");
	     }
	    	
	    }
		myCart.setUserId(userName);
		logger.info("End of save to cart method..");
		return cartDao.save(myCart);
	}

	public MyCartLists myCartList(String userName, MyCartLists myCartList) {
		logger.info("My cart list method start..");
		List<MyCart> cartList = cartDao.findByUserId(userName);
		if (cartList.isEmpty()) {
			throw new CustomException("Your Cart is empty , Please add to cart");
		}
		long totalCartPrice = 0;
		for (MyCart myCart : cartList) {
			totalCartPrice = totalCartPrice + myCart.getEntity().getPrice() * myCart.getQty();
		}

		if (myCartListDao.existsByUserName(userName)) {
			MyCartLists mycartList = myCartListDao.findByUserName(userName);
			String myCartListId = mycartList.getId();
			// myCartListDao.delete(mycartList);
			myCartList.setId(myCartListId);
		}

		myCartList.setUserName(userName);
		myCartList.setMyCartItems(cartList);
		myCartList.setTotalCost(totalCartPrice);
		logger.info("My cart list method end..");
		return myCartListDao.save(myCartList);

	}

	public MyCartLists updateMyCart(String cartId, long qty, String userName, MyCartLists myCartList) {

		logger.info("Update my cart start..");
		if(!(cartDao.existsByCartId(cartId)))
		{
			throw new CustomException("Given product does not exist in your Cart");
		}
		MyCart myCart = cartDao.findByCartId(cartId);
		myCart.setQty(qty);
		cartDao.save(myCart);

		logger.info("Update my cart end..");
		return this.myCartList(userName, myCartList);
	}

//	public MyCartLists updateMyCart(String cartId, long qty,String userName,MyCartLists myCartList)
//	{
//		//Save Updated quantity to the myCart
//		MyCart myCart= cartDao.findByCartId(cartId);
//		myCart.setQty(qty);
//		cartDao.save(myCart);
//		 
//		//Reflect the changes in myCartList that are made in mycart
//		MyCartLists cartLists=myCartListDao.findByUserName(userName);
//		long totalCartPrice=cartLists.getTotalCost();
//		
//		for(MyCart mycart:cartList)
//		{
//			if(cartLists.getMyCartItems().contains(myCart))
//			
//			totalCartPrice=totalCartPrice+myCart.getEntity().getPrice()*myCart.getQty();
//		}
//		
//		return this.myCartList(userName, myCartList);
//	}
	public MyCartLists getMyCartList(String userName, MyCartLists myCartList) {
		this.myCartList(userName, myCartList);
		logger.info("Get my cart list execution..");
		return myCartListDao.findByUserName(userName);
	}

	@Override
	public void deleteCartItem(String itemId, String userName, MyCartLists myCartList) {

		logger.info("Delete cart item start..");
		if (!(cartDao.existsById(itemId))) {
			throw new CustomException("Could not delete as it does not exist");
		}
		MyCart myCart = cartDao.findByCartId(itemId);

		cartDao.delete(myCart);
		// this.getMyCartList(userName, myCartList);

	}

	@Override
	public String deleteAllCartItems(String userName, MyCartLists myCartList) {

		logger.info("Delete cart item start..");
		// MyCart myCart = cartDao.findByCartId(itemId);
		MyCartLists myCartLists = myCartListDao.findByUserName(userName);
		List<MyCart> myCarts = myCartLists.getMyCartItems();

		MyCart myCart = myCarts.get(0);
	MyCart cart=	cartDao.findByCartId(myCart.getCartId());
		
		System.out.println(myCart);
		cartDao.delete(cart);
//		for(int i=0; i<myCarts.size();i++)
//		{
//			System.out.println(myCarts.get(i)+"<><><><><><><><><><<><><><><><<><>><<<<><>>><");
//		
//		}

		return "Deleted ";

	}

	public AddressEntity saveAddress(String userName, AdressDto addressDto) {
		logger.info("Save address method start..");
		ModelMapper mapper = new ModelMapper();
		AddressEntity addressEntity = new AddressEntity();
		if (!(addressDto == null)) {
			mapper.map(addressDto, addressEntity);
		}
		addressEntity.setUserName(userName);

		if (addressEntity.isCurrentAddress()) {
			List<AddressEntity> addressList = adressDao.findByUserName(userName);
			if (!(addressList == null)) {
				for (AddressEntity address : addressList) {
					if (address.isCurrentAddress()) {
						String id = address.getId();
						address.setId(id);
						address.setCurrentAddress(false);

						adressDao.save(address);
					}
				}

			}

		}

		logger.info("End of save address method..");
		return adressDao.save(addressEntity);
	}

	public OrdersEntity placeAOrder(String userName, OrdersEntity entity) {

		logger.info("Place order method start..");
		entity.setUserName(userName);

		List<AddressEntity> addressEntity = adressDao.findByUserName(userName);
		// adding an address to orders which is default address
		for (AddressEntity adEntity : addressEntity) {
			if (adEntity.isCurrentAddress()) {
				entity.setAddressEntity(adEntity);
			}
		}
		MyCartLists myCartLists = myCartListDao.findByUserName(userName);
		// setting myCarlist to orders entity
		entity.setCartList(myCartLists);
		DateTime date = new DateTime();
		date = date.plusHours(4);
		String stringDate = date.toString();

		entity.setDeliveryTime(stringDate.substring(11, 19) + " Date: " + stringDate.substring(0, 10));
		entity.setPaymentMode("Cash On Delivery");
		entity.setPaymentStatus("Not Paid");

		logger.info("Place order method end..");
		List<MyCart> cartList = entity.getCartList().getMyCartItems();
		OrdersEntity ordersEntity = ordersdao.save(entity);
		for (int i = 0; i < cartList.size(); i++) {

			MyCart cart = cartList.get(i);
			String orderProductName = cart.getEntity().getProdName();
			// System.out.println(orderProductName + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			long orderQuantity = cart.getQty();
			Inventory inventory = inventoryDao.findByProductName(orderProductName);
			long remainingQty = 0;
			if (inventory.getQuantity() >= orderQuantity) {
				remainingQty = inventory.getQuantity() - orderQuantity;
			} else
				throw new CustomException("Given order quantity is beyond our current stock!!");
			if (remainingQty == 0) {
				ProductsEntity prodEntity = productsDao.findByProdName(orderProductName);
				prodEntity.setStatus("Not Available");
			}

			inventory.setQuantity(remainingQty);
			inventoryDao.save(inventory);
		}
		logger.info("Place order method end>>>>>");
		System.out.println(myCartLists + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		myCartListDao.delete(myCartLists);
		return ordersEntity;
	}

	public String deleteMyCartList(String userName) {

		MyCartLists myCartLists = myCartListDao.findByUserName(userName);
		List<MyCart> myCarts = myCartLists.getMyCartItems();

		for (int i = 0; i < myCarts.size(); i++) {
			System.out.println(myCarts.get(i) + "<><><><><><><><><><<><><><><><<><>><<<<><>>><");
			cartDao.delete(myCarts.get(i));
		}
		// myCartListDao.delete(myCartLists);
		return "My Cart List deleted..";
	}

	public OrdersEntity orderByPaytm(String userName, OrdersEntity entity) {

		logger.info("Order by paytm method start..");
		entity.setUserName(userName);

		List<AddressEntity> addressEntity = adressDao.findByUserName(userName);
		// adding an address to orders which is default address
		for (AddressEntity adEntity : addressEntity) {
			if (adEntity.isCurrentAddress()) {
				entity.setAddressEntity(adEntity);
			}
		}
		MyCartLists myCartLists = myCartListDao.findByUserName(userName);
		// setting myCarlist to orders entity
		entity.setCartList(myCartLists);
		DateTime date = new DateTime();
		date = date.plusHours(4);
		String stringDate = date.toString();

		entity.setDeliveryTime(stringDate.substring(11, 19) + " Date: " + stringDate.substring(0, 10));
		entity.setPaymentMode("Paytm");
		entity.setPaymentStatus("Not Paid");

		logger.info("Order by paytm method start..");
		return ordersdao.save(entity);
	}

	public void geratePdf(HttpServletResponse response, String orderId) throws DocumentException, IOException {

		logger.info("Generate pdf method start..");
		if(!(ordersdao.existsByOrderId(orderId)))
		{
			throw new CustomException("Order not found");
		}
		OrdersEntity ordersEntityList = ordersdao.findByOrderId(orderId);
		RegistrationEntity registrationEntity = registrationDao.findByUserName(ordersEntityList.getUserName());

		AddressEntity addressEntity = ordersEntityList.getAddressEntity();
		MyCartLists cartList = ordersEntityList.getCartList();

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		Image image = Image.getInstance("omg.jpg");
		image.scaleAbsolute(120, 40);
		image.setAlignment(50);

		document.open();
		Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE);
		fontTitle.setSize(18);
		fontTitle.setColor(19, 124, 50);

		Header header = new Header("Invoice", "0");

		Paragraph paragraph = new Paragraph("Invoice #6006922 for Order # BPFVO­6012102­211215 : www.omg.com");
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);

		Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
		fontParagraph.setSize(12);
		// fontParagraph.setColor(206,33,33);

		Paragraph paragraph2 = new Paragraph("Invoice", fontTitle);
		paragraph2.setSpacingAfter(2f);
		paragraph2.setAlignment(Paragraph.ALIGN_CENTER);

		Paragraph paragraph4 = new Paragraph("Delivery Location :");
		Paragraph paragraph5 = new Paragraph(
				registrationEntity.getFirstName() + " " + registrationEntity.getLastName());
		Paragraph paragraph6 = new Paragraph(addressEntity.getHouseNumber() + "," + addressEntity.getLandmark());
		Paragraph paragraph7 = new Paragraph(addressEntity.getState() + "," + addressEntity.getPincode());

		Paragraph paragraph3 = new Paragraph("Order ID", fontParagraph);

		Table table = new Table(2, 6);
		table.setAlignment(5);
		table.setBorder(2);
		table.setPadding(3);
		Cell cell = new Cell("Invoice No");
		table.addCell(cell);
		table.addCell(String.valueOf(ordersEntityList.getOrderId()));
		table.addCell(paragraph3);
		table.addCell(String.valueOf(ordersEntityList.getOrderId()));
		table.addCell("Delivery Time");
		table.addCell(ordersEntityList.getDeliveryTime());
		table.addCell(new Paragraph("Final Total", fontParagraph));
		table.addCell(String.valueOf(cartList.getTotalCost()) + " INR");
		table.addCell("Payment By");
		table.addCell(ordersEntityList.getPaymentMode());
		table.addCell("Amount payable");
		table.addCell(String.valueOf(cartList.getTotalCost()) + " rs");
//		table.addCell("No.of items");
//		table.addCell("13");

		document.add(paragraph);
		document.add(paragraph2);
		document.add(image);
		document.add(paragraph4);
		document.add(paragraph5);
		document.add(paragraph6);
		document.add(paragraph7);
		document.add(table);

		document.close();

		logger.info("End of generate pdf..");
	}

}
