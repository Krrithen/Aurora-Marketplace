package com.example.Cart.controller;

import com.example.Cart.dto.ProductsDTO;
import com.example.Cart.entities.Cart;
import com.example.Cart.feignClient.FeignCartService;
import com.example.Cart.services.CartService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/cart")
@Slf4j
@Validated
@Tag(name = "Cart", description = "Shopping cart management operations")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    FeignCartService feignCartService;

    @PostMapping("/AddCartDetails")
    @Operation(summary = "Add item to cart", description = "Adds a product to the user's shopping cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item added to cart successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Cart> addCartDetails(@Valid @RequestBody Cart cart) {
        try {
            log.info("Adding item to cart for user: {}, product: {}", cart.getUserId(), cart.getProductId());
            ResponseEntity<Object> response = feignCartService.getProductsByProductId(cart.getProductId());
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ProductsDTO productsDTO = mapper.convertValue(response.getBody(), ProductsDTO.class);
            Cart savedCart = cartService.addCartDetails(cart, productsDTO);
            log.info("Item added to cart successfully");
            return new ResponseEntity<>(savedCart, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error adding item to cart: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/DeleteByCartId/{cartId}")
    public ResponseEntity<String> deleteByCartId(@PathVariable("cartId") String cartId) {
        List cart = cartService.getProductsByUserId(cartId);//.get(i).getProductId();
        for (int i = 0; i < cart.size(); i++) {
            String pid = cartService.getProductsByUserId(cartId).get(i).getProductId();
            int quantity = cartService.getProductsByUserId(cartId).get(i).getProductQuantity();
            System.out.println(quantity);
            for (int j = 0; j < quantity; j++) {
                feignCartService.reduceQuantity(pid);
            }

        }

        cartService.deleteCartById(cartId);
        return new ResponseEntity<>("Cart Id Deleted", HttpStatus.OK);
    }

    @DeleteMapping("/DeleteCartItems/{userId}")
    public ResponseEntity<String> deleteCartItems(@PathVariable("userId") String userId) {
        cartService.deleteUserCart(userId);
        return new ResponseEntity<>("Cart is Empty", HttpStatus.OK);
    }

    @GetMapping("/GetAllDetails/{id}")
    public ResponseEntity<List<Cart>> getAllDetails() {
        List<Cart> cartList = cartService.getAllCartDetails();
        return new ResponseEntity<List<Cart>>(cartList, HttpStatus.OK);
    }

    @GetMapping("/GetAllCartProducts/{userId}")
    public ResponseEntity<List<ProductsDTO>> getAllCartProducts(@PathVariable("userId") String userId) {
        List<ProductsDTO> cartProducts = cartService.getProductsByUserId(userId);
        System.out.println(cartProducts);
        return new ResponseEntity<>(cartProducts, HttpStatus.OK);
    }


    @DeleteMapping("/DeleteByCartId/{userId}/{productId}")
    public ResponseEntity<String> deleteByUserProduct(@PathVariable("userId") String userId, @PathVariable("productId") String productId) {
        cartService.deleteCartItem(userId, productId);
        return new ResponseEntity<>("Cart Id Deleted", HttpStatus.OK);
    }

    @PostMapping("/increaseButton/{cartId}/{productId}")
    public ResponseEntity<String> increaseCartQuantity(@PathVariable("cartId") String cartId, @PathVariable("productId") String productId) {
        double price = feignCartService.getPriceByProductId(productId).getBody();
        cartService.increaseCartQuantity(cartId,productId,price);
        return new ResponseEntity<>("Cart Item reduced", HttpStatus.OK);
    }

    @PostMapping("/decreaseButton/{cartId}/{productId}")
    public ResponseEntity<String> decreaseCartQuantity(@PathVariable("cartId") String cartId, @PathVariable("productId") String productId) {
        double price = feignCartService.getPriceByProductId(productId).getBody();
        cartService.decreaseCartQuantity(cartId,productId,price);
        return new ResponseEntity<>("Cart Item reduced", HttpStatus.OK);
    }

}