package com.demo.inventory.controller;

import com.demo.inventory.config.ChaosState;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chaos")
@RequiredArgsConstructor
public class ChaosController {

    private final ChaosState chaosState;

    @PostMapping("/db-delay")
    public ResponseEntity<String> activateDbDelay() {
        chaosState.setDbDelayEnabled(true);
        return ResponseEntity.ok("DB delay enabled (5s sleep on every reserve call)");
    }

    @PostMapping("/db-delay/reset")
    public ResponseEntity<String> resetDbDelay() {
        chaosState.setDbDelayEnabled(false);
        return ResponseEntity.ok("DB delay disabled");
    }

    @PostMapping("/error")
    public ResponseEntity<String> activateError() {
        chaosState.setErrorEnabled(true);
        return ResponseEntity.ok("DB error enabled (throws RuntimeException on every reserve call)");
    }

    @PostMapping("/error/reset")
    public ResponseEntity<String> resetError() {
        chaosState.setErrorEnabled(false);
        return ResponseEntity.ok("DB error disabled");
    }

    @PostMapping("/status")
    public ResponseEntity<Object> status() {
        return ResponseEntity.ok(new Object() {
            public final boolean dbDelayEnabled = chaosState.isDbDelayEnabled();
            public final boolean errorEnabled   = chaosState.isErrorEnabled();
        });
    }
}
