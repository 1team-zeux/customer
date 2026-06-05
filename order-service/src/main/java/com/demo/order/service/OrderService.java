package com.demo.order.service;

import com.demo.order.client.PaymentClient;
import com.demo.order.client.RecommendationClient;
import com.demo.order.domain.Order;
import com.demo.order.domain.Product;
import com.demo.order.dto.OrderRequest;
import com.demo.order.dto.OrderResponse;
import com.demo.order.dto.PaymentRequest;
import com.demo.order.dto.PaymentResponse;
import com.demo.order.repository.OrderRepository;
import com.demo.order.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentClient paymentClient;
    private final RecommendationClient recommendationClient;

    public OrderResponse createOrder(OrderRequest req) {
        Product product = productRepository.findById(req.productId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + req.productId()));

        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(req.quantity()));

        Order order = new Order();
        order.setUserId(req.userId());
        order.setProductId(req.productId());
        order.setQuantity(req.quantity());
        order.setStatus("PENDING");
        order.setTotalPrice(totalPrice);
        order.setCreatedAt(LocalDateTime.now());
        order = orderRepository.save(order);

        try {
            PaymentRequest paymentRequest = new PaymentRequest(
                order.getId(), req.productId(), req.quantity(), totalPrice);
            PaymentResponse payment = paymentClient.createPayment(paymentRequest);
            order.setStatus("COMPLETED".equals(payment.status()) ? "CONFIRMED" : "FAILED");
            log.info("Order {} status: {}", order.getId(), order.getStatus());
        } catch (Exception e) {
            order.setStatus("FAILED");
            log.error("Order {} failed: {}", order.getId(), e.getMessage());
            orderRepository.save(order);
            throw new RuntimeException("Order " + order.getId() + " failed: " + e.getMessage());
        }
        order = orderRepository.save(order);

        Object recommendations = recommendationClient.getRecommendations(req.userId());

        return toResponse(order, recommendations);
    }

    public List<OrderResponse> listOrders() {
        return orderRepository.findAll().stream()
            .map(o -> toResponse(o, List.of()))
            .toList();
    }

    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        return toResponse(order, List.of());
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id))
            throw new IllegalArgumentException("Order not found: " + id);
        orderRepository.deleteById(id);
    }

    private OrderResponse toResponse(Order order, Object recommendations) {
        return new OrderResponse(
            order.getId(), order.getUserId(), order.getProductId(),
            order.getQuantity(), order.getStatus(), order.getTotalPrice(),
            recommendations, order.getCreatedAt()
        );
    }
}
