package com.onlineShopping.order_service.controller;

import com.onlineShopping.order_service.dto.OrderRequest;
import com.onlineShopping.order_service.dto.OrderResponse;
import com.onlineShopping.order_service.modal.Order;
import com.onlineShopping.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order placeOrder(@RequestBody OrderRequest orderRequest){
        try {
            Order order = orderService.placeOrder(orderRequest);
            return order;
        } catch (RuntimeException e) {
            if(!Objects.equals(e.getMessage(), "Inventory updated successfully")){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to place order", e);
        }
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderDetails(@PathVariable String orderId){
        return orderService.getOrderDetails(Long.parseLong(orderId));
    }
}
