package com.jiocloud.messages.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import com.datastax.driver.core.ResultSet;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

@Component
public class JCMExecutorService {

	//ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	//ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(500);
	ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
	
	public ListenableFuture<ResultSet> submit(Callable<ResultSet>task){
		return executor.submit(task);
	}
}
