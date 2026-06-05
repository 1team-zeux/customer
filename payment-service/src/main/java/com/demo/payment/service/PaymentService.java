package com.demo.payment.service;

import com.demo.payment.client.InventoryClient;
import com.demo.payment.domain.Payment;
import com.demo.payment.dto.PaymentRequest;
import com.demo.payment.dto.PaymentResponse;
import com.demo.payment.dto.ReserveRequest;
import com.demo.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InventoryClient inventoryClient;

    public PaymentResponse createPayment(PaymentRequest req) {
        Payment payment = new Payment();
        payment.setOrderId(req.orderId());
        payment.setAmount(req.amount());
        payment.setStatus("PROCESSING");
        payment.setCreatedAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);

        try {
            inventoryClient.reserve(new ReserveRequest(req.productId(), req.quantity()));
            payment.setStatus("COMPLETED");
            log.info("Payment completed for order {}", req.orderId());
        } catch (Exception e) {
            payment.setStatus("FAILED");
            log.error("Inventory reservation failed for order {}: {}", req.orderId(), e.getMessage());
            paymentRepository.save(payment);
            throw new RuntimeException("Payment failed: " + e.getMessage());
        }

        payment = paymentRepository.save(payment);
        return new PaymentResponse(payment.getId(), payment.getOrderId(), payment.getStatus());
    }
}
