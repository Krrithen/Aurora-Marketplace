package com.example.Orders.controller;

import com.example.Orders.dto.OrderProductsDTO;
import com.example.Orders.dto.OrdersDTO;
import com.example.Orders.dto.ProductsDTO;
import com.example.Orders.entities.Orders;
import com.example.Orders.feignServices.FeignMailService;
import com.example.Orders.feignServices.FeignOrderService;
import com.example.Orders.feignServices.FeignProductService;
import com.example.Orders.services.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

@RestController
@RequestMapping("/orders")
@Slf4j
@Validated
@Tag(name = "Orders", description = "Order management operations")
public class OrdersController {

    @Autowired
    OrdersService ordersService;

    @Autowired
    FeignOrderService feignOrderService;

    @Autowired
    FeignMailService feignMailService;

    @Autowired
    FeignProductService feignProductService;

    Gson gson = new Gson();

    @PostMapping("/AddOrderDetails")
    @Operation(summary = "Add order details", description = "Creates a new order and sends confirmation email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order placed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> addOrdersDetails(@Valid @RequestBody Orders ordersDTO){
        try {
            log.info("Processing order for user: {}", ordersDTO.getUserId());
            
            // Create order
            ordersService.addOrdersDetails(ordersDTO);
            
            // Clear cart after successful order
            feignOrderService.deleteByCartId(ordersDTO.getUserId());
            
            // Get user email and name
            String to = feignMailService.getUserMailByUserId(ordersDTO.getUserId()).getBody();
            String name = feignMailService.getUserNameByUserId(ordersDTO.getUserId()).getBody();
            
            // Build email body
            String body = "Hey! " + name + "\n This is an email for your order. \n";
            String subject = "Order Summary";
            
            Orders orderDetails = ordersService.getOrdersDetails(ordersDTO.getUserId());
            if (orderDetails != null && orderDetails.getProductsDTOList() != null) {
                for (OrderProductsDTO product : orderDetails.getProductsDTOList()) {
                    body += product.getProductName() + "\n";
                    body += "Price: $" + product.getPrice() + "\n";
                }
                
                body += "Total Price: $" + orderDetails.getTotalPrice() + 
                       " purchased on " + orderDetails.getDateOfOrder() + 
                       " shipping to " + orderDetails.getShippingAddress() + ".";
            }
            
            log.info("Sending order confirmation email to: {}", to);
            ordersService.sendEmail(to, subject, body);
            
            log.info("Order placed successfully for user: {}", ordersDTO.getUserId());
            return new ResponseEntity<>("Order Placed Successfully!!!", HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Error processing order for user {}: {}", ordersDTO.getUserId(), e.getMessage(), e);
            return new ResponseEntity<>("Error processing order", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/GetAllDetails")
    @Operation(summary = "Get all order details", description = "Retrieves all orders in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Orders>> getAllOrderDetails(){
        try {
            log.info("Retrieving all order details");
            List<Orders> ordersList = ordersService.getAllOrdersDetails();
            log.info("Retrieved {} orders", ordersList.size());
            return new ResponseEntity<>(ordersList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving all orders: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/GetOrderDetails/{userId}")
    @Operation(summary = "Get order details by user ID", description = "Retrieves order details for a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order details retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "400", description = "Invalid user ID")
    })
    public ResponseEntity<Orders> GetOrderDetails(
            @Parameter(description = "User ID to retrieve order details", required = true)
            @PathVariable("userId") @NotBlank String userId) {
        try {
            log.info("Retrieving order details for user: {}", userId);
            Orders orderList = ordersService.getOrdersDetails(userId);
            if (orderList != null) {
                log.info("Order details found for user: {}", userId);
                return new ResponseEntity<>(orderList, HttpStatus.OK);
            } else {
                log.warn("No order found for user: {}", userId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error retrieving order details for user {}: {}", userId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/AddOrderProducts/{userId}")
    @Operation(summary = "Add order products", description = "Converts cart products to order products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order products added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid user ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> addOrderProducts(
            @Parameter(description = "User ID to add order products", required = true)
            @PathVariable("userId") @NotBlank String userId){
        try {
            log.info("Adding order products for user: {}", userId);
            
            List<ProductsDTO> cartList = feignOrderService.getAllCartProducts(userId).getBody();
            if (cartList == null || cartList.isEmpty()) {
                log.warn("No cart products found for user: {}", userId);
                return new ResponseEntity<>("No products in cart", HttpStatus.BAD_REQUEST);
            }
            
            List<OrderProductsDTO> productsList = new ArrayList<>();
            for (ProductsDTO cartProduct : cartList) {
                OrderProductsDTO orderProduct = new OrderProductsDTO();
                BeanUtils.copyProperties(cartProduct, orderProduct);
                productsList.add(orderProduct);
            }
            
            log.info("Converting {} cart products to order products for user: {}", productsList.size(), userId);
            ordersService.addOrderProducts(productsList, userId);
            
            return new ResponseEntity<>("Processing Your Order", HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Error adding order products for user {}: {}", userId, e.getMessage(), e);
            return new ResponseEntity<>("Error processing order products", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/GetAllProductInfo/{userId}")
    @Operation(summary = "Get all product info for user", description = "Retrieves all product information for a user's cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product info retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid user ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<OrderProductsDTO>> getAllProductInfo(
            @Parameter(description = "User ID to retrieve product info", required = true)
            @PathVariable("userId") @NotBlank String userId){
        try {
            log.info("Retrieving product info for user: {}", userId);
            
            List<ProductsDTO> cartList = feignOrderService.getAllCartProducts(userId).getBody();
            if (cartList == null || cartList.isEmpty()) {
                log.warn("No cart products found for user: {}", userId);
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
            }
            
            List<OrderProductsDTO> orderProductsDTOList = new ArrayList<>();
            for (ProductsDTO cartProduct : cartList) {
                OrderProductsDTO orderProduct = new OrderProductsDTO();
                BeanUtils.copyProperties(cartProduct, orderProduct);
                orderProductsDTOList.add(orderProduct);
            }
            
            log.info("Retrieved {} products for user: {}", orderProductsDTOList.size(), userId);
            return new ResponseEntity<>(orderProductsDTOList, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("Error retrieving product info for user {}: {}", userId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/GetOrderHistory/{userId}")
    @Operation(summary = "Get order history", description = "Retrieves order history for a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order history retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid user ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Orders>> getOrderHistory(
            @Parameter(description = "User ID to retrieve order history", required = true)
            @PathVariable("userId") @NotBlank String userId){
        try {
            log.info("Retrieving order history for user: {}", userId);
            List<Orders> orderHistory = ordersService.getOrderHistory(userId);
            log.info("Retrieved {} orders from history for user: {}", orderHistory.size(), userId);
            return new ResponseEntity<>(orderHistory, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving order history for user {}: {}", userId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}



