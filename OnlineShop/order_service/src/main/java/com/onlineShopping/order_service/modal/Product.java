package com.onlineShopping.order_service.modal;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ordered_products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_number")
    private Long orderNumber;

    private String id;
    private String farmerId;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal quantity;
}
