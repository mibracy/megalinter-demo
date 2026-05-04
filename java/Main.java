package com.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

/**
 * Main class for demonstrating MegaLinter fixes.
 */
public final class Main {
    private static final int MAX_THREADS = 100;
    private static final int SHORT_SLEEP_MS = 1000;
    private static final int AWAIT_TIMEOUT = 5;
    private static final List<String> CACHE = new ArrayList<>();
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    /**
     * Main method.
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        System.out.println("Hello World");

        try (FileInputStream fis = new FileInputStream("/tmp/test.txt")) {
            int data = fis.read();
            while (data != -1) {
                System.out.println((char) data);
                data = fis.read();
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        processData("test");

        incrementCounter();
        incrementCounter();
        incrementCounter();

        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
        for (int i = 0; i < MAX_THREADS; i++) {
            executor.submit(() -> {
                int current = COUNTER.incrementAndGet();
                synchronized (CACHE) {
                    CACHE.add("item-" + current);
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(AWAIT_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String response = fetchData("https://example.com");
        System.out.println(response);
    }

    /**
     * Process data if input is valid.
     * @param input the input string
     */
    private static void processData(final String input) {
        if (input != null && input.equals("test")) {
            System.out.println("Matched!");
        }
    }

    private static void incrementCounter() {
        COUNTER.incrementAndGet();
    }

    /**
     * Fetch data from URL.
     * @param url the URL to fetch
     * @return the response as string
     */
    private static String fetchData(final String url) {
        try {
            Thread.sleep(SHORT_SLEEP_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep interrupted: " + e.getMessage());
        }
        return "data";
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private Main() {
        // Utility class, prevent instantiation
    }
}
