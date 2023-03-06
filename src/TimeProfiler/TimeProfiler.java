package TimeProfiler;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class TimeProfiler {

	/**
	 * Given a runnable function, retuns the time that was spent executing that
	 * function
	 *
	 * @param function to time
	 * @return Time of execution
	 * @see Duration
	 */
	private static Duration timeFunction(Runnable function) {
		Instant startTime = Instant.now();
		function.run();
		Instant endTime = Instant.now();
		return Duration.between(startTime, endTime);
	}

	/**
	 * Given a runnable function returns a TimeResult object with the time that was
	 * spent running that function.
	 *
	 * @param function to time
	 * @return TimeResult with the duration of the execution
	 * @see TimeResult
	 */
	public static TimeResult timeIt(Runnable function) {
		return new TimeResult(new Duration[] { timeFunction(function) });
	}

	/**
	 * Given a runnable funtion returns a TimeResult object with all the values of
	 * time spent running that function. The amount of times that the function is
	 * executed is defined by batchSize. If batchSize is <= 0, returns a Duration of
	 * 0.
	 *
	 * @param function to run
	 * @param batchSize Amount of times that the function will be executed
	 * @throws IllegalArgumentException if batchSize <= 0
	 * @return TimeResult with the durations of each execution
	 * @see TimeResult
	 */
	public static TimeResult batchTimeIt(Runnable function, int batchSize) {
		if (batchSize <= 0) {
			throw new IllegalArgumentException("batchSize must be > 0");
		}

		return new TimeResult(LongStream.range(0, batchSize)
				.parallel()
				.mapToObj(i -> timeFunction(function))
				.toArray(Duration[]::new));
	}

	/**
	 * Given a runnable array of functions returns a TimeResult array object with
	 * the time that was spent running each one of those functions.
	 *
	 * @param functions to run
	 * @return TimeResult with the durations of each execution
	 * @see TimeResult
	 */
	public static TimeResult timeIt(Runnable[] functions) {
		return new TimeResult(Arrays.stream(functions)
				.parallel()
				.map(TimeProfiler::timeFunction)
				.toArray(Duration[]::new));
	}

	/**
	 * Given a runnable funtion array returns a TimeResult object array with the
	 * mean value of time spent running each function. The amount of times that a
	 * function is executed is defined by batchSize. If batchSize is <= 0, returns a
	 * Duration of 0.
	 *
	 * @param functions to run
	 * @param batchSize Amount of times that each function will be executed
	 * @throws IllegalArgumentException if batchSize <= 0
	 * @return TimeResult[] with all the durations of each execution per function
	 * @see TimeResult
	 */
	public static TimeResult[] batchTimeIt(Runnable[] functions, int batchSize) {
		if (batchSize <= 0) {
			throw new IllegalArgumentException("batchSize must be > 0");
		}

		return Arrays.stream(functions)
				.parallel()
				.map(function -> {
					return new TimeResult(LongStream.range(0, batchSize)
							.mapToObj(i -> timeFunction(function))
							.toArray(Duration[]::new));
				})
				.toArray(TimeResult[]::new);
	}
}
