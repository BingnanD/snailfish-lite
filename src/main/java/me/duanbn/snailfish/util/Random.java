package me.duanbn.snailfish.util;

/**
 * 
 * @author bingnan.dbn
 *
 */
public class Random {

	private static SnowflakeId snowflakeId = new SnowflakeId();

	public static Long snowFlakeId() {
		return snowflakeId.nextId();
	}

	public static Integer nextInt() {
		java.util.Random random = new java.util.Random();
		return Math.abs(random.nextInt());
	}

	public static Integer nextInt(int bound) {
		java.util.Random random = new java.util.Random();
		return Math.abs(random.nextInt(bound));
	}

	public static Integer nextInt(int lowBound, int upBound) {
		java.util.Random random = new java.util.Random();
		int num = random.nextInt(upBound - lowBound) + lowBound;
		return Math.abs(num);
	}

	public static Long nextLong() {
		java.util.Random random = new java.util.Random();
		return Math.abs(random.nextLong());
	}

	public static String javaUUID() {
		return javaUUID(false);
	}

	public static String javaUUID(boolean strikethrough) {
		if (strikethrough) {
			return java.util.UUID.randomUUID().toString();
		} else {
			return java.util.UUID.randomUUID().toString().replace("-", "");
		}
	}

}
