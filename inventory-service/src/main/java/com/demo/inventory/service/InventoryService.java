package com.demo.inventory.service;

import com.demo.inventory.config.ChaosState;
import com.demo.inventory.domain.Inventory;
import com.demo.inventory.dto.ReserveRequest;
import com.demo.inventory.dto.ReserveResponse;
import com.demo.inventory.repository.InventoryRepository;
import com.demo.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final ChaosState chaosState;

    @Transactional
    public ReserveResponse reserve(ReserveRequest req) {
        if (chaosState.isErrorEnabled()) {
            log.error("CHAOS: Simulating DB connection failure for product {}", req.productId());
            throw new RuntimeException("Simulated DB connection failure");
        }
        if (chaosState.isDbDelayEnabled()) {
            log.warn("CHAOS: Injecting 5s DB latency for product {}", req.productId());
            try { Thread.sleep(5000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        Inventory inv = inventoryRepository.findByProductId(req.productId())
            .orElseThrow(() -> new IllegalArgumentException("Product not in inventory: " + req.productId()));

        if (inv.getQuantity() < req.quantity()) {
            throw new IllegalStateException("Insufficient inventory: available=" + inv.getQuantity());
        }

        inv.setQuantity(inv.getQuantity() - req.quantity());
        inv.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inv);

        log.info("Reserved {} units of product {}. Remaining: {}", req.quantity(), req.productId(), inv.getQuantity());
        return new ReserveResponse(true, inv.getQuantity());
    }

    public List<Inventory> listAll() {
        return inventoryRepository.findAll();
    }
}
