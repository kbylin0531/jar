package cn.seapon.study.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

public class BlockingQueneTest {
	

	public static void main(String[] args) {
		testArrayBlockingQuene();
		
	}

	public static void testArrayBlockingQuene() {
		
		Thread consumer = new Thread(new Consumer());
		Thread producer = new Thread(new Producer());

		producer.start();
		consumer.start();
		
	}
	
}


class Observer {
	protected static Logger logger = Logger.getGlobal();
	protected static  ArrayBlockingQueue<String> quene = new ArrayBlockingQueue<String>(5);
	protected static int producerInterval = 10;
	protected static int consumerInterval = 1500;
	
	
	protected void testPutTakeProducerMethod() {
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(producerInterval);
				String string = " '"+i + "'";
				quene.put(string);//put方法可能会因为队列满员而阻塞，中断阻塞过程会导致中断异常
				logger.info("Consumer put "+string);
			} catch (InterruptedException e) {}
		}
	}	
	protected void testPutTakeConsumerMethod() {
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(consumerInterval);
				String taked = quene.take();
				logger.info("Consumer take"+taked);//put方法可能会因为队列满员而阻塞，中断阻塞过程会导致中断异常
			} catch (InterruptedException e) {}
		}
	}
	
	//TODO:测试add,remove方法
	//TODO: 测试offer，poll方法
	
}

class Producer extends Observer implements  Runnable {
	
	public void run() {
		testPutTakeProducerMethod();
	}
}

class Consumer extends Observer implements Runnable{
	public void run() {
		testPutTakeConsumerMethod();
	}
	
}
