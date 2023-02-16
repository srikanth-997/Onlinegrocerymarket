package com.lancesoft.omg.dto;

import java.util.Date;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;

import com.lancesoft.omg.entity.CategoriesEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table(name="products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsDto {

	
	private String prodId;
	@NotBlank(message = "Product name should not be null")
	private String prodName;
	private String description;
	@CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
	private Date AddOnDate;
	@Min(10)
	private long price;
	@NotBlank(message = "imageUrl should not be null")
	private String imageUrl; 
	@NotBlank(message = "status  should not be null")
	private String status;
	@ManyToOne
	@JoinColumn
	private CategoriesEntity categoriesEntity;

}
