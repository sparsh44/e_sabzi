package com.onlineShopping.order_service.dto;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String id;
    private String farmerId;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal quantity;
}
