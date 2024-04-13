package com.onlineShopping.order_service.service;

import com.onlineShopping.order_service.dto.OrderRequest;
import com.onlineShopping.order_service.dto.OrderResponse;
import com.onlineShopping.order_service.modal.Order;

import java.util.Optional;

public interface OrderServiceInterface {
    public Order placeOrder(OrderRequest orderRequest);

    public String cancelOrder(Long orderId);

    public OrderResponse getOrderDetails(Long orderId);
}
