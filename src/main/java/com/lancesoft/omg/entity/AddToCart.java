package com.lancesoft.omg.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;


@Data
public class AddToCart {
	
	private String prodId;
	private long qty;

}
