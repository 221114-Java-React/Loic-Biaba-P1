package com.revature.resproject.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class Sequence {
    private static final AtomicInteger counter = new AtomicInteger(1);

    public static int nextValue() {
        return counter.getAndIncrement();
    }
}
