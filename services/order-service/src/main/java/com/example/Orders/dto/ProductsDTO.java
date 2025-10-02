package com.example.Orders.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;


@Data
public class ProductsDTO {

    @Id
    private String productId;
    private String productName;
    private String description;
    private Double price;
    private Integer productQuantity;
    private String image;
    private String category;
    private String brand;
    private String merchantId;

}