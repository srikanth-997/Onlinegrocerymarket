package com.lancesoft.omg.dto;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table(name="categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesDto {
	private String catId;
	private String catName;
}
