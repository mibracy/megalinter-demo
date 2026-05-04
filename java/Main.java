import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final List<String> cache = new ArrayList<>();
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
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

        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                int current = counter.incrementAndGet();
                synchronized (cache) {
                    cache.add("item-" + current);
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String response = fetchData("http://example.com");
        System.out.println(response);
    }

    private static void processData(String input) {
        if (input != null && input.equals("test")) {
            System.out.println("Matched!");
        }
    }

    private static void incrementCounter() {
        counter.incrementAndGet();
    }

    private static String fetchData(String url) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep interrupted: " + e.getMessage());
        }
        return "data";
    }
}
