package com.onlineShopping.order_service.dto;

import com.onlineShopping.order_service.modal.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String buyerId;
    private List<Product> orderList;
}
