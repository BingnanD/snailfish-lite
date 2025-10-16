package me.duanbn.snailfish.test.util;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.util.Random;

@Slf4j
public class RandomTest {

	@Test
	public void testSnowFlakeId() {
		for (int i = 0; i < 20; i++) {
			log.info("{}", Random.snowFlakeId());
		}
	}

	@Test
	public void testJavaUUID() {
		log.info("{}", Random.javaUUID());
		log.info("{}", Random.javaUUID(true));
	}

	@Test
	public void testNextInteger() {
		log.info("{}", Random.nextInt());
		log.info("{}", Random.nextInt());
		log.info("{}", Random.nextInt());
		log.info("{}", Random.nextInt(1000, 3000));
	}

	@Test
	public void testNextLong() {
		List<Long> list = Lists.newArrayList();

		for (int i = 0; i < 100; i++) {
			long a = Random.nextLong();
			log.info("{} {}", a, list.contains(a));
			list.add(a);
		}
	}

}
