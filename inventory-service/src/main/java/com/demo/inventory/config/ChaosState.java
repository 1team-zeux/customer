package com.demo.inventory.config;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ChaosState {
    private final AtomicBoolean dbDelayEnabled = new AtomicBoolean(false);
    private final AtomicBoolean errorEnabled   = new AtomicBoolean(false);

    public boolean isDbDelayEnabled() { return dbDelayEnabled.get(); }
    public void setDbDelayEnabled(boolean v) { dbDelayEnabled.set(v); }
    public boolean isErrorEnabled() { return errorEnabled.get(); }
    public void setErrorEnabled(boolean v) { errorEnabled.set(v); }
}
