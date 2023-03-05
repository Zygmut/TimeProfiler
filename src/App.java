import java.time.Duration;

import TimeProfiler.TimeProfiler;

public class App {
    public static void main(String[] args) throws Exception {

        System.out.println(TimeProfiler.timeIt(() -> {System.out.println("Hello, World!");}).toString(Duration::toNanos));

    }
}
