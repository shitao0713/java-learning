	package com.example.learn.common.util.task;
	
	/**
	* @description:
	*
	* @author: xiaoTaoShi
	* @date: 2019/7/4 01:24
	* @version 1.0.0
	* @copyright (C) 2013 WonHigh Information Technology Co.,Ltd
	*  All Rights Reserved.
	*
	* The software for the WonHigh technology development, without the
	* company's written consent, and any other individuals and
	* organizations shall not be used, Copying, Modify or distribute
	* the software.
	*/
	
	public enum ExecutorEnum {
		
		/**
		 * 核心线程数
		 */
		CORE_NUM(10),
		
		/**
		 * 最大线程数
		 */
		MAX_NUM(50),
		/**
		 * 活跃时间
		 */
		KEEP_ALIVE_TIME(1000),
		
		/**
		 * 有界队列大小
		 */
		QUEUE_CAPACITY(1000000);
		
		private int value;
		
		ExecutorEnum(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
	}
