package com.demo.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
    Long id,
    String userId,
    Long productId,
    Integer quantity,
    String status,
    BigDecimal totalPrice,
    Object recommendations,
    LocalDateTime createdAt
) {}
