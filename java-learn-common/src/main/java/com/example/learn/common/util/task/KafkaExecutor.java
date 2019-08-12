package com.example.learn.common.util.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @description: 用于处理Kafka消息的线程池
 *
 * @author: xiaoTaoShi
 * @date: 2019/7/4 00:37
 * @version 1.0.0
 * @copyright (C) 2013 WonHigh Information Technology Co.,Ltd 
 *  All Rights Reserved. 
 *
 * The software for the WonHigh technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 */

public enum KafkaExecutor {
	
	/**
	 * 用于kafka消费的线程池
	 */
	KAFKA_POOL;
	
	private ExecutorService executorService = null;
	private ThreadPoolExecutor threadPoolExecutor = null;
	
	/**
	 * kafka线程池
	 */
	KafkaExecutor() {
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("KAFKA-POOL-%d")
				.build();
		
		/*
		 * 自定义异步线程池
		 * （1）任务队列使用有界队列
		 * （2）自定义拒绝策略(runnable, executor) -> log.error("MYSQL ASYNC POOL IS FULL!!!")
		 */
		final ThreadPoolExecutor kafkaPoolExecutor = new ThreadPoolExecutor(ExecutorEnum.CORE_NUM.getValue(),
				ExecutorEnum.MAX_NUM.getValue(), ExecutorEnum.KEEP_ALIVE_TIME.getValue(), TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(ExecutorEnum.QUEUE_CAPACITY.getValue()),
				namedThreadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());
		threadPoolExecutor = kafkaPoolExecutor;
		executorService = kafkaPoolExecutor;
	}
	
	public ExecutorService getExecutor() {
		return executorService;
	}
	
	public ThreadPoolExecutor getPool() {
		return threadPoolExecutor;
	}
}
