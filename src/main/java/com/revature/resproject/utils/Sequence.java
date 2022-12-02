package com.revature.resproject.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class Sequence {
    private static final AtomicInteger counter = new AtomicInteger(1);
    private static final AtomicInteger t_counter = new AtomicInteger(1);

    public static int nextValue() {
        return counter.getAndIncrement();
    }
    public static int nextIdValue() {
        return t_counter.getAndIncrement();
    }
}
