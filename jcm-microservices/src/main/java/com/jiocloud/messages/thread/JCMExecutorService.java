package com.jiocloud.messages.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.stereotype.Component;

@Component
public class JCMExecutorService {

	//ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10000);
	
	public void submit(Callable<String>task){
		executor.submit(task);
	}
}
