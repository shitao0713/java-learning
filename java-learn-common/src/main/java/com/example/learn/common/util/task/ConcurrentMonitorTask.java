package com.example.learn.common.util.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @description 线程监控类
 *
 * @author xiaoTaoShi
 * @date 2019/7/4 1:52
 * @version 1.0.0
 * @copyright (C) 2013 WonHigh Information Technology Co.,Ltd All Rights Reserved.
 *
 * The software for the WonHigh technology development, without the company's written consent,
 * and any other individuals and organizations shall not be used, Copying, Modify or distribute
 * the software.
 */

@Slf4j
@Component
public class ConcurrentMonitorTask implements AutoCloseable {
	
	private ConcurrentMonitorTask() {
	}
	
	/**
	 * 用于周期性监控线程池的运行状态
	 */
	private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(
			new ThreadFactoryBuilder().setNameFormat("ASYNC EXECUTOR POOL MONITOR").build());
	
	@PostConstruct
	public void init() {
		scheduledExecutorService.scheduleAtFixedRate(this::threadPoolMonitorRecordedInfo, 0, 60, TimeUnit.SECONDS);
	}
	
	/**
	 *	记录线程池的信息
	 */
	private void threadPoolMonitorRecordedInfo() {
		
		ThreadPoolExecutor poolInstance = KafkaExecutor.KAFKA_POOL.getPool();
		if (poolInstance == null) {
			return;
		}
		
		/*
		 * 线程池需要执行的任务数
		 */
		long taskCount = poolInstance.getTaskCount();
		/*
		 * 线程池在运行过程中已完成的任务数
		 */
		long completedTaskCount = poolInstance.getCompletedTaskCount();
		/*
		 * 曾经创建过的最大线程数
		 */
		long largestPoolSize = poolInstance.getLargestPoolSize();
		/*
		 * 线程池里的线程数量
		 */
		long poolSize = poolInstance.getPoolSize();
		/*
		 * 线程池里活跃的线程数量
		 */
		long activeCount = poolInstance.getActiveCount();
		
		log.info("EXECUTOR POOL MONITOR...taskCount:{}, completedTaskCount:{}, largestPoolSize:{}, poolSize:{}, activeCount:{}",
				taskCount, completedTaskCount, largestPoolSize, poolSize, activeCount);
	}
	
	@Override
	public void close() throws Exception {
		scheduledExecutorService.shutdown();
	}
	
}
