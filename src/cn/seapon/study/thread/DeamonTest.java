package cn.seapon.study.thread;

import java.util.logging.Logger;

public class DeamonTest {
	
	
	private static  Logger logger = Logger.getGlobal();

	public static void main(String[] args) {
		
		Thread deamon = new Thread(new Runnable() {
			@Override
			public void run() {
					try {	
						logger.info("守护线程已经被创建并准备睡10000秒！");
						Thread.sleep(10000000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally {
						logger.info("啊啊，即便是finally也无法执行，作为守护的我绝对不能访问固有资源！");
					}
			}
		});
		deamon.setDaemon(true);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 5; i++) {
					try {	
						logger.info("不是守护的我要睡一觉！");
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		

		logger.info("作为主线程的喔要退出了哈！");
		
	}

}
