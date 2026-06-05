package com.demo.order.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter @NoArgsConstructor
public class Product {
    @Id
    private Long id;

    private String name;
    private BigDecimal price;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
