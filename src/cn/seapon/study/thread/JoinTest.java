package cn.seapon.study.thread;

import java.util.logging.Logger;


/**
 * 测试线程2 中奖线程1join到线程2的执行中
 * 
 * 只有等到现场1结束才能轮到线程2的执行
 * 
 * 接下来之后线程2 的执行
 * 
 * @author linzh
 *
 */
public class JoinTest {
	private static final Logger logger = Logger.getLogger("cn.seapon.study.thread");

	private static Thread thread1 = null;
	private static Thread thread2 = null;
	
	
	public static void main(String[] args) {
		createThread().start();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		createThread2().start();

		//一段时间后主动结束线程1
		try {
			Thread.sleep(11000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread1.interrupt();
		
		
		logger.info("主线程执行完毕");
		
	}
	
	
	/**
	 * 添加一个线程并将之返回
	 * @return Thread
	 */
	public static Thread createThread() {
		//线程1 每过3秒打印一条“Hello I'm Thread Zero!”
		thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				logger.info("线程0启动");
				try {
					while (true) {
						logger.info("Hello I'm Thread Zero!"+Thread.currentThread());
						Thread.sleep(2000);//
					}
				} catch (InterruptedException e) {
					logger.info(e.getMessage());
					e.printStackTrace();
				}
			}
		});
		
		return thread1;
	}
	/**
	 * 添加一个线程并将之返回
	 * @return Thread
	 */
	public static Thread createThread2() {
		//线程1 每过3秒打印一条“Hello I'm Thread Zero!”
		thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				logger.info("线程2启动");
				try {
					while (true) {
						logger.info("Hello I'm Thread One!"+Thread.currentThread());
						Thread.sleep(2000);//
						thread1.join();
					}
				} catch (InterruptedException e) {
					logger.info(e.getMessage());
					e.printStackTrace();
				}
			}
		});
		
		return thread2;
	}
}
