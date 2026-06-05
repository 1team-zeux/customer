package com.demo.payment.controller;

import com.demo.payment.dto.PaymentRequest;
import com.demo.payment.dto.PaymentResponse;
import com.demo.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest req) {
        return ResponseEntity.ok(paymentService.createPayment(req));
    }
}
