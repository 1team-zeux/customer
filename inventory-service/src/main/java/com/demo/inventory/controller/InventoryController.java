package com.demo.inventory.controller;

import com.demo.inventory.domain.Inventory;
import com.demo.inventory.dto.ReserveRequest;
import com.demo.inventory.dto.ReserveResponse;
import com.demo.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/inventory/reserve")
    public ResponseEntity<ReserveResponse> reserve(@RequestBody ReserveRequest req) {
        return ResponseEntity.ok(inventoryService.reserve(req));
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<Inventory>> list() {
        return ResponseEntity.ok(inventoryService.listAll());
    }
}
