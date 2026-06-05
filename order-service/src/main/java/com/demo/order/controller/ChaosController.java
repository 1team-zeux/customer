package com.demo.order.controller;

import com.demo.order.client.InventoryChaosClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chaos")
@RequiredArgsConstructor
@Slf4j
public class ChaosController {

    private final InventoryChaosClient inventoryChaosClient;

    @PostMapping("/db-delay")
    public ResponseEntity<String> triggerDbDelay() {
        return ResponseEntity.ok(inventoryChaosClient.activateDbDelay());
    }

    @PostMapping("/db-delay/reset")
    public ResponseEntity<String> resetDbDelay() {
        return ResponseEntity.ok(inventoryChaosClient.resetDbDelay());
    }

    @PostMapping("/error")
    public ResponseEntity<String> triggerError() {
        return ResponseEntity.ok(inventoryChaosClient.activateError());
    }

    @PostMapping("/error/reset")
    public ResponseEntity<String> resetError() {
        return ResponseEntity.ok(inventoryChaosClient.resetError());
    }

    @PostMapping("/cpu")
    public ResponseEntity<String> triggerCpu() {
        Thread.ofVirtual().start(() -> {
            log.warn("CHAOS: CPU spike started (30s)");
            long end = System.currentTimeMillis() + 30_000L;
            while (System.currentTimeMillis() < end) { /* spin */ }
            log.info("CHAOS: CPU spike ended");
        });
        return ResponseEntity.ok("CPU spike started for 30s");
    }
}
