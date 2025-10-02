package com.example.Cart.services.impl;

import com.example.Cart.dto.ProductsDTO;
import com.example.Cart.entities.Cart;
import com.example.Cart.repository.CartRepository;
import com.example.Cart.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Override
    public Cart addCartDetails(Cart cart,ProductsDTO productsDTO)
    {
        Optional<Cart> temp = cartRepository.findById(cart.getUserId());
        log.debug("Found existing cart: {}", temp.isPresent());
        productsDTO.setProductQuantity(1);
        List<ProductsDTO> productList;

        if(temp.isPresent())
        {
            Cart tempCart = temp.get();
            log.debug("Processing existing cart for user: {}", cart.getUserId());
            productList = tempCart.getProductsDTOList();

            if(productList == null)
            {
                productList = new ArrayList<>();
            }
            log.debug("Current cart has {} products", productList.size());
            for(int i=0;i<productList.size();i++)
            {
                log.debug("Checking product: {} vs {}", productList.get(i).getProductId(), productsDTO.getProductId());

                if(productList.get(i).getProductId().equals(productsDTO.getProductId()))
                {
                    log.info("Same product found, incrementing quantity");
                    productsDTO.setProductQuantity(productList.get(i).getProductQuantity()+1);
                    productList.remove(i);
                    break;
                }
            }
            productsDTO.setPrice(productsDTO.getPrice() * productsDTO.getProductQuantity());
            log.debug("Updated product price: {}", productsDTO.getPrice());
        }
        else
            {
            log.info("Creating new cart for user: {}", cart.getUserId());
            productList = new ArrayList<>();
        }
        
        productList.add(productsDTO);
        cart.setProductsDTOList(productList);
        log.info("Saving cart with {} products for user: {}", productList.size(), cart.getUserId());
        return cartRepository.save(cart);
    }


    @Override
    public List<Cart> getAllCartDetails() {
        return cartRepository.findAll();
    }

    public void deleteCartById(String cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    public void deleteUserCart(String userId)
    {
        Optional<Cart> userCart  = cartRepository.findById(userId);
        if(userCart.isPresent())
        {
            Cart tempCart = userCart.get();
//            List l = tempCart.getProductsDTOList();
            tempCart.setProductsDTOList(new ArrayList<>());
            cartRepository.save(tempCart);
        }
        else{
            log.warn("Failed to delete cart for user: {}", userId);
        }
    }

    @Override
    public List<ProductsDTO> getProductsByUserId(String userId)
    {   //Cart userCart=cartRepository.findByUserId(userId).get(0);   //same
        Cart userCart = new Cart();
        if(cartRepository.findByUserId(userId).size()!=0) {
            userCart = cartRepository.findByUserId(userId).get(0);   // App Crash Due to no user found
        }
        log.debug("Retrieved cart for user: {}", userId);
        return userCart.getProductsDTOList();
    }

    @Override
    public boolean deleteCartItem(String userId, String productId) {
        //Cart userCart=cartRepository.findByUserId(userId).get(0);

        Cart userCart=cartRepository.findByUserId(userId).get(0);
        List<ProductsDTO> productList= userCart.getProductsDTOList();
        System.out.println("List");
        System.out.println(productList);
        System.out.println(userId);
        System.out.println(productId);

        for(int i=0;i<productList.size();i++)
        {
            if(productList.get(i).getProductId().equals(productId))
            {
                productList.remove(i);
                break;
            }

            else
                {
                    System.out.println("Dint Match");
                }
        }

        userCart.setProductsDTOList(productList);
        cartRepository.save(userCart);
        return true;
    }

    @Override
    public void increaseCartQuantity(String cartId, String productId,double price) {
        System.out.println(cartId);
        System.out.println(productId);
        System.out.println(price);
        Optional cart = cartRepository.findById(cartId);
        if(cart.isPresent()){
            List<ProductsDTO> list = cartRepository.findById(cartId).get().getProductsDTOList();
            for(int i=0;i<list.size();i++){
                if(list.get(i).getProductId().equals(productId)) {
                    System.out.println("increase here");
                    int quant = list.get(i).getProductQuantity();
                    System.out.println(quant);
                    list.get(i).setProductQuantity(quant+1);
                    System.out.println(list.get(i).getProductQuantity());

                    list.get(i).setPrice(price*(list.get(i).getProductQuantity()));
                    System.out.println(list.get(i).getPrice());
                }
            }
            Cart newCart = new Cart();
            newCart.setProductsDTOList(list);
            newCart.setCartId(cartId);
            newCart.setUserId(cartId);

            cartRepository.save(newCart);
        }else {
            System.out.println("Cart not Found");
        }
    }

    @Override
    public void decreaseCartQuantity(String cartId, String productId,double price) {
        System.out.println(cartId);
        System.out.println(productId);
        System.out.println(price);
        Optional cart = cartRepository.findById(cartId);
        if(cart.isPresent()){
            List<ProductsDTO> list = cartRepository.findById(cartId).get().getProductsDTOList();
            for(int i=0;i<list.size();i++){
                if(list.get(i).getProductId().equals(productId)) {
                    System.out.println("decrease here");
                    int quant = list.get(i).getProductQuantity();
                    System.out.println(quant);
                    
                    if (quant > 1) {
                        list.get(i).setProductQuantity(quant - 1);
                        list.get(i).setPrice(price * (list.get(i).getProductQuantity()));
                    } else {
                        // Remove item if quantity becomes 0
                        list.remove(i);
                        break;
                    }
                    System.out.println(list.get(i).getProductQuantity());
                    System.out.println(list.get(i).getPrice());
                }
            }
            Cart newCart = new Cart();
            newCart.setProductsDTOList(list);
            newCart.setCartId(cartId);
            newCart.setUserId(cartId);

            cartRepository.save(newCart);
        }else {
            System.out.println("Cart not Found");
        }
    }

}

