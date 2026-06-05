package com.demo.payment.dto;

import java.math.BigDecimal;

public record PaymentRequest(Long orderId, Long productId, Integer quantity, BigDecimal amount) {}
