package com.zhima.opendoor.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorManager {
	// 1.˽�й��캯�����ṩ��̬�������Դ洢���ṩ��̬�ķ���
	private ExecutorManager() {
		init();

	}

	// ��̬����
	private static ExecutorManager instance;

	// ��̬����
	public static ExecutorManager getInstance() {

		if (instance == null) {
			//����
			synchronized (ExecutorManager.class) {
				if (instance == null) {
					instance = new ExecutorManager();
				}
			}
		}
		return instance;
	}

	// ���õ���ģʽ����ֻ֤�ܹ���ʵ����һ��
	// ��������Ӧ�õ�����APP����
	private ExecutorService executorService;

	// ��ʼ���̳߳�
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
