package cn.seapon.study.thread;

public class MonitorTest {
	
	/**
	 * 无论是否添加volatile，最后的结果都非1000
	 */
	public volatile static int count = 0;
//	public static int count = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 同时启动1000个线程，去进行i++计算，看看实际结果

		for (int i = 0; i < 1000; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					MonitorTest.inc();
				}
			}).start();
		}
		//实际运算结果每次可能都不一样，本机的结果为：运行结果:Counter.count=981，可以看出，在多线程的环境下，Counter.count并没有期望结果是1000
		System.out.println("运行结果:Counter.count=" + MonitorTest.count);
	}

	public static void inc() {

		// 这里延迟1毫秒，使得结果明显（距离1000拉大了距离）
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {}

		count++;
	}

}
