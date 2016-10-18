package cn.seapon.study;


import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;  
import javax.swing.JFileChooser;  
import javax.swing.JFrame;  
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;  

@SuppressWarnings("serial")
/**
 * 需求说明:
 * 
 * 将某一个文件夹下的所有文件中（包括子文件夹）的内容中
 * 获取 http开头，jpg结尾的字符串并全部写入到一个目标文件中
 * 
 * 若要双击运行：
 * 新建run.bat文件，并且写入"javaw.exe -jar aaa.jar"
 * 
 * @author linzh_000
 *
 */
public class FileChooser extends JFrame implements ActionListener{

		private JButton openinput = null;  
	    private JButton openoutput = null;  
	    private JTextField inputtext = null;  
	    private JTextField outputtext = null;
	    private JTextField regtext = null;
	    
	    private JButton start = null;
	    
	    private String dftreg = "(http|https)://.*?\\.jpg";

	    private 	BufferedWriter bufferedWriter = null;
	    
//		private StringBuilder stringBuilder = new StringBuilder();
		private Pattern pattern = null;
		
	    public static void main(String[] args) {  
	        new FileChooser();  
	    }  
	    
	    public FileChooser(){  
	    	
	    	openinput=new JButton("选择");  
	        openinput.addActionListener(this);  
	        openoutput=new JButton("选择");  
	        openoutput.addActionListener(this);  
	        start = new JButton("开始");
	        start.addActionListener(this);  
	        
	    	JPanel jPanel = new JPanel();


	    	JLabel label3=new JLabel("正则表达式：");
	        regtext=new JTextField();
	        regtext.setColumns(16);
	        regtext.setText(dftreg);
	        jPanel.add(label3);
	        jPanel.add(regtext);
	    	
	        JLabel label1=new JLabel("输入目录：");
	        inputtext=new JTextField();
	        inputtext.setColumns(10);
	        jPanel.add(label1);
	        jPanel.add(inputtext);
	    	jPanel.add(openinput);
	        JLabel label2=new JLabel("输出文件：");
	        outputtext=new JTextField();
	        outputtext.setColumns(10);
	        jPanel.add(label2);
	        jPanel.add(outputtext);
	    	jPanel.add(openoutput);
	    	jPanel.add(start);
	    	
	    	this.add(jPanel);
	        this.setBounds(200, 300, 200, 300);  
	        this.setVisible(true);  
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    }  

		@Override
		public void actionPerformed(ActionEvent e) {
			Object sourse = e.getSource();
			if(sourse.equals(openinput)){
				File file = getFile(1);
		        String absolutePath = file.getAbsolutePath();//文件绝对路径
//		        String filename = file.getName();//文件名称
//		        System.out.println((file.isDirectory()?"Directory:":"File:" )+ absolutePath);
		        if(!Files.exists(Paths.get(absolutePath))){
		        	showMessage("输入目录不存在");
		        	return ;
		        }
		        inputtext.setText(absolutePath);
			}else if (sourse.equals(openoutput)) {
				File file = getFile(2);
		        String absolutePath = file.getAbsolutePath();//文件绝对路径
		        outputtext.setText(absolutePath);
			}else if(sourse.equals(start)){
		    	pattern = Pattern.compile(regtext.getText());
				String inputpath = inputtext.getText();
				String outputpath = outputtext.getText();
				
				//输入路径检查
				if(!Files.exists(Paths.get(inputpath))){
					showMessage("输入目录不存在！");
					return;
				}
				
				//覆盖文件的情况
		        if(Files.exists(Paths.get(outputpath))){
		        	int option = showConfirm("文件已经存在，是否覆盖");
		        	if(JOptionPane.YES_OPTION != option) return ;
		        }
				
				try {
					bufferedWriter = new BufferedWriter(new FileWriter(new File(outputpath)));
					yesbean(inputpath);
//					System.out.println(stringBuilder.toString());
				} catch (IOException e2) {
					e2.printStackTrace();
				} finally {
					try {
						bufferedWriter.close();
					} catch (Exception e3) {
							e3.printStackTrace();
					}
				}
				showMessage("完成。。。","提示",1);
			}
		}

		@SuppressWarnings("unused")
		private int showConfirm(String message,String titile){
			return JOptionPane.showConfirmDialog(null, message, titile, JOptionPane.YES_NO_OPTION); 
		}
		private int showConfirm(String message){
			return JOptionPane.showConfirmDialog(null, message, "请选择", JOptionPane.YES_NO_OPTION); 
		}

		private void showMessage(String message,String titile,int type){
			type = type > 0 ?JOptionPane.INFORMATION_MESSAGE:JOptionPane.ERROR_MESSAGE;
			JOptionPane.showMessageDialog(null, message, titile, type); 
		}
		private void showMessage(String message){
			JOptionPane.showMessageDialog(null, message, "错误", JOptionPane.ERROR_MESSAGE); 
		}
		
		private void yesbean(String inputpath) throws IOException{
			DirectoryStream<Path> entries = Files.newDirectoryStream(Paths.get(inputpath));
			for(Path entry :entries){
				File file = entry.toFile();
				if(file.isDirectory()){
					yesbean(file.getAbsolutePath());
				}else{
					BufferedReader bfReader = null;
					try {
						bfReader = new BufferedReader(new FileReader(file),8192);
						String content = "";
						String line = null;
						while (null != (line = bfReader.readLine())) {
							content += line;
						}
						Matcher matcher = pattern.matcher(content);
						if(matcher.find()){
							int start = matcher.start();
							int end = matcher.end();
							String match = content.substring(start,end);
//							stringBuilder.append(match+System.lineSeparator());
							bufferedWriter.write(match+System.lineSeparator());
							bufferedWriter.flush();
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						bfReader.close();
					}
				}
			}
		}
		
		/**
		 * 获取选择文件
		 * @return
		 */
		private File getFile(int type){
			switch (type) {
			case 1:
				type = JFileChooser.DIRECTORIES_ONLY;
				break;
			case 2:
				type = JFileChooser.FILES_ONLY;
				break;
			case 0:
			default:
				type = JFileChooser.FILES_AND_DIRECTORIES;
				break;
			}
	        JFileChooser jfc=new JFileChooser();  
	        jfc.setFileSelectionMode(type);  
	        jfc.showDialog(new JLabel(), "选择");  
	        File file=jfc.getSelectedFile();  
	        return  file;
		}
		
}