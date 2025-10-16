/**
 * 
 */
package me.duanbn.snailfish.util.lang;

import java.util.Random;

/**
 * @author bingnan.dbn
 *
 */
public class RandomUtil {

    private static final Random RANDOM = new Random();

    public static long nextLong(final long startInclusive, final long endExclusive) {
        isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        isTrue(startInclusive >= 0, "Both range values must be non-negative.");

        if (startInclusive == endExclusive) {
            return startInclusive;
        }

        return (long) nextDouble(startInclusive, endExclusive);
    }

    public static double nextDouble(final double startInclusive, final double endInclusive) {
        isTrue(endInclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        isTrue(startInclusive >= 0, "Both range values must be non-negative.");

        if (startInclusive == endInclusive) {
            return startInclusive;
        }

        return startInclusive + ((endInclusive - startInclusive) * RANDOM.nextDouble());
    }

    private static void isTrue(final boolean expression, final String message, final Object... values) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

}
