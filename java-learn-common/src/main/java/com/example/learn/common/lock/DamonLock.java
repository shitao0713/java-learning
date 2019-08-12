package com.example.learn.common.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @description 死锁测试类
 *
 * @author xiaoTaoShi
 * @date 2019/8/12 17:51
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
public class DamonLock implements Runnable {
	
	private boolean flag;
	
	/**
	 * 多线程共享锁变量
	 */
	private static final ReentrantLock LOCK_1 = new ReentrantLock();
	private static final ReentrantLock LOCK_2 = new ReentrantLock();
	
	@Override
	public void run() {
		if (flag) {
			synchronized (LOCK_1) {
				log.info("flag={}...获取lock1...", flag);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					log.error("异常!", e);
				}
				synchronized (LOCK_2) {
					log.info("flag={}...获取lock2...", flag);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						log.error("异常!", e);
					}
				}
			}
		}
		
		if (!flag) {
			log.info("flag={}...获取lock2...", flag);
			synchronized (LOCK_2) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					log.error("异常信息...", e);
				}
				synchronized (LOCK_1) {
					log.info("flag={}...获取lock1...", flag);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						log.error("异常信息...", e);
					}
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		DamonLock damonLock1 = new DamonLock();
		DamonLock damonLock2 = new DamonLock();
		damonLock1.flag = true;
		damonLock2.flag = false;
		new Thread(damonLock1).start();
		new Thread(damonLock2).start();
	}
	
}
