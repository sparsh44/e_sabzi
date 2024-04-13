package com.onlineShopping.product_service.modal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(value = "product")
public class Product {
    @Id
    private String id;
    private String farmerId;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal quantity;
}
