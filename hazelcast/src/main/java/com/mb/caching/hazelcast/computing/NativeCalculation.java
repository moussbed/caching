package com.mb.caching.hazelcast.computing;

import java.util.stream.IntStream;

public class NativeCalculation {

    public static void main(String[] args) {
        IntStream input = IntStream.range(0, 20000);
        final long start = System.currentTimeMillis();
        final int sum = input.sum();
        final long end = System.currentTimeMillis();
        System.out.println("Total sum = " + sum);
        System.out.println("Calculation time = " + (end-start));
    }
}
