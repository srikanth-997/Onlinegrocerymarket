package com.lancesoft.omg.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lancesoft.omg.exception.CategoriesAreEmpty;
import com.lancesoft.omg.exception.CategoryAlreadyExist;
import com.lancesoft.omg.exception.CustomException;
import com.lancesoft.omg.exception.InvalidEnteredPassword;
import com.lancesoft.omg.exception.InvalidSession;
import com.lancesoft.omg.exception.NotValidOTPException;
import com.lancesoft.omg.exception.ProductAlreadyExist;
import com.lancesoft.omg.exception.ProductsAreEmpty;
import com.lancesoft.omg.exception.UserAlreadyExist;

@RestControllerAdvice
public class ExceptionHandlers {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField() , error.getDefaultMessage());
		});
		return errorMap;
	}


	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(UserAlreadyExist.class)
	public Map<String, String> handleBusinessException(UserAlreadyExist userAlredayExist) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("errorMessage", userAlredayExist.getMessage());
		return errorMap;
	}
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(NotValidOTPException.class)
	public Map<String, String> handleBusinessException(NotValidOTPException notValidOTPException) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("errorMessage", notValidOTPException.getMessage());
		return errorMap;
	}
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(CategoryAlreadyExist.class)
	public Map<String, String> handleBusinessException(CategoryAlreadyExist categoryAlreadyExist) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("errorMessage", categoryAlreadyExist.getMessage());
		return errorMap;
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RuntimeException.class)
	public Map<String, String> handleBusinessException(RuntimeException runtimeException) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("errorMessage", runtimeException.getMessage());
		return errorMap;
	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ProductAlreadyExist.class)
	public Map<String, String> handleBusinessException(ProductAlreadyExist productAlreadyExist) {
		Map<String, String> errorMap = new HashMap<>();
		errorMap.put("errorMessage", productAlreadyExist.getMessage());
		return errorMap;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CategoriesAreEmpty.class)
	public Map<String, String>handleBussinessException(CategoriesAreEmpty categoriesAreEmpty)
	{
		Map<String, String> hashMap=new HashMap<>();
		hashMap.put("errorMessage", categoriesAreEmpty.getMessage());
		return hashMap;
	}

	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ProductsAreEmpty.class)
	public Map<String, String>handleBussinessException(ProductsAreEmpty productsAreEmpty)
	{
		Map<String, String> hashMap=new HashMap<>();
		hashMap.put("errorMessage", productsAreEmpty.getMessage());
		return hashMap;
	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidSession.class)
	public Map<String, String>handleBussinessException(InvalidSession invalidSession)
	{
		Map<String, String> hashMap=new HashMap<>();
		hashMap.put("errorMessage", invalidSession.getMessage());
		return hashMap;
	}
	
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	@ExceptionHandler(InvalidEnteredPassword.class)
	public Map<String, String>handleBussinessException(InvalidEnteredPassword invalidEnteredPassword)
	{
		Map<String, String>hashMap=new HashMap<String, String>();
		hashMap.put("errorMessage",invalidEnteredPassword.getMessage());
		return hashMap;
	}
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	@ExceptionHandler(CustomException.class)
	public Map<String, String>handleBussinessException(CustomException customException)
	{
		Map<String, String>hashMap=new HashMap<String, String>();
		hashMap.put("errorMessage",customException.getMessage());
		return hashMap;
	}

	

}
