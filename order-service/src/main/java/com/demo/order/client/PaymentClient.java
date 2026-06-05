package com.demo.order.client;

import com.demo.order.dto.PaymentRequest;
import com.demo.order.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentClient {
    @PostMapping("/payments")
    PaymentResponse createPayment(@RequestBody PaymentRequest req);
}
