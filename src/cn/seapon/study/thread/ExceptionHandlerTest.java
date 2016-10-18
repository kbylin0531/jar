package cn.seapon.study.thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Logger;

public class ExceptionHandlerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		tesStaticMethod();
		testInstanceMethod();
	}
	
	
	public static void testInstanceMethod() {
		MyExceptionHandler handler = new MyExceptionHandler();
		for (int i = 0; i < 5; i++) {
			Thread thread = createThread();
			thread.setUncaughtExceptionHandler(handler);//start之前设置
			thread.start();
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){
					e.printStackTrace();
			}
		}
	}
	
	
	public static void tesStaticMethod() {
		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler());
		for (int i = 0; i < 5; i++) {
			Thread thread = createThread();
			thread.start();
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){
					e.printStackTrace();
			}
		}
	}
	
	private static Thread createThread() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Logger.getGlobal().info("新的线程创建完成并开始运行！");
				try{
					Thread.sleep(2000);
					throw new RuntimeException("这是一个错误！");
				}catch(InterruptedException e){
						e.printStackTrace();
				}
			}
		});
		return thread;
	}

}

class MyExceptionHandler implements UncaughtExceptionHandler{
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		Logger.getGlobal().info(t+"  线程异常退出 ：  "+e.getMessage());
	}
}
