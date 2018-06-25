package com.mountainframework.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import com.mountainframework.client.CalcService;
import com.mountainframework.rpc.proxy.RpcMessageProxyExecutor;

public class CalcParallelRequestThread implements Runnable {

	private CountDownLatch signal;
	private CountDownLatch finish;
	private int idex;

	private static AtomicInteger integer = new AtomicInteger(1);

	public CalcParallelRequestThread(CountDownLatch signal, CountDownLatch finish, int idex) {
		this.signal = signal;
		this.finish = finish;
		this.idex = idex;
	}

	@Override
	public void run() {
		try {
			signal.await();
			CalcService calcService = RpcMessageProxyExecutor.getInstance().execute(CalcService.class);
			Integer result = null;
			if (calcService != null) {
				result = calcService.add(idex, idex);
				System.out.println(integer.getAndIncrement());
			}
			System.out.println(String.format("calc %s+%s add result:[%s]", idex, idex, result));
			finish.countDown();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}