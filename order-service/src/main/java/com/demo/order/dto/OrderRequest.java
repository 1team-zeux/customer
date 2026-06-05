package com.demo.order.dto;

public record OrderRequest(String userId, Long productId, Integer quantity) {}
