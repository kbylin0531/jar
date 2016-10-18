package cn.seapon.study.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSynchronizationTest {

	public static int AmoutA = 100;
	public static int AmoutB = 100;
	/**
	 * 需要注意的是两个线程必须使用同一把锁才能实现同步执行
	 */
	private static ReentrantLock lock = null;
	
	/**
	 * 避免透支过多（10元以内）
	 */
	private static Condition condition = null;

	public static void main(String[] args) {
		
		testReentrantLock();
		
	}

	
	private static void testReentrantLock() {
		lock = new ReentrantLock();//注释掉它会使得现场无法同步
		condition = lock.newCondition();
		createThreadA().start();
		createThreadB().start();
	}
	
	/**
	 * 线程A 的任务 将B的钱转10元到A的账户中
	 * 
	 * @return
	 */
	public static Thread createThreadA() {
		Thread thread = new Thread(new MyRunnable(0,lock,condition));
		return thread;
	}

	/**
	 * 线程B 的任务 将A的钱转10元到B的账户中
	 * 
	 * @return
	 */
	public static Thread createThreadB() {
		Thread thread = new Thread(new MyRunnable(1,lock,condition));
		return thread;
	}

}

class MyRunnable implements Runnable {

	private int type = 0;

	/**
	 * 可重入锁
	 */
	private ReentrantLock lock = null;
	
	private Condition condition = null;

	public MyRunnable(int i,ReentrantLock lock,Condition condition) {
		this.type = i;
		this.lock = lock;
		this.condition = condition;
	}

	@Override
	public void run() {
		while (true) {
			if(null != lock) lock.lock();
			try {
				int amount = (int) (Math.random()*10);
				if (type == 1) {
					if(ThreadSynchronizationTest.AmoutA > 0){
						ThreadSynchronizationTest.AmoutA -= amount;
						ThreadSynchronizationTest.AmoutB += amount;
						condition.signalAll();//通知大家金额发生了变化
					}else{
						condition.await();
					}
				} else {
					if(ThreadSynchronizationTest.AmoutB > 0){
						ThreadSynchronizationTest.AmoutB -= amount;
						ThreadSynchronizationTest.AmoutA += amount;
						condition.signalAll();//通知大家金额发生了变化
					}else{
						condition.await();
					}
				}
				int sum = ThreadSynchronizationTest.AmoutA + ThreadSynchronizationTest.AmoutB;
				Utils.println("A:"+ThreadSynchronizationTest.AmoutA +"   B:"+ThreadSynchronizationTest.AmoutB + "   =   "+sum  );
				Thread.sleep(5);
			} catch (InterruptedException e) {
			}finally{
				if(null != lock) lock.unlock();
			}
		}
	}

}
