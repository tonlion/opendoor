package com.zhima.opendoor.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorManager {
	// 1.私有构造函数，提供静态变量用以存储，提供静态的方法
	private ExecutorManager() {
		init();

	}

	// 静态变量
	private static ExecutorManager instance;

	// 静态方法
	public static ExecutorManager getInstance() {

		if (instance == null) {
			//加锁
			synchronized (ExecutorManager.class) {
				if (instance == null) {
					instance = new ExecutorManager();
				}
			}
		}
		return instance;
	}

	// 运用单例模式，保证只能够被实例化一次
	// 这个类就能应用到其他APP中了
	private ExecutorService executorService;

	// 初始化线程池
	private void init() {
		int threadCount = Runtime.getRuntime().availableProcessors() * 2 + 1;
		executorService = Executors
				.newFixedThreadPool(Math.min(threadCount, 8));
		
	}
	public ExecutorService getExecutor(){
		return this.executorService;
	}
	public void execute(Runnable runnable){
		this.executorService.execute(runnable);
	}

}
