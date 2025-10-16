package me.duanbn.snailfish.test.concurrent;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.concurrent.Executor;
import me.duanbn.snailfish.core.concurrent.ExecutorFuture;
import me.duanbn.snailfish.core.concurrent.Executors;

/**
 * @author bingnan.dbn
 */
@Slf4j
public class ExecutorsTest {

	@Test
	public void test() throws Exception {
		Executor executor = Executors.getInstance().create();
		executor.addTask("hello1", () -> this.hello(), 500L, TimeUnit.MILLISECONDS);
		executor.addTask("hello2", () -> this.hello("hello2"));
		executor.addTask("hello3", () -> this.hello(), 500L, TimeUnit.MILLISECONDS);

		long start = System.currentTimeMillis();

		ExecutorFuture executorFuture = executor.exec();
		String s = (String) executorFuture.get("hello1");
		log.info("{}", s);
		s = (String) executorFuture.get("hello2");
		log.info("{}", s);
		s = (String) executorFuture.get("hello3");
		log.info("{}", s);

		log.info("cost {}", (System.currentTimeMillis() - start));
	}

	private String hello() throws Exception {
		log.info(".");
		Thread.sleep(3000);
		return "hello snailfish concurrent executor";
	}

	private String hello(String value) throws Exception {
		log.info("..");
		Thread.sleep(1000);
		return value;
	}

}
