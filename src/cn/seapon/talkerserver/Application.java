package cn.seapon.talkerserver;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 带图形界面的控制面板
 * @author linzh_000
 */
public class Application extends JFrame implements ActionListener{
	private static final long serialVersionUID = 2565893197041592106L;
	/**
	 * 空间平铺板
	 */
	private JPanel jPanel = new JPanel();
	/**
	 * 开始服务按钮
	 */
	private JButton startButton = new JButton("开启");
	/**
	 * 结束服务按钮
	 */
	private JButton stopButton = new JButton("关闭");
	/**
	 * 标记是否正在运行
	 */
	private boolean isRunning = false;

	public static void main(String[] args) {
		ActionNerve.logger.setLevel(Level.FINE);//INFO级别以上的日志全部记录（默认）
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Application();
			}
		});
	}

	private Application() {
		startButton.addActionListener(this);
		stopButton.addActionListener(this);
		jPanel.add(startButton);
		jPanel.add(stopButton);
		
		this.add(jPanel);
		this.setSize(500, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭窗口退出
		this.setVisible(true);
	}
	
	/**
	 * 端口号
	 * @param portNum
	 */
	private Application(short portNum) {
		this();
		Property.setPortNum(portNum);
	}
	
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if(source.equals(startButton)){
			//开启服务器
			if (!isRunning) {//未在运行
				ActionNerve.startService();
				isRunning = true;
				ActionNerve.logger.info("App服务开启完成!");
			}else {
				ActionNerve.logger.info("App服务已在运行当中，无法再次开启!");
			}
		}else if (source == stopButton) {/*对象的比较，是否引用了同一块堆地址*/
			if (isRunning) {//在运行
				ActionNerve.stopService();
				isRunning = false;
				ActionNerve.logger.info("App成功关闭!");
			}else{
				ActionNerve.logger.info("App服务未在运行状态，关闭失败！!");
			}
		}else {
			ActionNerve.logger.info("你点击了其他构件："+event.getSource());
		}
	}
	
	
}
