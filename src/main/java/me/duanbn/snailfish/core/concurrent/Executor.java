package me.duanbn.snailfish.core.concurrent;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lombok.Builder;
import lombok.Getter;
import me.duanbn.snailfish.util.collection.Maps;

/**
 * 并发执行器.
 * 
 * @author bingnan.dbn
 */
public class Executor {

	/** 线程池 */
	private ExecutorService threadPool;

	/** 待执行任务池 */
	private Map<String, ExecutorTask<Object>> taskPool = Maps.newConcurrentMap();

	public Executor(ExecutorService threadPool) {
		this.threadPool = threadPool;
	}

	/**
	 * 出发任务执行.
	 * 
	 * @param task 被执行任务.
	 */
	public void submit(Runnable task) {
		this.threadPool.submit(task);
	}

	/**
	 * 出发任务执行.
	 * 
	 * @param task 被执行任务.
	 */
	public void submit(Callable<Object> task) {
		this.threadPool.submit(task);
	}

	/**
	 * 触发任务执行.
	 * 
	 * @return 执行结果.
	 */
	public ExecutorFuture exec() {
		ExecutorFuture executorFuture = ExecutorFuture.builder().build();

		taskPool.forEach((k, v) -> {
			Future<Object> future = this.threadPool.submit(v.getCallable());
			executorFuture.put(k, future, v.getTimeout(), v.getTimeUnit());
		});

		return executorFuture;
	}

	/**
	 * 添加待执行任务.
	 * 
	 * @param name 任务名称，用于获取执行结果.
	 * @param task 执行任务
	 */
	public void addTask(String name, Callable<Object> task) {
		addTask(name, task, null, null);
	}

	/**
	 * 添加待执行任务.
	 * 
	 * @param name     任务名称，用于获取执行结果
	 * @param task     待执行任务
	 * @param timeout  任务超时时间
	 * @param timeUnit 任务超时时间单位
	 */
	public void addTask(String name, Callable<Object> task, Long timeout, TimeUnit timeUnit) {
		ExecutorTask<Object> executorTask = ExecutorTask.builder().callable(task).timeout(timeout).timeUnit(timeUnit)
				.build();
		this.taskPool.put(name, executorTask);
	}

	/**
	 * 并发执行任务.
	 * 
	 * @author bingnan.db
	 */
	@Builder
	public static class ExecutorTask<R> {

		/** 并发处理逻辑 */
		@Getter
		private Callable<R> callable;

		/** 超时时间 */
		@Getter
		private Long timeout;
		/** 超时时间单位 */
		@Getter
		private TimeUnit timeUnit;

	}

	/**
	 * @author bingnan.dbn
	 */
	public static class ExecutorTaskException extends RuntimeException {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ExecutorTaskException(String msg) {
			super(msg);
		}

		public ExecutorTaskException(Throwable t) {
			super(t);
		}

		public ExecutorTaskException(String msg, Throwable t) {
			super(msg, t);
		}

	}

}
