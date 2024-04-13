package com.onlineShopping.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequest {

    private String id;
    private String farmerId;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal quantity;
}
