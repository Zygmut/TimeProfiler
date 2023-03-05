package TimeProfiler;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

public class TimeResult {
    private final Duration[] data;

    public TimeResult(Duration[] data) {
        if (data.length == 0) {
            throw new IllegalArgumentException("Data array cannot be empty.");
        }
        this.data = data;
    }

    /**
     * Returns the array of durations contained in this object
     *
     * @return an array of durations
     */
    public Duration[] getData() {
        return data;
    }

    /**
     * Calculates the sum of the durations as a long value computed by the given
     * timestep
     * function
     * i.e:
     *
     * <pre>
     * {@code
     * TimeProfiler.batchTimeIt(this::fn, 10).sum(Duration::toMillis);
     * }
     * </pre>
     *
     * Would return the sum of all the values in milliseconds
     *
     * @param timeStep a function that extracts a long value from a Duration object
     * @return The sum of the data as a long value
     * @throws NullPointerException if the timeStep function is null
     * @see ToLongFunction
     * @see Duration
     */
    public long sum(ToLongFunction<? super Duration> timeStep) {
        return Arrays.stream(this.data)
                .mapToLong(timeStep)
                .sum();
    }

    /**
     * Calculates the mean value of the durations as a double value computed by the
     * given
     * timestep
     * function.
     * i.e:
     *
     * <pre>
     * {@code
     * TimeProfiler.batchTimeIt(this::fn, 10).mean(Duration::toNanos);
     * }
     * </pre>
     *
     * Would return the mean value in nanoseconds
     *
     * @param timeStep a function that extracts a long value from a Duration object
     * @return The mean of the data as a double value
     * @throws NullPointerException if the timeStep function is null
     * @see ToLongFunction
     * @see Duration
     */
    public double mean(ToLongFunction<? super Duration> timeStep) {
        return Arrays.stream(this.data)
                .mapToLong(timeStep)
                .average()
                .getAsDouble();
    }

    /**
     * Calculates the minimum value among the durations values represented by the
     * array,
     * as computed by the given timeStep function.
     * i.e:
     *
     * <pre>
     * {@code
     * TimeProfiler.batchTimeIt(this::fn, 10).min(Duration::toSeconds);
     * }
     * </pre>
     *
     * Would return the minimum duration of them all in seconds
     *
     * @param timeStep a function that extracts a long value from a Duration object
     * @return The minimum value among the durations in long
     * @throws NullPointerException if the timeStep function is null
     * @see ToLongFunction
     * @see Duration
     */
    public long min(ToLongFunction<? super Duration> timeStep) {
        return Arrays.stream(this.data)
                .mapToLong(timeStep)
                .min()
                .getAsLong();
    }

    /**
     * Calculates the maximum value among the durations values represented by the
     * array,
     * as computed by the given timeStep function.
     * i.e:
     *
     * <pre>
     * {@code
     * TimeProfiler.batchTimeIt(this::fn, 10).max(Duration::toMinutes);
     * }
     * </pre>
     *
     * Would return the maximum duration of them all in minutes
     *
     * @param timeStep a function that extracts a long value from a Duration object
     * @return The maximum value among the durations in long
     * @throws NullPointerException if the timeStep function is null
     * @see ToLongFunction
     * @see Duration
     */
    public long max(ToLongFunction<? super Duration> timeStep) {
        return Arrays.stream(this.data)
                .mapToLong(timeStep)
                .max()
                .getAsLong();
    }

    /**
     * Returns a copy of the data within this object sorted in ascending order
     *
     * @return and array of durations sorted in ascending order
     * @see Duration
     */
    public Duration[] sort() {
        return Arrays.stream(this.data)
                .sorted()
                .toArray(Duration[]::new);
    }

    /**
     * Calculates the mode of the data array as computed by the
     * given timeStep function. It's time complexity is O(n) i.e:
     *
     * <pre>
     * {@code
     * TimeProfiler.batchTimeIt(this::fn, 10).mode(Duration::toMinutes);
     * }
     * </pre>
     *
     * Would return the maximum duration of them all in minutes
     *
     * @param timeStep a function that extracts a long value from a Duration object
     * @return the mode value as a long
     * @throws NullPointerException if the timeStep function is null
     * @see ToLongFunction
     * @see Duration
     */
    public long mode(ToLongFunction<? super Duration> timeStep) {
        return Arrays.stream(data)
                .mapToLong(timeStep)
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Calculates the median of the data array as computed by the given timeStep
     * function. i.e:
     *
     * <pre>
     * {@code
     * TimeProfiler.batchTimeIt(this::fn, 10).median(Duration::toHours);
     * }
     * </pre>
     *
     * Would return the median duration of them all in hours
     *
     * @param timeStep a function that extracts a long value from a Duration object
     * @return the median value as a long
     * @throws NullPointerException if the timeStep function is null
     * @see ToLongFunction
     * @see Duration
     */
    public long median(ToLongFunction<? super Duration> timeStep) {
        return timeStep.applyAsLong(Arrays.stream(this.data)
                .sorted()
                .skip((this.data.length - 1) / 2)
                .limit(2 - this.data.length % 2)
                .reduce(Duration.ZERO, Duration::plus)
                .dividedBy(2));

    }

    /**
     * Returns a String representation of the data array as computed by the given timeStep function. i.e:
     * <pre>
     * {@code
     * TimeProfiler.batchTimeIt(this::fn, 10).toString(Duration::toDays);
     * }
     * </pre>
     *
     * Would return a String with all the data parsed to days
     *
     * @param timeStep a function that extracts a long value from a Duration object
     * @return a String representation of the data array within the object
     * @throws NullPointerException if the timeStep function is null
     * @see ToLongFunction
     * @see Duration
     */
    public String toString(ToLongFunction<? super Duration> timeStep) {
        return Arrays.stream(this.data)
                .mapToLong(timeStep::applyAsLong)
                .mapToObj(Long::toString)
                .collect(Collectors.joining(", "));
    }

    /**
     * Returns the amount of values of the current data within the object
     *
     * @return the amount of values of the current data within the object
     */
    public int length() {
        return data.length;
    }
}
