package me.duanbn.snailfish.core.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发执行器.
 * 
 * @author bingnan.dbn
 */
public class Executors {

	private static byte[] lock = new byte[0];

	private static ExecutorService threadPool;

	private static Executors instance;

	private Executors() {
	}

	public static Executors getInstance() {
		ExecutorConfig config = new ExecutorConfig();
		config.setWorkQueueSize(100000);
		config.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 10);
		config.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 200);
		config.setKeepAliveTime(1000L);
		config.setTimeUnit(TimeUnit.MILLISECONDS);
		return getInstance(config);
	}

	public static Executors getInstance(ExecutorConfig config) {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new Executors();
					threadPool = new ThreadPoolExecutor(config.getCorePoolSize(), config.getMaximumPoolSize(),
							config.getKeepAliveTime(), config.getTimeUnit(),
							new LinkedBlockingDeque<>(config.getWorkQueueSize()), new ThreadFactory() {
								private AtomicInteger threadNum = new AtomicInteger();

								@Override
								public java.lang.Thread newThread(Runnable r) {
									return new java.lang.Thread(r, "Snailfish-Worker-" + threadNum.incrementAndGet());
								}
							}, new ThreadPoolExecutor.CallerRunsPolicy());
				}
			}
		}

		return instance;
	}

	/** 创建并发执行器 */
	public Executor create() {
		Executor executor = new Executor(threadPool);
		return executor;
	}

	public void shutdown() {
		threadPool.shutdown();
	}

}
