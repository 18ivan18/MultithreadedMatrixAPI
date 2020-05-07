package com.ivan.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AtmoicBigInteger {
    private final AtomicReference<BigInteger> valueHolder;

    private AtmoicBigInteger() {
        valueHolder = new AtomicReference<>();
    }

    private static class BillPughSingleton {
        private static final AtmoicBigInteger INSTANCE = new AtmoicBigInteger();
    }

    public static AtmoicBigInteger getInstance() {
        return AtmoicBigInteger.BillPughSingleton.INSTANCE;
    }

    public void setValue(BigInteger newValue) {
        valueHolder.set(newValue);
    }

    public BigInteger getValue() {
        return valueHolder.get();
    }

    public BigInteger addAndGet(BigInteger value) {
        while (true) {
            BigInteger current = valueHolder.get();
            BigInteger next = current.add(value);
            if (valueHolder.compareAndSet(current, next)) {
                return next;
            }
        }
    }
}
