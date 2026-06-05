package com.demo.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "inventory-chaos")
public interface InventoryChaosClient {
    @PostMapping("/chaos/db-delay")
    String activateDbDelay();

    @PostMapping("/chaos/db-delay/reset")
    String resetDbDelay();

    @PostMapping("/chaos/error")
    String activateError();

    @PostMapping("/chaos/error/reset")
    String resetError();
}
