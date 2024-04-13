package com.onlineShopping.order_service.dto;

import com.onlineShopping.order_service.modal.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private String buyerId;
    private BigDecimal totalAmount;
    private List<Product> orderList;
}
