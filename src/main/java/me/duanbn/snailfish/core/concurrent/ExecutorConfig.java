package me.duanbn.snailfish.core.concurrent;

import java.util.concurrent.TimeUnit;

import lombok.Data;

@Data
public class ExecutorConfig {

	private int workQueueSize;

	private int corePoolSize;

	private int maximumPoolSize;

	private long keepAliveTime;

	private TimeUnit timeUnit;

}
