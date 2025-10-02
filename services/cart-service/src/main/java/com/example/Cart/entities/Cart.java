package com.example.Cart.entites;


import com.example.Cart.dto.ProductsDTO;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@ToString
@Document(collection = Cart.COLLECTION_NAME3)
public class Cart {
    public static final String COLLECTION_NAME3 = "Cart";

    @Id
    private String cartId;
    private String productId;
    private String userId;
    private List<ProductsDTO> productsDTOList;


}
