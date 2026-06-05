package com.demo.payment.client;

import com.demo.payment.dto.ReserveRequest;
import com.demo.payment.dto.ReserveResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @PostMapping("/inventory/reserve")
    ReserveResponse reserve(@RequestBody ReserveRequest req);
}
