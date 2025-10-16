package me.duanbn.snailfish.core.concurrent;

import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.core.concurrent.Executor.ExecutorTaskException;
import me.duanbn.snailfish.util.collection.Maps;

/**
 * 并发执行结果.
 * 
 * @author bingnan.dbn
 */
@Slf4j
@Builder
public class ExecutorFuture {

	/** 执行结果future */
	@Builder.Default
	private Map<String, Future<Object>> futureMap = Maps.newConcurrentMap();
	/** 执行超时时间 */
	@Builder.Default
	private Map<String, Long> timeoutMap = Maps.newConcurrentMap();
	/** 执行超时时间单位 */
	@Builder.Default
	private Map<String, TimeUnit> timeUnitMap = Maps.newConcurrentMap();

	/**
	 * 放入执行结果.
	 * 
	 * @param name     执行任务名称
	 * @param value    执行结果
	 * @param timeout  超时时间
	 * @param timeUnit 超时时间单位
	 */
	public void put(String name, Future<Object> value, Long timeout, TimeUnit timeUnit) {
		this.futureMap.put(name, value);
		if (timeout != null && timeout > 0)
			this.timeoutMap.put(name, timeout);
		if (timeUnit != null)
			this.timeUnitMap.put(name, timeUnit);
	}

	/**
	 * 获取任务执行结果
	 * 
	 * @param name 执行任务名称
	 * @return 执行结果
	 */
	public Object get(String name) {
		Future<Object> future = this.futureMap.get(name);
		if (future == null) {
			return null;
		}

		try {
			Object taskResult = null;

			Long timeout = timeoutMap.get(name);
			TimeUnit timeUnit = timeUnitMap.get(name);

			if (timeout != null && timeUnit != null) {
				try {
					taskResult = future.get(timeout, timeUnit);
				} catch (TimeoutException e) {
					log.error("task timeout name = {}", name);
				}
			} else {
				taskResult = future.get();
			}

			return taskResult;
		} catch (Exception e) {
			throw new ExecutorTaskException("execute task failure", e);
		}
	}

}
