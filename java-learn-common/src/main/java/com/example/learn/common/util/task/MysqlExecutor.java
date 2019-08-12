package com.example.learn.common.util.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @description: 用于处理数据库操作的线程池
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

@Slf4j
public enum MysqlExecutor {
	
	/**
	 * 用于mysql消费的线程池
	 */
	MYSQL_POOL;
	
	private ExecutorService pool = null;
	
	/**
	 * mysql线程池
	 */
	MysqlExecutor() {
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("MYSQL-POOL-%d")
				.build();
		
		pool = new ThreadPoolExecutor(ExecutorEnum.CORE_NUM.getValue(),
				ExecutorEnum.CORE_NUM.getValue(), ExecutorEnum.KEEP_ALIVE_TIME.getValue(), TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(ExecutorEnum.QUEUE_CAPACITY.getValue()),
				namedThreadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());
	}
	
	public ExecutorService getExecutor() {
		return pool;
	}
	
}
