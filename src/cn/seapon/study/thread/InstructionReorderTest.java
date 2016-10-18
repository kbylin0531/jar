package cn.seapon.study.thread;

/**
 * CPU指令重排序测试
 * 
 * 未出现指定的情况****************
 * 
 * @author linzh
 *
 */
public class InstructionReorderTest {
	
	private static int a = 0;
	private static boolean flag = false;//标记a是偶已经被写入
		

	public static void writer() {
	    a = 1;                   //1
	    flag = true;             //2
	}
	public static void reader() {
	    if (flag) {                //3
	        int i =  a * a;        //4
	        if(0==i){
		        System.out.println("Yes bean!");
		        System.exit(0);
	        }
	    }
	}

	public static void main(String[] args) {


		
		while(true){
			Thread aThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					reader();
				}
			});

			Thread bThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					writer();
				}
			});
			aThread.start();
			bThread.start();
			try {
				Thread.sleep(10);
			    a = 0;                   //1
			    flag = false;             //2
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
	}


}
