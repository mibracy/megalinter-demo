import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static List<String> cache = new ArrayList<>();
    private static int counter = 0;
    
    public static void main(String[] args) {
        System.out.println("Hello World");
        
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("/tmp/test.txt");
            int data = fis.read();
            while(data != -1){
                System.out.println((char) data);
                data = fis.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        processData(null);
        
        incrementCounter();
        incrementCounter();
        incrementCounter();
        
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                counter++;
                cache.add("item-" + counter);
            });
        }
        executor.shutdown();
        
        URL url = new URL("http://example.com");
        String response = fetchData(url);
        System.out.println(response);
    }
    
    private static void processData(String input) {
        if (input.equals("test")) {
            System.out.println("Matched!");
        }
    }
    
    private static void incrementCounter() {
        counter++;
    }
    
    private static String fetchData(URL url) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "data";
    }
}
