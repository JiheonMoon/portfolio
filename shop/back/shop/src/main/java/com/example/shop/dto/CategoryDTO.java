package com.example.shop.dto;

import com.example.shop.entity.CategoryEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
	private int categoryId;
	private String categoryName;
	
	public CategoryDTO(CategoryEntity entity) {
		this.categoryId = entity.getCategoryId();
		this.categoryName = entity.getCategoryName();
	}
}
