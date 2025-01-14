package com.example.shop.dto;

import com.example.shop.entity.ProductEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	private int productId;
	private String productName;
	private int productPrice;
	private int productQuantity;
    private String description;
    private String imageUrl; 
    private int categoryId;
    
    public ProductDTO(ProductEntity entity) {
    	this.productId = entity.getProductId();
    	this.productName = entity.getProductName();
    	this.productPrice = entity.getProductPrice();
    	this.description = entity.getDescription();
    	this.imageUrl = entity.getImageUrl();
    	this.categoryId = entity.getCategory().getCategoryId();
    }
}
