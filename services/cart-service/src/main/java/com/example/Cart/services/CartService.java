package com.example.Cart.services;

import com.example.Cart.dto.ProductsDTO;
import com.example.Cart.entities.Cart;

import java.util.List;

public interface CartService {
    public Cart addCartDetails(Cart cart,ProductsDTO productsDTO);
    public List<Cart> getAllCartDetails();
    public void deleteCartById(String cartId);
    public void deleteUserCart(String userId);
    public List<ProductsDTO> getProductsByUserId(String userId);
    public boolean deleteCartItem(String userId, String ProductId);
    public void increaseCartQuantity(String cartId,String productId,double price);
    public void decreaseCartQuantity(String cartId,String productId,double price);


}
