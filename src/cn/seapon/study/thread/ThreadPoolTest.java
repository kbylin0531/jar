package cn.seapon.study.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class ThreadPoolTest {

	private static final Logger logger = Logger.getGlobal();
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
			
		testCachedThreadPool();
	}

	private static <V> void testCachedThreadPool() throws InterruptedException, ExecutionException {
		ExecutorService pool = Executors.newCachedThreadPool();
		
	   Future<String> result = pool.submit(new MyCallable());
	   logger.info(result.get());
		
	   //线程提交的返回值是null
	   @SuppressWarnings("unchecked")
	Future<String> result2 = (Future<String>) pool.submit(new MyRunnable());
	   logger.info(result2.get());
	}

}

class MyRunnable implements Runnable {

	@Override
	public void run() {
			Logger.getGlobal().info("Hello Zhonghuang!");
	}
	
}

class MyCallable implements Callable<String> {

	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		return "Hello world!";
	}
	
}
