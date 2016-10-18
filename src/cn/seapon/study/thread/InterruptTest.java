package cn.seapon.study.thread;

import java.util.logging.Logger;

public class InterruptTest {
	
	
	private static final Logger logger = Logger.getLogger("cn.seapon.study.thread");

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Thread thread1 = createThread();

		//----------------- 测试中断sleep BEGIN ------------------//
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		//----------------- 测试中断sleep END ------------------//
			
		//----------------- 测试中断wait BEGIN ------------------//
		// wait是Object的final方法，只能在同步控制方法或者同步控制代码块中使用
		//----------------- 测试中断wait END ------------------//
		
		
		//主线程上的调用thread1的intterupt方法  线程将
		thread1.interrupt();

		logger.info("主线程结束！");
	}
	
	/**
	 * 添加一个线程并将之返回
	 * @return Thread
	 */
	public static Thread createThread() {
		//线程1 每过3秒打印一条“Hello I'm Thread Zero!”
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						logger.info("这里打印检查时间");
						System.out.println("Hello I'm Thread Zero!"+Thread.currentThread());
						Thread.sleep(2000);//
					}
				} catch (InterruptedException e) {
					logger.info(e.getMessage());
					e.printStackTrace();
				}
			}
		});
		thread1.start();
		
		return thread1;
	}
		

}
